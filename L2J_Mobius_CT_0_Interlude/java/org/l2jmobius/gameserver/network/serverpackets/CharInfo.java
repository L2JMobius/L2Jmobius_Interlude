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

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.instancemanager.CursedWeaponsManager;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.Decoy;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.model.skill.AbnormalVisualEffect;
import org.l2jmobius.gameserver.network.ServerPackets;

public class CharInfo extends ServerPacket
{
	private final Player _player;
	private int _objId;
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	private final int _mAtkSpd;
	private final int _pAtkSpd;
	private final int _runSpd;
	private final int _walkSpd;
	private final int _swimRunSpd;
	private final int _swimWalkSpd;
	private final int _flyRunSpd;
	private final int _flyWalkSpd;
	private final double _moveMultiplier;
	private int _vehicleId = 0;
	private final boolean _gmSeeInvis;
	
	public CharInfo(Player player, boolean gmSeeInvis)
	{
		super(256);
		
		_player = player;
		_objId = player.getObjectId();
		if ((_player.getVehicle() != null) && (_player.getInVehiclePosition() != null))
		{
			_x = _player.getInVehiclePosition().getX();
			_y = _player.getInVehiclePosition().getY();
			_z = _player.getInVehiclePosition().getZ();
			_vehicleId = _player.getVehicle().getObjectId();
		}
		else
		{
			_x = _player.getX();
			_y = _player.getY();
			_z = _player.getZ();
		}
		_heading = _player.getHeading();
		_mAtkSpd = _player.getMAtkSpd();
		_pAtkSpd = (int) _player.getPAtkSpd();
		_moveMultiplier = player.getMovementSpeedMultiplier();
		_runSpd = (int) Math.round(player.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) Math.round(player.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = (int) Math.round(player.getSwimRunSpeed() / _moveMultiplier);
		_swimWalkSpd = (int) Math.round(player.getSwimWalkSpeed() / _moveMultiplier);
		_flyRunSpd = player.isFlying() ? _runSpd : 0;
		_flyWalkSpd = player.isFlying() ? _walkSpd : 0;
		_gmSeeInvis = gmSeeInvis;
	}
	
	public CharInfo(Decoy decoy, boolean gmSeeInvis)
	{
		this(decoy.getActingPlayer(), gmSeeInvis); // init
		_objId = decoy.getObjectId();
		_x = decoy.getX();
		_y = decoy.getY();
		_z = decoy.getZ();
		_heading = decoy.getHeading();
	}
	
	@Override
	public void write()
	{
		ServerPackets.CHAR_INFO.writeId(this);
		writeInt(_x);
		writeInt(_y);
		writeInt(_z);
		writeInt(_vehicleId);
		writeInt(_objId);
		writeString(_player.getAppearance().getVisibleName());
		writeInt(_player.getRace().ordinal());
		writeInt(_player.getAppearance().isFemale());
		writeInt(_player.getBaseClass());
		
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_UNDER));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CLOAK));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
		writeInt(_player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR2));
		// c6 new h's
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeInt(_player.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeInt(_player.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeShort(0);
		writeInt(_player.getPvpFlag());
		writeInt(_player.getKarma());
		writeInt(_mAtkSpd);
		writeInt(_pAtkSpd);
		writeInt(_player.getPvpFlag());
		writeInt(_player.getKarma());
		writeInt(_runSpd);
		writeInt(_walkSpd);
		writeInt(_swimRunSpd);
		writeInt(_swimWalkSpd);
		writeInt(_flyRunSpd);
		writeInt(_flyWalkSpd);
		writeInt(_flyRunSpd);
		writeInt(_flyWalkSpd);
		writeDouble(_moveMultiplier);
		writeDouble(_player.getAttackSpeedMultiplier());
		writeDouble(_player.getCollisionRadius());
		writeDouble(_player.getCollisionHeight());
		writeInt(_player.getAppearance().getHairStyle());
		writeInt(_player.getAppearance().getHairColor());
		writeInt(_player.getAppearance().getFace());
		writeString(_gmSeeInvis ? "Invisible" : _player.getAppearance().getVisibleTitle());
		if (!_player.isCursedWeaponEquipped())
		{
			writeInt(_player.getClanId());
			writeInt(_player.getClanCrestId());
			writeInt(_player.getAllyId());
			writeInt(_player.getAllyCrestId());
		}
		else
		{
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
		}
		// In UserInfo leader rights and siege flags, but here found nothing??
		// Therefore RelationChanged packet with that info is required
		writeInt(0);
		writeByte(!_player.isSitting()); // standing = 1 sitting = 0
		writeByte(_player.isRunning()); // running = 1 walking = 0
		writeByte(_player.isInCombat());
		writeByte(!_player.isInOlympiadMode() && _player.isAlikeDead());
		writeByte(!_gmSeeInvis && _player.isInvisible()); // invisible = 1 visible =0
		writeByte(_player.getMountType().ordinal()); // 1-on Strider, 2-on Wyvern, 3-on Great Wolf, 0-no mount
		writeByte(_player.getPrivateStoreType().getId());
		
		writeShort(_player.getCubics().size());
		for (int cubicId : _player.getCubics().keySet())
		{
			writeShort(cubicId);
		}
		
		writeByte(_player.isInPartyMatchRoom());
		writeInt(_gmSeeInvis ? (_player.getAbnormalVisualEffects() | AbnormalVisualEffect.STEALTH.getMask()) : _player.getAbnormalVisualEffects());
		writeByte(_player.getRecomLeft());
		writeShort(_player.getRecomHave()); // Blue value for name (0 = white, 255 = pure blue)
		writeInt(_player.getClassId().getId());
		writeInt(_player.getMaxCp());
		writeInt((int) _player.getCurrentCp());
		writeByte(_player.isMounted() ? 0 : _player.getEnchantEffect());
		writeByte(_player.getTeam().getId());
		writeInt(_player.getClanCrestLargeId());
		writeByte(_player.isNoble()); // Symbol on char menu ctrl+I
		writeByte(_player.isHero() || (_player.isGM() && Config.GM_HERO_AURA)); // Hero Aura
		
		writeByte(_player.isFishing()); // 1: Fishing Mode (Cant be undone by setting back to 0)
		writeInt(_player.getFishX());
		writeInt(_player.getFishY());
		writeInt(_player.getFishZ());
		
		writeInt(_player.getAppearance().getNameColor());
		writeInt(_heading);
		writeInt(_player.getPledgeClass());
		writeInt(_player.getPledgeType());
		writeInt(_player.getAppearance().getTitleColor());
		writeInt(_player.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_player.getCursedWeaponEquippedId()) : 0);
	}
}
