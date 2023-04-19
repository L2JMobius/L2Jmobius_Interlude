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

import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.ServerPackets;

public class ExPutItemResultForVariationCancel extends ServerPacket
{
	private final int _itemObjId;
	private final int _price;
	
	public ExPutItemResultForVariationCancel(Item item, int price)
	{
		_itemObjId = item.getObjectId();
		_price = price;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PUT_ITEM_RESULT_FOR_VARIATION_CANCEL.writeId(this);
		writeInt(0x40A97712);
		writeInt(_itemObjId);
		writeInt(0x27);
		writeInt(0x2006);
		writeLong(_price);
		writeInt(1);
	}
}
