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
package org.l2jmobius.gameserver.network.clientpackets;

import org.l2jmobius.commons.network.ReadablePacket;
import org.l2jmobius.gameserver.enums.ShortcutType;
import org.l2jmobius.gameserver.model.Shortcut;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.serverpackets.ShortCutRegister;

public class RequestShortCutReg implements ClientPacket
{
	private ShortcutType _type;
	private int _id;
	private int _slot;
	private int _page;
	@SuppressWarnings("unused")
	private int _level;
	
	@Override
	public void read(ReadablePacket packet)
	{
		final int typeId = packet.readInt();
		_type = ShortcutType.values()[(typeId < 1) || (typeId > 6) ? 0 : typeId];
		final int slot = packet.readInt();
		_slot = slot % 12;
		_page = slot / 12;
		_id = packet.readInt();
		_level = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if ((_page > 10) || (_page < 0))
		{
			return;
		}
		
		switch (_type)
		{
			case ITEM:
			case ACTION:
			case MACRO:
			case RECIPE:
			{
				final Shortcut sc = new Shortcut(_slot, _page, _type, _id, -1);
				player.sendPacket(new ShortCutRegister(sc));
				player.registerShortCut(sc);
				break;
			}
			case SKILL:
			{
				final int level = player.getSkillLevel(_id);
				if (level > 0)
				{
					final Shortcut sc = new Shortcut(_slot, _page, _type, _id, level);
					player.sendPacket(new ShortCutRegister(sc));
					player.registerShortCut(sc);
				}
				break;
			}
		}
	}
}
