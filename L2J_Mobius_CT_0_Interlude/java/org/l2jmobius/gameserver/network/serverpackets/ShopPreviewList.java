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

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.buylist.BuyListHolder;
import org.l2jmobius.gameserver.model.buylist.Product;
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.network.ServerPackets;

public class ShopPreviewList extends ServerPacket
{
	private final int _listId;
	private final Collection<Product> _list;
	private final long _money;
	private int _expertise;
	
	public ShopPreviewList(BuyListHolder list, long currentMoney, int expertiseIndex)
	{
		_listId = list.getListId();
		_list = list.getProducts();
		_money = currentMoney;
		_expertise = expertiseIndex;
	}
	
	public ShopPreviewList(Collection<Product> lst, int listId, long currentMoney)
	{
		_listId = listId;
		_list = lst;
		_money = currentMoney;
	}
	
	@Override
	public void write()
	{
		ServerPackets.SHOP_PREVIEW_LIST.writeId(this);
		writeByte(0xC0); // ?
		writeByte(0x13); // ?
		writeByte(0); // ?
		writeByte(0); // ?
		writeInt((int) _money); // current money
		writeInt(_listId);
		int newlength = 0;
		for (Product product : _list)
		{
			if ((product.getItem().getCrystalType().getLevel() <= _expertise) && product.getItem().isEquipable())
			{
				newlength++;
			}
		}
		writeShort(newlength);
		for (Product product : _list)
		{
			if ((product.getItem().getCrystalType().getLevel() <= _expertise) && product.getItem().isEquipable())
			{
				writeInt(product.getItemId());
				writeShort(product.getItem().getType2()); // item type2
				if (product.getItem().getType1() != ItemTemplate.TYPE1_ITEM_QUESTITEM_ADENA)
				{
					writeShort(product.getItem().getBodyPart()); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
				}
				else
				{
					writeShort(0); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
				}
				writeInt(Config.WEAR_PRICE);
			}
		}
	}
}
