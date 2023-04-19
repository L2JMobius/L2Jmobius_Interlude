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

import org.l2jmobius.gameserver.enums.AcquireSkillType;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * Acquire Skill List server packet implementation.
 */
public class AcquireSkillList extends ServerPacket
{
	private final List<Skill> _skills;
	private final AcquireSkillType _skillType;
	
	/**
	 * Private class containing learning skill information.
	 */
	private static class Skill
	{
		public int id;
		public int nextLevel;
		public int maxLevel;
		public int spCost;
		public int requirements;
		
		public Skill(int pId, int pNextLevel, int pMaxLevel, int pSpCost, int pRequirements)
		{
			id = pId;
			nextLevel = pNextLevel;
			maxLevel = pMaxLevel;
			spCost = pSpCost;
			requirements = pRequirements;
		}
	}
	
	public AcquireSkillList(AcquireSkillType type)
	{
		super(512);
		_skillType = type;
		_skills = new ArrayList<>();
	}
	
	public void addSkill(int id, int nextLevel, int maxLevel, int spCost, int requirements)
	{
		_skills.add(new Skill(id, nextLevel, maxLevel, spCost, requirements));
	}
	
	@Override
	public void write()
	{
		if (_skills.isEmpty())
		{
			return;
		}
		
		ServerPackets.ACQUIRE_SKILL_LIST.writeId(this);
		writeInt(_skillType.ordinal());
		writeInt(_skills.size());
		for (Skill temp : _skills)
		{
			writeInt(temp.id);
			writeInt(temp.nextLevel);
			writeInt(temp.maxLevel);
			writeInt(temp.spCost);
			writeInt(temp.requirements);
		}
	}
}