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

import org.l2jmobius.gameserver.model.actor.instance.Door;
import org.l2jmobius.gameserver.network.ServerPackets;

public class DoorInfo extends ServerPacket
{
	private final Door _door;
	
	public DoorInfo(Door door)
	{
		_door = door;
	}
	
	@Override
	public void write()
	{
		ServerPackets.DOOR_INFO.writeId(this);
		writeInt(_door.getObjectId());
		writeInt(_door.getId());
	}
}
