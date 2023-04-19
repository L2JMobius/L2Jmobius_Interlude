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

import org.l2jmobius.gameserver.model.ItemInfo;
import org.l2jmobius.gameserver.model.TradeItem;
import org.l2jmobius.gameserver.model.item.instance.Item;

/**
 * @author UnAfraid
 */
public abstract class AbstractItemPacket extends ServerPacket
{
	protected void writeItem(TradeItem item)
	{
		writeItem(new ItemInfo(item));
	}
	
	protected void writeItem(Item item)
	{
		writeItem(new ItemInfo(item));
	}
	
	protected void writeItem(ItemInfo item)
	{
		writeShort(item.getItem().getType1());
		writeInt(item.getObjectId()); // ObjectId
		writeInt(item.getItem().getDisplayId()); // ItemId
		writeInt(item.getCount()); // Quantity
		writeShort(item.getItem().getType2()); // Item Type 2 : 00-weapon, 01-shield/armor, 02-ring/earring/necklace, 03-questitem, 04-adena, 05-item
		writeShort(item.getCustomType1()); // Filler (always 0)
		writeShort(item.getEquipped()); // Equipped : 00-No, 01-yes
		writeInt(item.getItem().getBodyPart()); // Slot : 0006-lr.ear, 0008-neck, 0030-lr.finger, 0040-head, 0100-l.hand, 0200-gloves, 0400-chest, 0800-pants, 1000-feet, 4000-r.hand, 8000-r.hand
		writeShort(item.getEnchant()); // Enchant level (pet level shown in control item)
		writeShort(item.getCustomType2()); // Pet name exists or not shown in control item
		writeInt(item.getAugmentationBonus());
		writeInt(item.getMana());
	}
}
