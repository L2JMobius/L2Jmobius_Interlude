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

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.Henna;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author Zoey76
 */
public class HennaRemoveList extends ServerPacket
{
	private final Player _player;
	
	public HennaRemoveList(Player player)
	{
		_player = player;
	}
	
	@Override
	public void write()
	{
		ServerPackets.HENNA_REMOVE_LIST.writeId(this);
		writeInt(_player.getAdena());
		writeInt(0);
		writeInt(3 - _player.getHennaEmptySlots());
		for (Henna henna : _player.getHennaList())
		{
			if (henna != null)
			{
				writeInt(henna.getDyeId());
				writeInt(henna.getDyeItemId());
				writeInt(henna.getCancelCount());
				writeInt(0);
				writeInt(henna.getCancelFee());
				writeInt(0);
				writeInt(1);
			}
		}
	}
}
