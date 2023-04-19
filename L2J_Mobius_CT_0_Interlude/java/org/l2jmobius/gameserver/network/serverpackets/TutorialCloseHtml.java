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

import org.l2jmobius.gameserver.enums.HtmlActionScope;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * TutorialCloseHtml server packet implementation.
 * @author HorridoJoho
 */
public class TutorialCloseHtml extends ServerPacket
{
	public static final TutorialCloseHtml STATIC_PACKET = new TutorialCloseHtml();
	
	private TutorialCloseHtml()
	{
	}
	
	@Override
	public void run(Player player)
	{
		player.clearHtmlActions(HtmlActionScope.TUTORIAL_HTML);
	}
	
	@Override
	public void write()
	{
		ServerPackets.TUTORIAL_CLOSE_HTML.writeId(this);
	}
}
