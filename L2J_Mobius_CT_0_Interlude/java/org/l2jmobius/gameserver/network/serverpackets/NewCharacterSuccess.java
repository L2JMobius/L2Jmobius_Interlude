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

import org.l2jmobius.gameserver.model.actor.templates.PlayerTemplate;
import org.l2jmobius.gameserver.network.ServerPackets;

public class NewCharacterSuccess extends ServerPacket
{
	private final List<PlayerTemplate> _chars = new ArrayList<>();
	
	public void addChar(PlayerTemplate template)
	{
		_chars.add(template);
	}
	
	@Override
	public void write()
	{
		ServerPackets.CHAR_TEMPLATES.writeId(this);
		writeInt(_chars.size());
		for (PlayerTemplate chr : _chars)
		{
			if (chr == null)
			{
				continue;
			}
			// TODO: Unhardcode these
			writeInt(chr.getRace().ordinal());
			writeInt(chr.getClassId().getId());
			writeInt(0x46);
			writeInt(chr.getBaseSTR());
			writeInt(0x0A);
			writeInt(0x46);
			writeInt(chr.getBaseDEX());
			writeInt(0x0A);
			writeInt(0x46);
			writeInt(chr.getBaseCON());
			writeInt(0x0A);
			writeInt(0x46);
			writeInt(chr.getBaseINT());
			writeInt(0x0A);
			writeInt(0x46);
			writeInt(chr.getBaseWIT());
			writeInt(0x0A);
			writeInt(0x46);
			writeInt(chr.getBaseMEN());
			writeInt(0x0A);
		}
	}
}
