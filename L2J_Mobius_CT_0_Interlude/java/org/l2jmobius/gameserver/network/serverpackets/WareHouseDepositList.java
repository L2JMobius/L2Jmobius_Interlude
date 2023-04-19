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

import java.util.ArrayList;
import java.util.List;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.ServerPackets;

public class WareHouseDepositList extends ServerPacket
{
	public static final int PRIVATE = 1;
	public static final int CLAN = 4;
	public static final int CASTLE = 3; // not sure
	public static final int FREIGHT = 1;
	
	private final long _playerAdena;
	private final List<Item> _items = new ArrayList<>();
	/**
	 * <ul>
	 * <li>0x01-Private Warehouse</li>
	 * <li>0x02-Clan Warehouse</li>
	 * <li>0x03-Castle Warehouse</li>
	 * <li>0x04-Warehouse</li>
	 * </ul>
	 */
	private final int _whType;
	
	public WareHouseDepositList(Player player, int type)
	{
		_whType = type;
		_playerAdena = player.getAdena();
		final boolean isPrivate = _whType == PRIVATE;
		for (Item temp : player.getInventory().getAvailableItems(true, isPrivate, false))
		{
			if ((temp != null) && temp.isDepositable(isPrivate))
			{
				_items.add(temp);
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.WAREHOUSE_DEPOSIT_LIST.writeId(this);
		writeShort(_whType);
		writeInt((int) _playerAdena);
		writeShort(_items.size());
		for (Item item : _items)
		{
			writeShort(item.getTemplate().getType1());
			writeInt(item.getObjectId());
			writeInt(item.getId());
			writeInt(item.getCount());
			writeShort(item.getTemplate().getType2());
			writeShort(item.getCustomType1());
			writeInt(item.getTemplate().getBodyPart());
			writeShort(item.getEnchantLevel());
			writeShort(0);
			writeShort(item.getCustomType2());
			writeInt(item.getObjectId());
			if (item.isAugmented())
			{
				writeInt(0x0000FFFF & item.getAugmentation().getAugmentationId());
				writeInt(item.getAugmentation().getAugmentationId() >> 16);
			}
			else
			{
				writeLong(0);
			}
		}
	}
}
