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

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.clan.ClanMember;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author -Wooden-
 */
public class PledgeShowMemberListUpdate extends ServerPacket
{
	private final int _pledgeType;
	private boolean _hasSponsor;
	private final String _name;
	private final int _level;
	private final int _classId;
	private final int _objectId;
	private final boolean _isOnline;
	private final int _race;
	private final boolean _female;
	
	public PledgeShowMemberListUpdate(Player player)
	{
		_pledgeType = player.getPledgeType();
		if (_pledgeType == Clan.SUBUNIT_ACADEMY)
		{
			_hasSponsor = player.getSponsor() != 0;
		}
		else
		{
			_hasSponsor = false;
		}
		_name = player.getName();
		_level = player.getLevel();
		_classId = player.getClassId().getId();
		_race = player.getRace().ordinal();
		_female = player.getAppearance().isFemale();
		_objectId = player.getObjectId();
		_isOnline = player.isOnline();
	}
	
	public PledgeShowMemberListUpdate(ClanMember member)
	{
		_name = member.getName();
		_level = member.getLevel();
		_classId = member.getClassId();
		_objectId = member.getObjectId();
		_isOnline = member.isOnline();
		_pledgeType = member.getPledgeType();
		_race = member.getRaceOrdinal();
		_female = member.getSex();
		if (_pledgeType == Clan.SUBUNIT_ACADEMY)
		{
			_hasSponsor = member.getSponsor() != 0;
		}
		else
		{
			_hasSponsor = false;
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.PLEDGE_SHOW_MEMBER_LIST_UPDATE.writeId(this);
		writeString(_name);
		writeInt(_level);
		writeInt(_classId);
		writeInt(_female);
		writeInt(_race);
		if (_isOnline)
		{
			writeInt(_objectId);
			writeInt(_pledgeType);
		}
		else
		{
			// when going offline send as 0
			writeInt(0);
			writeInt(0);
		}
		writeInt(_hasSponsor);
	}
}
