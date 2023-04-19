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

import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.clan.Clan.SubPledge;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.network.PacketLogger;

/**
 * @author -Wooden-
 */
public class PledgeReceiveSubPledgeCreated extends ServerPacket
{
	private final SubPledge _subPledge;
	private final Clan _clan;
	
	/**
	 * @param subPledge
	 * @param clan
	 */
	public PledgeReceiveSubPledgeCreated(SubPledge subPledge, Clan clan)
	{
		_subPledge = subPledge;
		_clan = clan;
	}
	
	@Override
	public void write()
	{
		ServerPackets.PLEDGE_RECEIVE_SUB_PLEDGE_CREATED.writeId(this);
		writeInt(1);
		writeInt(_subPledge.getId());
		writeString(_subPledge.getName());
		writeString(getLeaderName());
	}
	
	private String getLeaderName()
	{
		final int LeaderId = _subPledge.getLeaderId();
		if ((_subPledge.getId() == Clan.SUBUNIT_ACADEMY) || (LeaderId == 0))
		{
			return "";
		}
		else if (_clan.getClanMember(LeaderId) == null)
		{
			PacketLogger.warning("SubPledgeLeader: " + LeaderId + " is missing from clan: " + _clan.getName() + "[" + _clan.getId() + "]");
			return "";
		}
		else
		{
			return _clan.getClanMember(LeaderId).getName();
		}
	}
}
