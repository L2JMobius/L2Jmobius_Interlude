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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is made to create packets with any format
 * @author Maktakien
 */
public class AdminForgePacket extends ServerPacket
{
	private final List<Part> _parts = new ArrayList<>();
	
	private static class Part
	{
		public byte b;
		public String str;
		
		public Part(byte bb, String string)
		{
			b = bb;
			str = string;
		}
	}
	
	@Override
	public void write()
	{
		for (Part p : _parts)
		{
			generate(p.b, p.str);
		}
	}
	
	public boolean generate(byte b, String string)
	{
		if ((b == 'C') || (b == 'c'))
		{
			writeByte(Integer.decode(string));
			return true;
		}
		else if ((b == 'D') || (b == 'd'))
		{
			writeInt(Integer.decode(string));
			return true;
		}
		else if ((b == 'H') || (b == 'h'))
		{
			writeShort(Integer.decode(string));
			return true;
		}
		else if ((b == 'F') || (b == 'f'))
		{
			writeDouble(Double.parseDouble(string));
			return true;
		}
		else if ((b == 'S') || (b == 's'))
		{
			writeString(string);
			return true;
		}
		else if ((b == 'B') || (b == 'b') || (b == 'X') || (b == 'x'))
		{
			writeBytes(new BigInteger(string).toByteArray());
			return true;
		}
		else if ((b == 'Q') || (b == 'q'))
		{
			writeLong(Long.decode(string));
			return true;
		}
		return false;
	}
	
	public void addPart(byte b, String string)
	{
		_parts.add(new Part(b, string));
	}
}