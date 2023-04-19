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
package org.l2jmobius.gameserver.network.serverpackets;

import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.ServerPackets;

public class Ride extends ServerPacket
{
	private final int _objectId;
	private final boolean _mounted;
	private final int _rideType;
	private final int _rideNpcId;
	private final Location _loc;
	
	public Ride(Player player)
	{
		_objectId = player.getObjectId();
		_mounted = player.isMounted();
		_rideType = player.getMountType().ordinal();
		_rideNpcId = player.getMountNpcId() + 1000000;
		_loc = player.getLocation();
	}
	
	@Override
	public void write()
	{
		ServerPackets.RIDE.writeId(this);
		writeInt(_objectId);
		writeInt(_mounted);
		writeInt(_rideType);
		writeInt(_rideNpcId);
		writeInt(_loc.getX());
		writeInt(_loc.getY());
		writeInt(_loc.getZ());
	}
}
