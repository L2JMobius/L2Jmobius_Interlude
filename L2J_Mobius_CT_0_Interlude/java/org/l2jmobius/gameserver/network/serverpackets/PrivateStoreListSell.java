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

import org.l2jmobius.gameserver.instancemanager.SellBuffsManager;
import org.l2jmobius.gameserver.model.TradeItem;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.ServerPackets;

public class PrivateStoreListSell extends ServerPacket
{
	private final Player _player;
	private final Player _seller;
	
	public PrivateStoreListSell(Player player, Player seller)
	{
		_player = player;
		_seller = seller;
	}
	
	@Override
	public void write()
	{
		if (_seller.isSellingBuffs())
		{
			SellBuffsManager.getInstance().sendBuffMenu(_player, _seller, 0);
		}
		else
		{
			ServerPackets.PRIVATE_STORE_LIST_SELL.writeId(this);
			writeInt(_seller.getObjectId());
			writeInt(_seller.getSellList().isPackaged());
			writeInt(_player.getAdena());
			writeInt(_seller.getSellList().getItems().size());
			for (TradeItem item : _seller.getSellList().getItems())
			{
				writeInt(item.getItem().getType2());
				writeInt(item.getObjectId());
				writeInt(item.getItem().getId());
				writeInt(item.getCount());
				writeShort(0);
				writeShort(item.getEnchant());
				writeShort(item.getCustomType2());
				writeInt(item.getItem().getBodyPart());
				writeInt(item.getPrice()); // your price
				writeInt(item.getItem().getReferencePrice()); // store price
			}
		}
	}
}
