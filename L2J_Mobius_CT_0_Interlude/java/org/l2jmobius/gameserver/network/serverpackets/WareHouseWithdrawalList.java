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

import java.util.Collection;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.PacketLogger;
import org.l2jmobius.gameserver.network.ServerPackets;

public class WareHouseWithdrawalList extends ServerPacket
{
	public static final int PRIVATE = 1;
	public static final int CLAN = 4;
	public static final int CASTLE = 3; // not sure
	public static final int FREIGHT = 1;
	
	private long _playerAdena;
	private Collection<Item> _items;
	/**
	 * <ul>
	 * <li>0x01-Private Warehouse</li>
	 * <li>0x02-Clan Warehouse</li>
	 * <li>0x03-Castle Warehouse</li>
	 * <li>0x04-Warehouse</li>
	 * </ul>
	 */
	private int _whType;
	
	public WareHouseWithdrawalList(Player player, int type)
	{
		if (player.getActiveWarehouse() == null)
		{
			PacketLogger.warning("Error while sending withdraw request to: " + player.getName());
			return;
		}
		_playerAdena = player.getAdena();
		_items = player.getActiveWarehouse().getItems();
		_whType = type;
	}
	
	@Override
	public void write()
	{
		ServerPackets.WAREHOUSE_WITHDRAW_LIST.writeId(this);
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
