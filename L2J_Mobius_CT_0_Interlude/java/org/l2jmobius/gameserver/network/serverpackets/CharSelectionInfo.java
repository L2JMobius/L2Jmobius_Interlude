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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2jmobius.Config;
import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.data.sql.ClanTable;
import org.l2jmobius.gameserver.model.CharSelectInfoPackage;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.clan.Clan;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.network.Disconnection;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.ServerPackets;

public class CharSelectionInfo extends ServerPacket
{
	private static final Logger LOGGER = Logger.getLogger(CharSelectionInfo.class.getName());
	
	private final String _loginName;
	private final int _sessionId;
	private int _activeId;
	private final List<CharSelectInfoPackage> _characterPackages;
	
	/**
	 * Constructor for CharSelectionInfo.
	 * @param loginName
	 * @param sessionId
	 */
	public CharSelectionInfo(String loginName, int sessionId)
	{
		_sessionId = sessionId;
		_loginName = loginName;
		_characterPackages = loadCharacterSelectInfo(_loginName);
		_activeId = -1;
	}
	
	public CharSelectionInfo(String loginName, int sessionId, int activeId)
	{
		_sessionId = sessionId;
		_loginName = loginName;
		_characterPackages = loadCharacterSelectInfo(_loginName);
		_activeId = activeId;
	}
	
	public List<CharSelectInfoPackage> getCharInfo()
	{
		return _characterPackages;
	}
	
	@Override
	public void write()
	{
		ServerPackets.CHAR_SELECT_INFO.writeId(this);
		final int size = _characterPackages.size();
		writeInt(size); // Created character count
		long lastAccess = 0;
		if (_activeId == -1)
		{
			for (int i = 0; i < size; i++)
			{
				if (lastAccess < _characterPackages.get(i).getLastAccess())
				{
					lastAccess = _characterPackages.get(i).getLastAccess();
					_activeId = i;
				}
			}
		}
		for (int i = 0; i < size; i++)
		{
			final CharSelectInfoPackage charInfoPackage = _characterPackages.get(i);
			writeString(charInfoPackage.getName()); // Character name
			writeInt(charInfoPackage.getObjectId()); // Character ID
			writeString(_loginName); // Account name
			writeInt(_sessionId); // Account ID
			writeInt(charInfoPackage.getClanId()); // Clan ID
			writeInt(0); // Builder level
			writeInt(charInfoPackage.getSex()); // Sex
			writeInt(charInfoPackage.getRace()); // Race
			writeInt(charInfoPackage.getBaseClassId());
			writeInt(1); // GameServerName
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeDouble(charInfoPackage.getCurrentHp());
			writeDouble(charInfoPackage.getCurrentMp());
			writeInt((int) charInfoPackage.getSp());
			writeLong(charInfoPackage.getExp());
			writeInt(charInfoPackage.getLevel());
			writeInt(charInfoPackage.getKarma());
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_UNDER));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_CLOAK));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
			writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HAIR2));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_UNDER));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_REAR));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_NECK));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_FEET));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_CLOAK));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
			writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HAIR2));
			writeInt(charInfoPackage.getHairStyle());
			writeInt(charInfoPackage.getHairColor());
			writeInt(charInfoPackage.getFace());
			writeDouble(charInfoPackage.getMaxHp()); // Maximum HP
			writeDouble(charInfoPackage.getMaxMp()); // Maximum MP
			writeInt(charInfoPackage.getDeleteTimer() > 0 ? (int) ((charInfoPackage.getDeleteTimer() - System.currentTimeMillis()) / 1000) : 0);
			writeInt(charInfoPackage.getClassId());
			writeInt(i == _activeId);
			writeByte(Math.min(charInfoPackage.getEnchantEffect(), 127));
			writeInt(charInfoPackage.getAugmentationId());
		}
	}
	
	private static List<CharSelectInfoPackage> loadCharacterSelectInfo(String loginName)
	{
		CharSelectInfoPackage charInfopackage;
		final List<CharSelectInfoPackage> characterList = new LinkedList<>();
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY createDate"))
		{
			statement.setString(1, loginName);
			try (ResultSet charList = statement.executeQuery())
			{
				while (charList.next()) // fills the package
				{
					charInfopackage = restoreChar(charList);
					if (charInfopackage != null)
					{
						characterList.add(charInfopackage);
						
						// Disconnect offline trader.
						if (Config.OFFLINE_DISCONNECT_SAME_ACCOUNT)
						{
							final Player player = World.getInstance().getPlayer(charInfopackage.getObjectId());
							if (player != null)
							{
								Disconnection.of(player).storeMe().deleteMe();
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Could not restore char info: " + e.getMessage(), e);
		}
		return characterList;
	}
	
	private static void loadCharacterSubclassInfo(CharSelectInfoPackage charInfopackage, int objectId, int activeClassId)
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT exp, sp, level FROM character_subclasses WHERE charId=? AND class_id=? ORDER BY charId"))
		{
			statement.setInt(1, objectId);
			statement.setInt(2, activeClassId);
			try (ResultSet charList = statement.executeQuery())
			{
				if (charList.next())
				{
					charInfopackage.setExp(charList.getLong("exp"));
					charInfopackage.setSp(charList.getInt("sp"));
					charInfopackage.setLevel(charList.getInt("level"));
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Could not restore char subclass info: " + e.getMessage(), e);
		}
	}
	
	private static CharSelectInfoPackage restoreChar(ResultSet chardata) throws Exception
	{
		final int objectId = chardata.getInt("charId");
		final String name = chardata.getString("char_name");
		
		// See if the char must be deleted
		final long deletetime = chardata.getLong("deletetime");
		if ((deletetime > 0) && (System.currentTimeMillis() > deletetime))
		{
			final Clan clan = ClanTable.getInstance().getClan(chardata.getInt("clanid"));
			if (clan != null)
			{
				clan.removeClanMember(objectId, 0);
			}
			GameClient.deleteCharByObjId(objectId);
			return null;
		}
		
		final CharSelectInfoPackage charInfopackage = new CharSelectInfoPackage(objectId, name);
		charInfopackage.setAccessLevel(chardata.getInt("accesslevel"));
		charInfopackage.setLevel(chardata.getInt("level"));
		charInfopackage.setMaxHp(chardata.getInt("maxhp"));
		charInfopackage.setCurrentHp(chardata.getDouble("curhp"));
		charInfopackage.setMaxMp(chardata.getInt("maxmp"));
		charInfopackage.setCurrentMp(chardata.getDouble("curmp"));
		charInfopackage.setKarma(chardata.getInt("karma"));
		charInfopackage.setPkKills(chardata.getInt("pkkills"));
		charInfopackage.setPvPKills(chardata.getInt("pvpkills"));
		charInfopackage.setFace(chardata.getInt("face"));
		charInfopackage.setHairStyle(chardata.getInt("hairstyle"));
		charInfopackage.setHairColor(chardata.getInt("haircolor"));
		charInfopackage.setSex(chardata.getInt("sex"));
		charInfopackage.setExp(chardata.getLong("exp"));
		charInfopackage.setSp(chardata.getLong("sp"));
		charInfopackage.setVitalityPoints(chardata.getInt("vitality_points"));
		charInfopackage.setClanId(chardata.getInt("clanid"));
		charInfopackage.setRace(chardata.getInt("race"));
		final int baseClassId = chardata.getInt("base_class");
		final int activeClassId = chardata.getInt("classid");
		charInfopackage.setX(chardata.getInt("x"));
		charInfopackage.setY(chardata.getInt("y"));
		charInfopackage.setZ(chardata.getInt("z"));
		final int faction = chardata.getInt("faction");
		if (faction == 1)
		{
			charInfopackage.setGood();
		}
		if (faction == 2)
		{
			charInfopackage.setEvil();
		}
		if (Config.MULTILANG_ENABLE)
		{
			String lang = chardata.getString("language");
			if (!Config.MULTILANG_ALLOWED.contains(lang))
			{
				lang = Config.MULTILANG_DEFAULT;
			}
			charInfopackage.setHtmlPrefix("data/lang/" + lang + "/");
		}
		// if is in subclass, load subclass exp, sp, level info
		if (baseClassId != activeClassId)
		{
			loadCharacterSubclassInfo(charInfopackage, objectId, activeClassId);
		}
		charInfopackage.setClassId(activeClassId);
		// Get the augmentation id for equipped weapon
		int weaponObjId = charInfopackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND);
		if (weaponObjId < 1)
		{
			weaponObjId = charInfopackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND);
		}
		if (weaponObjId > 0)
		{
			try (Connection con = DatabaseFactory.getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT augAttributes FROM item_attributes WHERE itemId=?"))
			{
				statement.setInt(1, weaponObjId);
				try (ResultSet result = statement.executeQuery())
				{
					if (result.next())
					{
						final int augment = result.getInt("augAttributes");
						charInfopackage.setAugmentationId(augment == -1 ? 0 : augment);
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.log(Level.WARNING, "Could not restore augmentation info: " + e.getMessage(), e);
			}
		}
		// Check if the base class is set to zero and also doesn't match with the current active class, otherwise send the base class ID. This prevents chars created before base class was introduced from being displayed incorrectly.
		if ((baseClassId == 0) && (activeClassId > 0))
		{
			charInfopackage.setBaseClassId(activeClassId);
		}
		else
		{
			charInfopackage.setBaseClassId(baseClassId);
		}
		charInfopackage.setDeleteTimer(deletetime);
		charInfopackage.setLastAccess(chardata.getLong("lastAccess"));
		return charInfopackage;
	}
}
