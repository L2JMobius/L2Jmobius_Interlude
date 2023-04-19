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

import org.l2jmobius.gameserver.data.sql.ClanTable;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.siege.Castle;
import org.l2jmobius.gameserver.model.siege.SiegeClan;
import org.l2jmobius.gameserver.model.siege.clanhalls.SiegableHall;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * Populates the Siege Attacker List in the SiegeInfo Window<br>
 * <br>
 * c = ca<br>
 * d = CastleID<br>
 * d = unknown (0)<br>
 * d = unknown (1)<br>
 * d = unknown (0)<br>
 * d = Number of Attackers Clans?<br>
 * d = Number of Attackers Clans<br>
 * { //repeats<br>
 * d = ClanID<br>
 * S = ClanName<br>
 * S = ClanLeaderName<br>
 * d = ClanCrestID<br>
 * d = signed time (seconds)<br>
 * d = AllyID<br>
 * S = AllyName<br>
 * S = AllyLeaderName<br>
 * d = AllyCrestID<br>
 * @author KenM
 */
public class SiegeAttackerList extends ServerPacket
{
	private Castle _castle;
	private SiegableHall _hall;
	
	public SiegeAttackerList(Castle castle)
	{
		_castle = castle;
	}
	
	public SiegeAttackerList(SiegableHall hall)
	{
		_hall = hall;
	}
	
	@Override
	public void write()
	{
		ServerPackets.SIEGE_ATTACKER_LIST.writeId(this);
		if (_castle != null)
		{
			writeInt(_castle.getResidenceId());
			writeInt(0); // 0
			writeInt(1); // 1
			writeInt(0); // 0
			final int size = _castle.getSiege().getAttackerClans().size();
			if (size > 0)
			{
				Clan clan;
				writeInt(size);
				writeInt(size);
				for (SiegeClan siegeclan : _castle.getSiege().getAttackerClans())
				{
					clan = ClanTable.getInstance().getClan(siegeclan.getClanId());
					if (clan == null)
					{
						continue;
					}
					writeInt(clan.getId());
					writeString(clan.getName());
					writeString(clan.getLeaderName());
					writeInt(clan.getCrestId());
					writeInt(0); // signed time (seconds) (not storated by L2J)
					writeInt(clan.getAllyId());
					writeString(clan.getAllyName());
					writeString(""); // AllyLeaderName
					writeInt(clan.getAllyCrestId());
				}
			}
			else
			{
				writeInt(0);
				writeInt(0);
			}
		}
		else
		{
			writeInt(_hall.getId());
			writeInt(0); // 0
			writeInt(1); // 1
			writeInt(0); // 0
			final Collection<SiegeClan> attackers = _hall.getSiege().getAttackerClans();
			final int size = attackers.size();
			if (size > 0)
			{
				writeInt(size);
				writeInt(size);
				for (SiegeClan sClan : attackers)
				{
					final Clan clan = ClanTable.getInstance().getClan(sClan.getClanId());
					if (clan == null)
					{
						continue;
					}
					writeInt(clan.getId());
					writeString(clan.getName());
					writeString(clan.getLeaderName());
					writeInt(clan.getCrestId());
					writeInt(0); // signed time (seconds) (not storated by L2J)
					writeInt(clan.getAllyId());
					writeString(clan.getAllyName());
					writeString(""); // AllyLeaderName
					writeInt(clan.getAllyCrestId());
				}
			}
			else
			{
				writeInt(0);
				writeInt(0);
			}
		}
	}
}
