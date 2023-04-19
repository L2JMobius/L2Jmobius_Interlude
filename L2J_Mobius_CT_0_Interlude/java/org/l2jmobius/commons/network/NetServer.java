package org.l2jmobius.commons.network;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 * @param <E> extends NetClient
 */
public class NetServer<E extends NetClient>
{
	protected static final Logger LOGGER = Logger.getLogger(NetServer.class.getName());
	
	protected final List<Set<E>> _clientReadPools = new LinkedList<>();
	protected final List<Set<E>> _clientExecutePools = new LinkedList<>();
	protected final NetConfig _netConfig = new NetConfig();
	protected final String _hostname;
	protected final int _port;
	protected final Supplier<E> _clientSupplier;
	protected final PacketHandlerInterface<E> _packetHandler;
	protected String _name = getClass().getSimpleName();
	
	/**
	 * Creates a new NetServer.
	 * @param port that the server will listen for incoming connections.
	 * @param packetHandler that will be used to handle incoming data.
	 * @param clientSupplier that will be used to create new client objects.
	 */
	public NetServer(int port, PacketHandlerInterface<E> packetHandler, Supplier<E> clientSupplier)
	{
		this("0.0.0.0", port, packetHandler, clientSupplier);
	}
	
	/**
	 * Creates a new NetServer.
	 * @param hostname of the server, use 0.0.0.0 to bind on all available IPs.
	 * @param port that the server will listen for incoming connections.
	 * @param packetHandler that will be used to handle incoming data.
	 * @param clientSupplier that will be used to create new client objects.
	 */
	public NetServer(String hostname, int port, PacketHandlerInterface<E> packetHandler, Supplier<E> clientSupplier)
	{
		_hostname = hostname;
		_port = port;
		_packetHandler = packetHandler;
		_clientSupplier = clientSupplier;
	}
	
	/**
	 * Use alternative name for this NetServer object.
	 * @param name
	 */
	public void setName(String name)
	{
		_name = name;
	}
	
	/**
	 * @return the network configurations of this server.
	 */
	public NetConfig getNetConfig()
	{
		return _netConfig;
	}
	
	/**
	 * Start listening for clients.
	 */
	public void start()
	{
		if (_clientSupplier == null)
		{
			LOGGER.warning(_name + ": Could not start because client Supplier was not set.");
			return;
		}
		
		if (_packetHandler == null)
		{
			LOGGER.warning(_name + ": Could not start because PacketHandler was not set.");
			return;
		}
		
		// Runs on a separate thread.
		final Thread thread = new Thread(new NetworkListenerThread(), _name + ": Network listener thread");
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.setDaemon(true);
		thread.start();
	}
	
	private class NetworkListenerThread implements Runnable
	{
		public NetworkListenerThread()
		{
		}
		
		@Override
		public void run()
		{
			// Create server and bind port.
			try (ServerSocketChannel server = ServerSocketChannel.open())
			{
				server.bind(new InetSocketAddress(_hostname, _port));
				server.configureBlocking(false); // Non-blocking I/O.
				
				// Listen for new connections.
				LOGGER.info(_name + ": Listening on port " + _port + " for incoming connections.");
				long executionStart;
				long currentTime;
				while (true)
				{
					executionStart = System.currentTimeMillis();
					
					final SocketChannel channel = server.accept();
					if (channel != null)
					{
						// Configure channel.
						channel.socket().setTcpNoDelay(_netConfig.isTcpNoDelay());
						channel.socket().setSoTimeout(_netConfig.getConnectionTimeout());
						channel.configureBlocking(false); // Non-blocking I/O.
						
						// Create client.
						final E client = _clientSupplier.get();
						client.init(channel, _netConfig);
						
						// Add to read pool.
						
						// Find a pool that is not full.
						boolean readPoolFound = false;
						READ_POOLS: for (Set<E> pool : _clientReadPools)
						{
							if (pool.size() < _netConfig.getReadPoolSize())
							{
								pool.add(client);
								readPoolFound = true;
								break READ_POOLS;
							}
						}
						
						// All pools are full.
						if (!readPoolFound)
						{
							// Create a new client pool.
							final Set<E> newReadPool = ConcurrentHashMap.newKeySet(_netConfig.getReadPoolSize());
							newReadPool.add(client);
							// Create a new task for the new pool.
							final Thread readThread = new Thread(new ReadThread<>(newReadPool), _name + ": Packet read thread " + _clientReadPools.size());
							readThread.setPriority(Thread.MAX_PRIORITY);
							readThread.setDaemon(true);
							readThread.start();
							// Add the new pool to the pool list.
							_clientReadPools.add(newReadPool);
						}
						
						// Add to execute pool.
						
						// Find a pool that is not full.
						boolean executePoolFound = false;
						EXECUTE_POOLS: for (Set<E> pool : _clientExecutePools)
						{
							if (pool.size() < _netConfig.getExecutePoolSize())
							{
								pool.add(client);
								executePoolFound = true;
								break EXECUTE_POOLS;
							}
						}
						
						// All pools are full.
						if (!executePoolFound)
						{
							// Create a new client pool.
							final Set<E> newExecutePool = ConcurrentHashMap.newKeySet(_netConfig.getExecutePoolSize());
							newExecutePool.add(client);
							// Create a new task for the new pool.
							final Thread executeThread = new Thread(new ExecuteThread<>(newExecutePool, _packetHandler), _name + ": Packet execute thread " + _clientExecutePools.size());
							executeThread.setPriority(Thread.MAX_PRIORITY);
							executeThread.setDaemon(true);
							executeThread.start();
							// Add the new pool to the pool list.
							_clientExecutePools.add(newExecutePool);
						}
					}
					
					// Prevent high CPU caused by repeatedly polling the channel.
					currentTime = System.currentTimeMillis();
					if ((currentTime - executionStart) < 1)
					{
						Thread.sleep(1);
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.warning(_name + ": Problem initializing. " + e);
			}
		}
	}
}
