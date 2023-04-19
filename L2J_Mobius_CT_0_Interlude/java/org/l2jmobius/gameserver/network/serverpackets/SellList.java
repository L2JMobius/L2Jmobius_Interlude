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

/**
 * @author Mobius
 */
public class SellList extends ServerPacket
{
	private final int _money;
	private final List<Item> _items = new ArrayList<>();
	
	public SellList(Player player)
	{
		_money = player.getAdena();
		for (Item item : player.getInventory().getItems())
		{
			if (!item.isEquipped() && item.getTemplate().isSellable() && ((player.getSummon() == null) || (item.getObjectId() != player.getSummon().getControlObjectId())))
			{
				_items.add(item);
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.SELL_LIST.writeId(this);
		writeInt(_money);
		writeInt(0);
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
			writeShort(item.getCustomType2());
			writeShort(0);
			writeInt(item.getTemplate().getReferencePrice() / 2);
		}
	}
}
