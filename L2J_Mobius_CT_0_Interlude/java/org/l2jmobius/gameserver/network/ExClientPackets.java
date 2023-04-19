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
package org.l2jmobius.gameserver.network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.network.clientpackets.*;

/**
 * @author Mobius
 */
public enum ExClientPackets
{
	REQUEST_OUST_FROM_PARTY_ROOM(0x01, RequestOustFromPartyRoom::new, ConnectionState.IN_GAME),
	REQUEST_DISMISS_PARTY_ROOM(0x02, RequestDismissPartyRoom::new, ConnectionState.IN_GAME),
	REQUEST_WITHDRAW_PARTY_ROOM(0x03, RequestWithdrawPartyRoom::new, ConnectionState.IN_GAME),
	REQUEST_CHANGE_PARTY_LEADER(0x04, RequestChangePartyLeader::new, ConnectionState.IN_GAME),
	REQUEST_AUTO_SOUL_SHOT(0x05, RequestAutoSoulShot::new, ConnectionState.IN_GAME),
	REQUEST_EX_ENCHANT_SKILL_INFO(0x06, RequestExEnchantSkillInfo::new, ConnectionState.IN_GAME),
	REQUEST_EX_ENCHANT_SKILL(0x07, RequestExEnchantSkill::new, ConnectionState.IN_GAME),
	REQUEST_MANOR_LIST(0x08, RequestManorList::new, ConnectionState.IN_GAME),
	REQUEST_PROCURE_CROP_LIST(0x09, RequestProcureCropList::new, ConnectionState.IN_GAME),
	REQUEST_SET_SEED(0x0A, RequestSetSeed::new, ConnectionState.IN_GAME),
	REQUEST_SET_CROP(0x0B, RequestSetCrop::new, ConnectionState.IN_GAME),
	REQUEST_WRITE_HERO_WORDS(0x0C, RequestWriteHeroWords::new, ConnectionState.IN_GAME),
	REQUEST_EX_ASK_JOIN_MPCC(0x0D, RequestExAskJoinMPCC::new, ConnectionState.IN_GAME),
	REQUEST_EX_ACCEPT_JOIN_MPCC(0x0E, RequestExAcceptJoinMPCC::new, ConnectionState.IN_GAME),
	REQUEST_EX_OUST_FROM_MPCC(0x0F, RequestExOustFromMPCC::new, ConnectionState.IN_GAME),
	REQUEST_EX_PLEDGE_CREST_LARGE(0x10, RequestExPledgeCrestLarge::new, ConnectionState.IN_GAME),
	REQUEST_EX_SET_PLEDGE_CREST_LARGE(0x11, RequestExSetPledgeCrestLarge::new, ConnectionState.IN_GAME),
	REQUEST_OLYMPIAD_OBSERVER_END(0x12, RequestOlympiadObserverEnd::new, ConnectionState.IN_GAME),
	REQUEST_OLYMPIAD_MATCH_LIST(0x13, RequestOlympiadMatchList::new, ConnectionState.IN_GAME),
	REQUEST_ASK_JOIN_PARTY_ROOM(0x14, RequestAskJoinPartyRoom::new, ConnectionState.IN_GAME),
	ANSWER_JOIN_PARTY_ROOM(0x15, AnswerJoinPartyRoom::new, ConnectionState.IN_GAME),
	REQUEST_LIST_PARTY_MATCHING_WAITING_ROOM(0x16, RequestListPartyMatchingWaitingRoom::new, ConnectionState.IN_GAME),
	REQUEST_EXIT_PARTY_MATCHING_WAITING_ROOM(0x17, RequestExitPartyMatchingWaitingRoom::new, ConnectionState.IN_GAME),
	REQUEST_GET_BOSS_RECORD(0x18, RequestGetBossRecord::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_SET_ACADEMY_MASTER(0x19, RequestPledgeSetAcademyMaster::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_POWER_GRADE_LIST(0x1A, RequestPledgePowerGradeList::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_MEMBER_POWER_INFO(0x1B, RequestPledgeMemberPowerInfo::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_SET_MEMBER_POWER_GRADE(0x1C, RequestPledgeSetMemberPowerGrade::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_MEMBER_INFO(0x1D, RequestPledgeMemberInfo::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_WAR_LIST(0x1E, RequestPledgeWarList::new, ConnectionState.IN_GAME),
	REQUEST_EX_FISH_RANKING(0x1F, RequestExFishRanking::new, ConnectionState.IN_GAME),
	REQUEST_PC_CAFE_COUPON_USE(0x20, RequestPCCafeCouponUse::new, ConnectionState.IN_GAME),
	REQUEST_CURSED_WEAPON_LIST(0x21, RequestCursedWeaponList::new, ConnectionState.IN_GAME),
	REQUEST_CURSED_WEAPON_LOCATION(0x23, RequestCursedWeaponLocation::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_REORGANIZE_MEMBER(0x24, RequestPledgeReorganizeMember::new, ConnectionState.IN_GAME),
	REQUEST_EX_MPCC_SHOW_PARTY_MEMBERS_INFO(0x25, RequestExMPCCShowPartyMembersInfo::new, ConnectionState.IN_GAME),
	REQUEST_DUEL_START(0x27, RequestDuelStart::new, ConnectionState.IN_GAME),
	REQUEST_DUEL_ANSWER_START(0x28, RequestDuelAnswerStart::new, ConnectionState.IN_GAME),
	REQUEST_CONFIRM_TARGET_ITEM(0x29, RequestConfirmTargetItem::new, ConnectionState.IN_GAME),
	REQUEST_CONFIRM_REFINER_ITEM(0x2A, RequestConfirmRefinerItem::new, ConnectionState.IN_GAME),
	REQUEST_CONFIRM_GEM_STONE(0x2B, RequestConfirmGemStone::new, ConnectionState.IN_GAME),
	REQUEST_REFINE(0x2C, RequestRefine::new, ConnectionState.IN_GAME),
	REQUEST_CONFIRM_CANCEL_ITEM(0x2D, RequestConfirmCancelItem::new, ConnectionState.IN_GAME),
	REQUEST_REFINE_CANCEL(0x2E, RequestRefineCancel::new, ConnectionState.IN_GAME),
	REQUEST_EX_MAGIC_SKILL_USE_GROUND(0x2F, RequestExMagicSkillUseGround::new, ConnectionState.IN_GAME),
	REQUEST_DUEL_SURRENDER(0x30, RequestDuelSurrender::new, ConnectionState.IN_GAME);
	
	public static final ExClientPackets[] PACKET_ARRAY;
	static
	{
		final int maxPacketId = Arrays.stream(values()).mapToInt(ExClientPackets::getPacketId).max().orElse(0);
		PACKET_ARRAY = new ExClientPackets[maxPacketId + 1];
		for (ExClientPackets packet : values())
		{
			PACKET_ARRAY[packet.getPacketId()] = packet;
		}
	}
	
	private int _packetId;
	private Supplier<ClientPacket> _packetSupplier;
	private Set<ConnectionState> _connectionStates;
	
	ExClientPackets(int packetId, Supplier<ClientPacket> packetSupplier, ConnectionState... connectionStates)
	{
		// Packet id is an unsigned short.
		if (packetId > 0xFFFF)
		{
			throw new IllegalArgumentException("Packet id must not be bigger than 0xFFFF");
		}
		
		_packetId = packetId;
		_packetSupplier = packetSupplier != null ? packetSupplier : () -> null;
		_connectionStates = new HashSet<>(Arrays.asList(connectionStates));
	}
	
	public int getPacketId()
	{
		return _packetId;
	}
	
	public ClientPacket newPacket()
	{
		final ClientPacket packet = _packetSupplier.get();
		if (Config.DEBUG_EX_CLIENT_PACKETS)
		{
			if (packet != null)
			{
				final String name = packet.getClass().getSimpleName();
				if (!Config.ALT_DEV_EXCLUDED_PACKETS.contains(name))
				{
					PacketLogger.info("[C EX] " + name);
				}
			}
			else if (Config.DEBUG_UNKNOWN_PACKETS)
			{
				PacketLogger.info("[C EX] 0x" + Integer.toHexString(_packetId).toUpperCase());
			}
		}
		return packet;
	}
	
	public Set<ConnectionState> getConnectionStates()
	{
		return _connectionStates;
	}
}
