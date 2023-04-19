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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.l2jmobius.gameserver.instancemanager.CastleManorManager;
import org.l2jmobius.gameserver.model.CropProcure;
import org.l2jmobius.gameserver.model.Seed;
import org.l2jmobius.gameserver.network.ServerPackets;

/**
 * @author l3x
 */
public class ExShowCropSetting extends ServerPacket
{
	private final int _manorId;
	private final Set<Seed> _seeds;
	private final Map<Integer, CropProcure> _current = new HashMap<>();
	private final Map<Integer, CropProcure> _next = new HashMap<>();
	
	public ExShowCropSetting(int manorId)
	{
		final CastleManorManager manor = CastleManorManager.getInstance();
		_manorId = manorId;
		_seeds = manor.getSeedsForCastle(_manorId);
		for (Seed s : _seeds)
		{
			// Current period
			CropProcure cp = manor.getCropProcure(manorId, s.getCropId(), false);
			if (cp != null)
			{
				_current.put(s.getCropId(), cp);
			}
			// Next period
			cp = manor.getCropProcure(manorId, s.getCropId(), true);
			if (cp != null)
			{
				_next.put(s.getCropId(), cp);
			}
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_SHOW_CROP_SETTING.writeId(this);
		writeInt(_manorId); // manor id
		writeInt(_seeds.size()); // size
		CropProcure cp;
		for (Seed s : _seeds)
		{
			writeInt(s.getCropId()); // crop id
			writeInt(s.getLevel()); // seed level
			writeByte(1);
			writeInt(s.getReward(1)); // reward 1 id
			writeByte(1);
			writeInt(s.getReward(2)); // reward 2 id
			writeInt(s.getCropLimit()); // next sale limit
			writeInt(0); // ???
			writeInt(s.getCropMinPrice()); // min crop price
			writeInt(s.getCropMaxPrice()); // max crop price
			// Current period
			if (_current.containsKey(s.getCropId()))
			{
				cp = _current.get(s.getCropId());
				writeInt(cp.getStartAmount()); // buy
				writeInt(cp.getPrice()); // price
				writeByte(cp.getReward()); // reward
			}
			else
			{
				writeInt(0);
				writeInt(0);
				writeByte(0);
			}
			// Next period
			if (_next.containsKey(s.getCropId()))
			{
				cp = _next.get(s.getCropId());
				writeInt(cp.getStartAmount()); // buy
				writeInt(cp.getPrice()); // price
				writeByte(cp.getReward()); // reward
			}
			else
			{
				writeInt(0);
				writeInt(0);
				writeByte(0);
			}
		}
		_next.clear();
		_current.clear();
	}
}