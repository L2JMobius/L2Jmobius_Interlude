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

import org.l2jmobius.gameserver.network.ServerPackets;

public class ExPutItemResultForVariationMake extends ServerPacket
{
	private final int _itemObjId;
	private final int _itemId;
	
	public ExPutItemResultForVariationMake(int itemObjId, int itemId)
	{
		_itemObjId = itemObjId;
		_itemId = itemId;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_PUT_ITEM_RESULT_FOR_VARIATION_MAKE.writeId(this);
		writeInt(_itemObjId);
		writeInt(_itemId);
		writeInt(1);
	}
}
