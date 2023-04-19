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
import org.l2jmobius.gameserver.network.ServerPackets;

public class ItemList extends AbstractItemPacket
{
	private final Collection<Item> _items;
	private final boolean _showWindow;
	
	public ItemList(Player player, boolean showWindow)
	{
		_showWindow = showWindow;
		_items = player.getInventory().getItems();
	}
	
	@Override
	public void write()
	{
		ServerPackets.ITEM_LIST.writeId(this);
		writeShort(_showWindow);
		writeShort(_items.size());
		for (Item item : _items)
		{
			writeItem(item);
		}
	}
}
