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
package handlers.admincommandhandlers;

import org.l2jmobius.gameserver.handler.IAdminCommandHandler;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.util.BuilderUtil;

/**
 * @author
 */
public class AdminRide implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ride_wyvern",
		"admin_ride_strider",
		"admin_unride_wyvern",
		"admin_unride_strider",
		"admin_unride",
	};
	
	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
		final Player player = getRideTarget(activeChar);
		if (player == null)
		{
			return false;
		}
		
		if (command.startsWith("admin_ride"))
		{
			if (player.isMounted() || player.hasSummon())
			{
				BuilderUtil.sendSysMessage(activeChar, "Target already have a summon.");
				return false;
			}
			
			int petRideId;
			if (command.startsWith("admin_ride_wyvern"))
			{
				petRideId = 12621;
			}
			else if (command.startsWith("admin_ride_strider"))
			{
				petRideId = 12526;
			}
			else
			{
				BuilderUtil.sendSysMessage(activeChar, "Command '" + command + "' not recognized");
				return false;
			}
			
			player.mount(petRideId, 0, false);
			return false;
		}
		else if (command.startsWith("admin_unride"))
		{
			player.dismount();
		}
		return true;
	}
	
	private Player getRideTarget(Player activeChar)
	{
		Player player = null;
		if ((activeChar.getTarget() == null) || (activeChar.getTarget().getObjectId() == activeChar.getObjectId()) || !activeChar.getTarget().isPlayer())
		{
			player = activeChar;
		}
		else
		{
			player = (Player) activeChar.getTarget();
		}
		return player;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
