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

import org.l2jmobius.Config;
import org.l2jmobius.commons.network.ReadablePacket;
import org.l2jmobius.gameserver.enums.PlayerAction;
import org.l2jmobius.gameserver.handler.AdminCommandHandler;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.events.EventDispatcher;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.impl.creature.player.OnPlayerDlgAnswer;
import org.l2jmobius.gameserver.model.events.returns.TerminateReturn;
import org.l2jmobius.gameserver.model.holders.DoorRequestHolder;
import org.l2jmobius.gameserver.model.holders.SummonRequestHolder;
import org.l2jmobius.gameserver.model.olympiad.Olympiad;
import org.l2jmobius.gameserver.network.Disconnection;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ActionFailed;
import org.l2jmobius.gameserver.network.serverpackets.LeaveWorld;
import org.l2jmobius.gameserver.util.OfflineTradeUtil;

/**
 * @author Dezmond_snz
 */
public class DlgAnswer implements ClientPacket
{
	private int _messageId;
	private int _answer;
	private int _requesterId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_messageId = packet.readInt();
		_answer = packet.readInt();
		_requesterId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (EventDispatcher.getInstance().hasListener(EventType.ON_PLAYER_DLG_ANSWER, player))
		{
			final TerminateReturn term = EventDispatcher.getInstance().notifyEvent(new OnPlayerDlgAnswer(player, _messageId, _answer, _requesterId), player, TerminateReturn.class);
			if ((term != null) && term.terminate())
			{
				return;
			}
		}
		
		if (_messageId == SystemMessageId.S1_3.getId())
		{
			if (player.removeAction(PlayerAction.USER_ENGAGE))
			{
				if (Config.ALLOW_WEDDING)
				{
					player.engageAnswer(_answer);
				}
			}
			else if (player.removeAction(PlayerAction.ADMIN_COMMAND))
			{
				final String cmd = player.getAdminConfirmCmd();
				player.setAdminConfirmCmd(null);
				if (_answer == 0)
				{
					return;
				}
				
				// The 'useConfirm' must be disabled here, as we don't want to repeat that process.
				AdminCommandHandler.getInstance().useAdminCommand(player, cmd, false);
			}
		}
		else if (_messageId == SystemMessageId.DO_YOU_WISH_TO_EXIT_THE_GAME.getId())
		{
			if ((_answer == 0) || !Config.ENABLE_OFFLINE_COMMAND || (!Config.OFFLINE_TRADE_ENABLE && !Config.OFFLINE_CRAFT_ENABLE))
			{
				return;
			}
			
			if (!player.isInStoreMode())
			{
				player.sendPacket(SystemMessageId.PRIVATE_STORE_ALREADY_CLOSED);
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if ((player.getInstanceId() > 0) || player.isInVehicle() || !player.canLogout())
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			// Remove player from boss zone.
			player.removeFromBossZone();
			
			// Unregister from olympiad.
			if (Olympiad.getInstance().isRegistered(player))
			{
				Olympiad.getInstance().unRegisterNoble(player);
			}
			
			if (!OfflineTradeUtil.enteredOfflineMode(player))
			{
				Disconnection.of(client, player).defaultSequence(LeaveWorld.STATIC_PACKET);
			}
		}
		else if ((_messageId == SystemMessageId.C1_IS_MAKING_AN_ATTEMPT_TO_RESURRECT_YOU_IF_YOU_CHOOSE_THIS_PATH_S2_EXPERIENCE_POINTS_WILL_BE_RETURNED_TO_YOU_DO_YOU_WANT_TO_BE_RESURRECTED.getId()) || (_messageId == SystemMessageId.YOUR_CHARM_OF_COURAGE_IS_TRYING_TO_RESURRECT_YOU_WOULD_YOU_LIKE_TO_RESURRECT_NOW.getId()))
		{
			player.reviveAnswer(_answer);
		}
		else if (_messageId == SystemMessageId.C1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT.getId())
		{
			final SummonRequestHolder holder = player.removeScript(SummonRequestHolder.class);
			if ((_answer == 1) && (holder != null) && (holder.getSummoner().getObjectId() == _requesterId))
			{
				player.teleToLocation(holder.getLocation(), true);
			}
		}
		else if (_messageId == SystemMessageId.WOULD_YOU_LIKE_TO_OPEN_THE_GATE.getId())
		{
			final DoorRequestHolder holder = player.removeScript(DoorRequestHolder.class);
			if ((holder != null) && (holder.getDoor() == player.getTarget()) && (_answer == 1))
			{
				holder.getDoor().openMe();
			}
		}
		else if (_messageId == SystemMessageId.WOULD_YOU_LIKE_TO_CLOSE_THE_GATE.getId())
		{
			final DoorRequestHolder holder = player.removeScript(DoorRequestHolder.class);
			if ((holder != null) && (holder.getDoor() == player.getTarget()) && (_answer == 1))
			{
				holder.getDoor().closeMe();
			}
		}
	}
}
