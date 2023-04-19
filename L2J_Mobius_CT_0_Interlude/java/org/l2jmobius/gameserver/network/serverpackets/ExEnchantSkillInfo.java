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

import org.l2jmobius.gameserver.network.ServerPackets;

public class ExEnchantSkillInfo extends ServerPacket
{
	private final List<Req> _reqs;
	private final int _id;
	private final int _level;
	private final int _spCost;
	private final int _xpCost;
	private final int _rate;
	
	class Req
	{
		public int id;
		public int count;
		public int type;
		public int unk;
		
		Req(int pType, int pId, int pCount, int pUnk)
		{
			id = pId;
			type = pType;
			count = pCount;
			unk = pUnk;
		}
	}
	
	public ExEnchantSkillInfo(int id, int level, int spCost, int xpCost, int rate)
	{
		_reqs = new ArrayList<>();
		_id = id;
		_level = level;
		_spCost = spCost;
		_xpCost = xpCost;
		_rate = rate;
	}
	
	public void addRequirement(int type, int id, int count, int unk)
	{
		_reqs.add(new Req(type, id, count, unk));
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_ENCHANT_SKILL_INFO.writeId(this);
		writeInt(_id);
		writeInt(_level);
		writeInt(_spCost);
		writeLong(_xpCost);
		writeInt(_rate);
		writeInt(_reqs.size());
		for (Req temp : _reqs)
		{
			writeInt(temp.type);
			writeInt(temp.id);
			writeInt(temp.count);
			writeInt(temp.unk);
		}
	}
}
