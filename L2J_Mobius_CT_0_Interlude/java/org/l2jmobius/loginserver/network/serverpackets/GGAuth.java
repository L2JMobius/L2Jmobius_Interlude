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
package org.l2jmobius.loginserver.network.serverpackets;

import org.l2jmobius.commons.network.WritablePacket;
import org.l2jmobius.loginserver.network.LoginServerPackets;

/**
 * Format: d d: response
 */
public class GGAuth extends WritablePacket
{
	private final int _response;
	
	public GGAuth(int response)
	{
		_response = response;
	}
	
	@Override
	public void write()
	{
		LoginServerPackets.GG_AUTH.writeId(this);
		writeInt(_response);
		writeInt(0);
		writeInt(0);
		writeInt(0);
		writeInt(0);
	}
}
