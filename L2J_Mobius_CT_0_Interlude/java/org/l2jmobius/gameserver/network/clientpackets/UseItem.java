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

import java.util.concurrent.TimeUnit;

import org.l2jmobius.Config;
import org.l2jmobius.commons.network.ReadablePacket;
import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.gameserver.ai.CtrlEvent;
import org.l2jmobius.gameserver.ai.CtrlIntention;
import org.l2jmobius.gameserver.ai.NextAction;
import org.l2jmobius.gameserver.data.xml.EnchantItemGroupsData;
import org.l2jmobius.gameserver.enums.IllegalActionPunishmentType;
import org.l2jmobius.gameserver.enums.PrivateStoreType;
import org.l2jmobius.gameserver.handler.IItemHandler;
import org.l2jmobius.gameserver.handler.ItemHandler;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.effects.EffectType;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.item.EtcItem;
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.model.item.Weapon;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.item.type.WeaponType;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.zone.ZoneId;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.PacketLogger;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ActionFailed;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.ExUseSharedGroupItem;
import org.l2jmobius.gameserver.network.serverpackets.ItemList;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import org.l2jmobius.gameserver.util.Util;

public class UseItem implements ClientPacket
{
	private int _objectId;
	private boolean _ctrlPressed;
	private int _itemId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_objectId = packet.readInt();
		_ctrlPressed = packet.readInt() != 0;
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		// Flood protect UseItem
		if (!client.getFloodProtectors().canUseItem())
		{
			return;
		}
		
		if (player.isInsideZone(ZoneId.JAIL))
		{
			player.sendMessage("You cannot use items while jailed.");
			return;
		}
		
		if (player.getActiveTradeList() != null)
		{
			player.cancelActiveTrade();
		}
		
		if (player.getPrivateStoreType() != PrivateStoreType.NONE)
		{
			player.sendPacket(SystemMessageId.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final Item item = player.getInventory().getItemByObjectId(_objectId);
		if (item == null)
		{
			return;
		}
		
		if (item.getTemplate().getType2() == ItemTemplate.TYPE2_QUEST)
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_USE_QUEST_ITEMS);
			return;
		}
		
		// No UseItem is allowed while the player is in special conditions
		if (player.isStunned() || player.isParalyzed() || player.isSleeping() || player.isAfraid() || player.isAlikeDead())
		{
			return;
		}
		
		_itemId = item.getId();
		
		// Char cannot use item when dead
		if (player.isDead() || !player.getInventory().canManipulateWithItemId(_itemId))
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS);
			sm.addItemName(item);
			player.sendPacket(sm);
			return;
		}
		
		if (!item.isEquipped() && !item.getTemplate().checkCondition(player, player, true))
		{
			return;
		}
		
		if (player.isFishing() && ((_itemId < 6535) || (_itemId > 6540)))
		{
			// You cannot do anything else while fishing
			player.sendPacket(SystemMessageId.YOU_CANNOT_DO_THAT_WHILE_FISHING_3);
			return;
		}
		
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_TELEPORT && (player.getKarma() > 0))
		{
			final SkillHolder[] skills = item.getTemplate().getSkills();
			if (skills != null)
			{
				for (SkillHolder sHolder : skills)
				{
					final Skill skill = sHolder.getSkill();
					if ((skill != null) && skill.hasEffectType(EffectType.TELEPORT))
					{
						return;
					}
				}
			}
		}
		
		// If the item has reuse time and it has not passed.
		// Message from reuse delay must come from item.
		final int reuseDelay = item.getReuseDelay();
		final int sharedReuseGroup = item.getSharedReuseGroup();
		if (reuseDelay > 0)
		{
			final long reuse = player.getItemRemainingReuseTime(item.getObjectId());
			if (reuse > 0)
			{
				reuseData(player, item, reuse);
				sendSharedGroupUpdate(player, sharedReuseGroup, reuse, reuseDelay);
				return;
			}
			
			final long reuseOnGroup = player.getReuseDelayOnGroup(sharedReuseGroup);
			if (reuseOnGroup > 0)
			{
				reuseData(player, item, reuseOnGroup);
				sendSharedGroupUpdate(player, sharedReuseGroup, reuseOnGroup, reuseDelay);
				return;
			}
		}
		
		player.onActionRequest();
		
		if (item.isEquipable())
		{
			// Don't allow to put formal wear while a cursed weapon is equipped.
			if (player.isCursedWeaponEquipped() && (_itemId == 6408))
			{
				return;
			}
			
			switch (item.getTemplate().getBodyPart())
			{
				case ItemTemplate.SLOT_LR_HAND:
				case ItemTemplate.SLOT_L_HAND:
				case ItemTemplate.SLOT_R_HAND:
				{
					// Prevent players to equip weapon while wearing combat flag
					if ((player.getActiveWeaponItem() != null) && (player.getActiveWeaponItem().getId() == 9819))
					{
						player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
						return;
					}
					
					if (player.isMounted())
					{
						player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
						return;
					}
					if (player.isDisarmed())
					{
						player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
						return;
					}
					
					// Don't allow weapon/shield equipment if a cursed weapon is equipped.
					if (player.isCursedWeaponEquipped())
					{
						return;
					}
					
					break;
				}
			}
			
			// Over-enchant protection.
			if (Config.OVER_ENCHANT_PROTECTION && !player.isGM() //
				&& ((item.isWeapon() && (item.getEnchantLevel() > EnchantItemGroupsData.getInstance().getMaxWeaponEnchant())) //
					|| ((item.getTemplate().getType2() == ItemTemplate.TYPE2_ACCESSORY) && (item.getEnchantLevel() > EnchantItemGroupsData.getInstance().getMaxAccessoryEnchant())) //
					|| (item.isArmor() && (item.getTemplate().getType2() != ItemTemplate.TYPE2_ACCESSORY) && (item.getEnchantLevel() > EnchantItemGroupsData.getInstance().getMaxArmorEnchant()))))
			{
				player.getInventory().destroyItem("Over-enchant protection", item, player, null);
				PacketLogger.info("Over-enchanted " + item + " has been removed from " + player);
				if (Config.OVER_ENCHANT_PUNISHMENT != IllegalActionPunishmentType.NONE)
				{
					player.sendMessage("[Server]: You have over-enchanted items!");
					player.sendMessage("[Server]: Respect our server rules.");
					player.sendPacket(new ExShowScreenMessage("You have over-enchanted items!", 6000));
					Util.handleIllegalPlayerAction(player, player.getName() + " has over-enchanted items.", Config.OVER_ENCHANT_PUNISHMENT);
				}
				return;
			}
			
			if (player.isCastingNow() || player.isCastingSimultaneouslyNow())
			{
				// Creating next action class.
				final NextAction nextAction = new NextAction(CtrlEvent.EVT_FINISH_CASTING, CtrlIntention.AI_INTENTION_CAST, () -> player.useEquippableItem(item, true));
				
				// Binding next action to AI.
				player.getAI().setNextAction(nextAction);
			}
			else // Equip or unEquip.
			{
				final long currentTime = System.nanoTime();
				final long attackEndTime = player.getAttackEndTime();
				if (attackEndTime > currentTime)
				{
					ThreadPool.schedule(() -> player.useEquippableItem(item, false), TimeUnit.NANOSECONDS.toMillis(attackEndTime - currentTime));
				}
				else
				{
					player.useEquippableItem(item, true);
				}
			}
		}
		else
		{
			final Weapon weaponItem = player.getActiveWeaponItem();
			if (((weaponItem != null) && (weaponItem.getItemType() == WeaponType.FISHINGROD)) && (((_itemId >= 6519) && (_itemId <= 6527)) || ((_itemId >= 7610) && (_itemId <= 7613)) || ((_itemId >= 7807) && (_itemId <= 7809)) || ((_itemId >= 8484) && (_itemId <= 8486)) || ((_itemId >= 8505) && (_itemId <= 8513))))
			{
				player.getInventory().setPaperdollItem(Inventory.PAPERDOLL_LHAND, item);
				player.broadcastUserInfo();
				// Send a Server->Client packet ItemList to this Player to update left hand equipment.
				player.sendPacket(new ItemList(player, false));
				return;
			}
			
			final EtcItem etcItem = item.getEtcItem();
			final IItemHandler handler = ItemHandler.getInstance().getHandler(etcItem);
			if (handler == null)
			{
				if ((etcItem != null) && (etcItem.getHandlerName() != null))
				{
					PacketLogger.warning("Unmanaged Item handler: " + etcItem.getHandlerName() + " for Item Id: " + _itemId + "!");
				}
				return;
			}
			
			// Item reuse time should be added if the item is successfully used.
			// Skill reuse delay is done at handlers.itemhandlers.ItemSkillsTemplate;
			if (handler.useItem(player, item, _ctrlPressed) && (reuseDelay > 0))
			{
				player.addTimeStampItem(item, reuseDelay);
				sendSharedGroupUpdate(player, sharedReuseGroup, reuseDelay, reuseDelay);
			}
		}
	}
	
	private void reuseData(Player player, Item item, long remainingTime)
	{
		final int hours = (int) (remainingTime / 3600000);
		final int minutes = (int) (remainingTime % 3600000) / 60000;
		final int seconds = (int) ((remainingTime / 1000) % 60);
		final SystemMessage sm;
		if (hours > 0)
		{
			sm = new SystemMessage(SystemMessageId.THERE_ARE_S2_HOUR_S_S3_MINUTE_S_AND_S4_SECOND_S_REMAINING_IN_S1_S_RE_USE_TIME);
			sm.addItemName(item);
			sm.addInt(hours);
			sm.addInt(minutes);
		}
		else if (minutes > 0)
		{
			sm = new SystemMessage(SystemMessageId.THERE_ARE_S2_MINUTE_S_S3_SECOND_S_REMAINING_IN_S1_S_RE_USE_TIME);
			sm.addItemName(item);
			sm.addInt(minutes);
		}
		else
		{
			sm = new SystemMessage(SystemMessageId.THERE_ARE_S2_SECOND_S_REMAINING_IN_S1_S_RE_USE_TIME);
			sm.addItemName(item);
		}
		sm.addInt(seconds);
		player.sendPacket(sm);
	}
	
	private void sendSharedGroupUpdate(Player player, int group, long remaining, int reuse)
	{
		if (group > 0)
		{
			player.sendPacket(new ExUseSharedGroupItem(_itemId, group, remaining, reuse));
		}
	}
}
