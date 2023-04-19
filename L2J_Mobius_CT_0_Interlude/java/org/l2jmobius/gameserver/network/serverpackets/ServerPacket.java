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

import org.l2jmobius.commons.network.WritablePacket;
import org.l2jmobius.gameserver.model.actor.Player;

/**
 * @author Mobius
 */
public abstract class ServerPacket extends WritablePacket
{
	/**
	 * Construct a ServerPacket with an initial data size of 32 bytes.
	 */
	protected ServerPacket()
	{
		super(32);
	}
	
	/**
	 * Construct a ServerPacket with a given initial data size.
	 * @param initialSize
	 */
	protected ServerPacket(int initialSize)
	{
		super(initialSize);
	}
	
	/**
	 * Method that runs after packet is sent.
	 * @param player
	 */
	public void run(Player player)
	{
	}
}
