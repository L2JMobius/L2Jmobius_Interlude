/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver.network.clientpackets;

import org.l2jmobius.commons.network.ReadablePacket;
import org.l2jmobius.gameserver.network.ExClientPackets;
import org.l2jmobius.gameserver.network.GameClient;

/**
 * @author Nos
 */
public class ExPacket implements ClientPacket
{
	private ExClientPackets _packetEnum;
	private ClientPacket _newPacket;
	
	@Override
	public void read(ReadablePacket packet)
	{
		final int exPacketId = packet.readShort() & 0xFFFF;
		if ((exPacketId < 0) || (exPacketId >= ExClientPackets.PACKET_ARRAY.length))
		{
			return;
		}
		
		_packetEnum = ExClientPackets.PACKET_ARRAY[exPacketId];
		if (_packetEnum == null)
		{
			return;
		}
		
		_newPacket = _packetEnum.newPacket();
		if (_newPacket == null)
		{
			return;
		}
		
		_newPacket.read(packet);
	}
	
	@Override
	public void run(GameClient client)
	{
		if (_newPacket == null)
		{
			return;
		}
		
		if (!_packetEnum.getConnectionStates().contains(client.getConnectionState()))
		{
			return;
		}
		
		_newPacket.run(client);
	}
}
