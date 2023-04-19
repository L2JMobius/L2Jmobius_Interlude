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
package handlers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.handler.ActionHandler;
import org.l2jmobius.gameserver.handler.ActionShiftHandler;
import org.l2jmobius.gameserver.handler.AdminCommandHandler;
import org.l2jmobius.gameserver.handler.BypassHandler;
import org.l2jmobius.gameserver.handler.ChatHandler;
import org.l2jmobius.gameserver.handler.CommunityBoardHandler;
import org.l2jmobius.gameserver.handler.IHandler;
import org.l2jmobius.gameserver.handler.ItemHandler;
import org.l2jmobius.gameserver.handler.PunishmentHandler;
import org.l2jmobius.gameserver.handler.TargetHandler;
import org.l2jmobius.gameserver.handler.UserCommandHandler;
import org.l2jmobius.gameserver.handler.VoicedCommandHandler;

import handlers.actionhandlers.ArtefactAction;
import handlers.actionhandlers.DecoyAction;
import handlers.actionhandlers.DoorAction;
import handlers.actionhandlers.ItemAction;
import handlers.actionhandlers.NpcAction;
import handlers.actionhandlers.PetAction;
import handlers.actionhandlers.PlayerAction;
import handlers.actionhandlers.StaticObjectAction;
import handlers.actionhandlers.SummonAction;
import handlers.actionhandlers.TrapAction;
import handlers.actionshifthandlers.DoorActionShift;
import handlers.actionshifthandlers.ItemActionShift;
import handlers.actionshifthandlers.NpcActionShift;
import handlers.actionshifthandlers.PlayerActionShift;
import handlers.actionshifthandlers.StaticObjectActionShift;
import handlers.actionshifthandlers.SummonActionShift;
import handlers.admincommandhandlers.AdminAdmin;
import handlers.admincommandhandlers.AdminAnnouncements;
import handlers.admincommandhandlers.AdminBBS;
import handlers.admincommandhandlers.AdminBuffs;
import handlers.admincommandhandlers.AdminCHSiege;
import handlers.admincommandhandlers.AdminCamera;
import handlers.admincommandhandlers.AdminChangeAccessLevel;
import handlers.admincommandhandlers.AdminClan;
import handlers.admincommandhandlers.AdminCreateItem;
import handlers.admincommandhandlers.AdminCursedWeapons;
import handlers.admincommandhandlers.AdminDelete;
import handlers.admincommandhandlers.AdminDestroyItems;
import handlers.admincommandhandlers.AdminDisconnect;
import handlers.admincommandhandlers.AdminDoorControl;
import handlers.admincommandhandlers.AdminEditChar;
import handlers.admincommandhandlers.AdminEffects;
import handlers.admincommandhandlers.AdminEnchant;
import handlers.admincommandhandlers.AdminEvents;
import handlers.admincommandhandlers.AdminExpSp;
import handlers.admincommandhandlers.AdminFakePlayers;
import handlers.admincommandhandlers.AdminFence;
import handlers.admincommandhandlers.AdminFightCalculator;
import handlers.admincommandhandlers.AdminGamePoints;
import handlers.admincommandhandlers.AdminGeodata;
import handlers.admincommandhandlers.AdminGm;
import handlers.admincommandhandlers.AdminGmChat;
import handlers.admincommandhandlers.AdminGmSpeed;
import handlers.admincommandhandlers.AdminGrandBoss;
import handlers.admincommandhandlers.AdminHeal;
import handlers.admincommandhandlers.AdminHide;
import handlers.admincommandhandlers.AdminHtml;
import handlers.admincommandhandlers.AdminInstance;
import handlers.admincommandhandlers.AdminInstanceZone;
import handlers.admincommandhandlers.AdminInvul;
import handlers.admincommandhandlers.AdminKick;
import handlers.admincommandhandlers.AdminKill;
import handlers.admincommandhandlers.AdminLevel;
import handlers.admincommandhandlers.AdminLogin;
import handlers.admincommandhandlers.AdminMammon;
import handlers.admincommandhandlers.AdminManor;
import handlers.admincommandhandlers.AdminMenu;
import handlers.admincommandhandlers.AdminMessages;
import handlers.admincommandhandlers.AdminMobGroup;
import handlers.admincommandhandlers.AdminOnline;
import handlers.admincommandhandlers.AdminPForge;
import handlers.admincommandhandlers.AdminPathNode;
import handlers.admincommandhandlers.AdminPcCafePoints;
import handlers.admincommandhandlers.AdminPcCondOverride;
import handlers.admincommandhandlers.AdminPetition;
import handlers.admincommandhandlers.AdminPledge;
import handlers.admincommandhandlers.AdminPremium;
import handlers.admincommandhandlers.AdminPunishment;
import handlers.admincommandhandlers.AdminQuest;
import handlers.admincommandhandlers.AdminReload;
import handlers.admincommandhandlers.AdminRepairChar;
import handlers.admincommandhandlers.AdminRes;
import handlers.admincommandhandlers.AdminRide;
import handlers.admincommandhandlers.AdminScan;
import handlers.admincommandhandlers.AdminSearch;
import handlers.admincommandhandlers.AdminServerInfo;
import handlers.admincommandhandlers.AdminShop;
import handlers.admincommandhandlers.AdminShowQuests;
import handlers.admincommandhandlers.AdminShutdown;
import handlers.admincommandhandlers.AdminSiege;
import handlers.admincommandhandlers.AdminSkill;
import handlers.admincommandhandlers.AdminSpawn;
import handlers.admincommandhandlers.AdminSummon;
import handlers.admincommandhandlers.AdminSuperHaste;
import handlers.admincommandhandlers.AdminTarget;
import handlers.admincommandhandlers.AdminTargetSay;
import handlers.admincommandhandlers.AdminTeleport;
import handlers.admincommandhandlers.AdminTest;
import handlers.admincommandhandlers.AdminVitality;
import handlers.admincommandhandlers.AdminZone;
import handlers.admincommandhandlers.AdminZones;
import handlers.bypasshandlers.Augment;
import handlers.bypasshandlers.Buy;
import handlers.bypasshandlers.BuyShadowItem;
import handlers.bypasshandlers.ChatLink;
import handlers.bypasshandlers.ClanWarehouse;
import handlers.bypasshandlers.EnchantSkillList;
import handlers.bypasshandlers.Festival;
import handlers.bypasshandlers.FindPvP;
import handlers.bypasshandlers.Freight;
import handlers.bypasshandlers.Link;
import handlers.bypasshandlers.Loto;
import handlers.bypasshandlers.Multisell;
import handlers.bypasshandlers.NpcViewMod;
import handlers.bypasshandlers.Observation;
import handlers.bypasshandlers.PlayerHelp;
import handlers.bypasshandlers.PrivateWarehouse;
import handlers.bypasshandlers.QuestLink;
import handlers.bypasshandlers.QuestList;
import handlers.bypasshandlers.Rift;
import handlers.bypasshandlers.Sell;
import handlers.bypasshandlers.SkillList;
import handlers.bypasshandlers.SupportBlessing;
import handlers.bypasshandlers.SupportMagic;
import handlers.bypasshandlers.TerritoryStatus;
import handlers.bypasshandlers.TutorialClose;
import handlers.bypasshandlers.VoiceCommand;
import handlers.bypasshandlers.Wear;
import handlers.chathandlers.ChatAlliance;
import handlers.chathandlers.ChatBattlefield;
import handlers.chathandlers.ChatClan;
import handlers.chathandlers.ChatGeneral;
import handlers.chathandlers.ChatHeroVoice;
import handlers.chathandlers.ChatParty;
import handlers.chathandlers.ChatPartyMatchRoom;
import handlers.chathandlers.ChatPartyRoomAll;
import handlers.chathandlers.ChatPartyRoomCommander;
import handlers.chathandlers.ChatPetition;
import handlers.chathandlers.ChatShout;
import handlers.chathandlers.ChatTrade;
import handlers.chathandlers.ChatWhisper;
import handlers.communityboard.ClanBoard;
import handlers.communityboard.DropSearchBoard;
import handlers.communityboard.FavoriteBoard;
import handlers.communityboard.FriendsBoard;
import handlers.communityboard.HomeBoard;
import handlers.communityboard.HomepageBoard;
import handlers.communityboard.MailBoard;
import handlers.communityboard.MemoBoard;
import handlers.communityboard.RegionBoard;
import handlers.itemhandlers.BeastSoulShot;
import handlers.itemhandlers.BeastSpice;
import handlers.itemhandlers.BeastSpiritShot;
import handlers.itemhandlers.BlessedSpiritShot;
import handlers.itemhandlers.Book;
import handlers.itemhandlers.Calculator;
import handlers.itemhandlers.CharmOfCourage;
import handlers.itemhandlers.Elixir;
import handlers.itemhandlers.EnchantScrolls;
import handlers.itemhandlers.ExtractableItems;
import handlers.itemhandlers.FishShots;
import handlers.itemhandlers.Harvester;
import handlers.itemhandlers.ItemSkills;
import handlers.itemhandlers.ItemSkillsTemplate;
import handlers.itemhandlers.Maps;
import handlers.itemhandlers.MercTicket;
import handlers.itemhandlers.PetFood;
import handlers.itemhandlers.Recipes;
import handlers.itemhandlers.RollingDice;
import handlers.itemhandlers.Seed;
import handlers.itemhandlers.SevenSignsRecord;
import handlers.itemhandlers.SoulShots;
import handlers.itemhandlers.SpecialXMas;
import handlers.itemhandlers.SpiritShot;
import handlers.itemhandlers.SummonItems;
import handlers.punishmenthandlers.BanHandler;
import handlers.punishmenthandlers.ChatBanHandler;
import handlers.punishmenthandlers.JailHandler;
import handlers.targethandlers.Area;
import handlers.targethandlers.AreaCorpseMob;
import handlers.targethandlers.AreaFriendly;
import handlers.targethandlers.AreaSummon;
import handlers.targethandlers.Aura;
import handlers.targethandlers.AuraCorpseMob;
import handlers.targethandlers.AuraFriendly;
import handlers.targethandlers.BehindArea;
import handlers.targethandlers.BehindAura;
import handlers.targethandlers.Clan;
import handlers.targethandlers.ClanMember;
import handlers.targethandlers.CommandChannel;
import handlers.targethandlers.CorpseClan;
import handlers.targethandlers.CorpseMob;
import handlers.targethandlers.EnemySummon;
import handlers.targethandlers.FlagPole;
import handlers.targethandlers.FrontArea;
import handlers.targethandlers.FrontAura;
import handlers.targethandlers.Ground;
import handlers.targethandlers.Holy;
import handlers.targethandlers.One;
import handlers.targethandlers.OwnerPet;
import handlers.targethandlers.Party;
import handlers.targethandlers.PartyClan;
import handlers.targethandlers.PartyMember;
import handlers.targethandlers.PartyNotMe;
import handlers.targethandlers.PartyOther;
import handlers.targethandlers.PcBody;
import handlers.targethandlers.Pet;
import handlers.targethandlers.Self;
import handlers.targethandlers.Servitor;
import handlers.targethandlers.Summon;
import handlers.targethandlers.TargetParty;
import handlers.targethandlers.Unlockable;
import handlers.usercommandhandlers.ChannelDelete;
import handlers.usercommandhandlers.ChannelInfo;
import handlers.usercommandhandlers.ChannelLeave;
import handlers.usercommandhandlers.ClanPenalty;
import handlers.usercommandhandlers.ClanWarsList;
import handlers.usercommandhandlers.Dismount;
import handlers.usercommandhandlers.ExperienceGain;
import handlers.usercommandhandlers.Loc;
import handlers.usercommandhandlers.Mount;
import handlers.usercommandhandlers.OlympiadStat;
import handlers.usercommandhandlers.PartyInfo;
import handlers.usercommandhandlers.SiegeStatus;
import handlers.usercommandhandlers.Time;
import handlers.usercommandhandlers.Unstuck;
import handlers.voicedcommandhandlers.AutoPotion;
import handlers.voicedcommandhandlers.Banking;
import handlers.voicedcommandhandlers.ChangePassword;
import handlers.voicedcommandhandlers.ChatAdmin;
import handlers.voicedcommandhandlers.Lang;
import handlers.voicedcommandhandlers.Offline;
import handlers.voicedcommandhandlers.Online;
import handlers.voicedcommandhandlers.Premium;
import handlers.voicedcommandhandlers.Wedding;

/**
 * Master handler.
 * @author UnAfraid
 */
public class MasterHandler
{
	private static final Logger LOGGER = Logger.getLogger(MasterHandler.class.getName());
	
	private static final IHandler<?, ?>[] LOAD_INSTANCES =
	{
		ActionHandler.getInstance(),
		ActionShiftHandler.getInstance(),
		AdminCommandHandler.getInstance(),
		BypassHandler.getInstance(),
		ChatHandler.getInstance(),
		CommunityBoardHandler.getInstance(),
		ItemHandler.getInstance(),
		PunishmentHandler.getInstance(),
		UserCommandHandler.getInstance(),
		VoicedCommandHandler.getInstance(),
		TargetHandler.getInstance(),
	};
	
	private static final Class<?>[][] HANDLERS =
	{
		{
			// Action Handlers
			ArtefactAction.class,
			DecoyAction.class,
			DoorAction.class,
			ItemAction.class,
			NpcAction.class,
			PlayerAction.class,
			PetAction.class,
			StaticObjectAction.class,
			SummonAction.class,
			TrapAction.class,
		},
		{
			// Action Shift Handlers
			DoorActionShift.class,
			ItemActionShift.class,
			NpcActionShift.class,
			PlayerActionShift.class,
			StaticObjectActionShift.class,
			SummonActionShift.class,
		},
		{
			// Admin Command Handlers
			AdminAdmin.class,
			AdminAnnouncements.class,
			AdminBBS.class,
			AdminBuffs.class,
			AdminCamera.class,
			AdminChangeAccessLevel.class,
			AdminCHSiege.class,
			AdminClan.class,
			AdminPcCondOverride.class,
			AdminCreateItem.class,
			AdminCursedWeapons.class,
			AdminDelete.class,
			AdminDestroyItems.class,
			AdminDisconnect.class,
			AdminDoorControl.class,
			AdminEditChar.class,
			AdminEffects.class,
			AdminEnchant.class,
			AdminEvents.class,
			AdminExpSp.class,
			AdminFakePlayers.class,
			AdminFence.class,
			AdminFightCalculator.class,
			AdminGamePoints.class,
			AdminGeodata.class,
			AdminGm.class,
			AdminGmChat.class,
			AdminGmSpeed.class,
			AdminGrandBoss.class,
			AdminHeal.class,
			AdminHide.class,
			AdminHtml.class,
			AdminInstance.class,
			AdminInstanceZone.class,
			AdminInvul.class,
			AdminKick.class,
			AdminKill.class,
			AdminLevel.class,
			AdminLogin.class,
			AdminMammon.class,
			AdminManor.class,
			AdminMenu.class,
			AdminMessages.class,
			AdminMobGroup.class,
			AdminOnline.class,
			AdminPathNode.class,
			AdminPcCafePoints.class,
			AdminPetition.class,
			AdminPForge.class,
			AdminPledge.class,
			AdminZones.class,
			AdminPremium.class,
			AdminPunishment.class,
			AdminQuest.class,
			AdminReload.class,
			AdminRepairChar.class,
			AdminRes.class,
			AdminRide.class,
			AdminScan.class,
			AdminSearch.class,
			AdminServerInfo.class,
			AdminShop.class,
			AdminShowQuests.class,
			AdminShutdown.class,
			AdminSiege.class,
			AdminSkill.class,
			AdminSpawn.class,
			AdminSummon.class,
			AdminSuperHaste.class,
			AdminTarget.class,
			AdminTargetSay.class,
			AdminTeleport.class,
			AdminTest.class,
			AdminVitality.class,
			AdminZone.class,
		},
		{
			// Bypass Handlers
			Augment.class,
			Buy.class,
			BuyShadowItem.class,
			ChatLink.class,
			ClanWarehouse.class,
			EnchantSkillList.class,
			Festival.class,
			FindPvP.class,
			Freight.class,
			Link.class,
			Loto.class,
			Multisell.class,
			NpcViewMod.class,
			Observation.class,
			QuestLink.class,
			PlayerHelp.class,
			PrivateWarehouse.class,
			QuestList.class,
			Rift.class,
			Sell.class,
			SkillList.class,
			SupportBlessing.class,
			SupportMagic.class,
			TerritoryStatus.class,
			TutorialClose.class,
			VoiceCommand.class,
			Wear.class,
		},
		{
			// Chat Handlers
			ChatGeneral.class,
			ChatAlliance.class,
			ChatBattlefield.class,
			ChatClan.class,
			ChatHeroVoice.class,
			ChatParty.class,
			ChatPartyMatchRoom.class,
			ChatPartyRoomAll.class,
			ChatPartyRoomCommander.class,
			ChatPetition.class,
			ChatShout.class,
			ChatWhisper.class,
			ChatTrade.class,
		},
		{
			// Community Board
			ClanBoard.class,
			DropSearchBoard.class,
			FavoriteBoard.class,
			FriendsBoard.class,
			HomeBoard.class,
			HomepageBoard.class,
			MailBoard.class,
			MemoBoard.class,
			RegionBoard.class,
		},
		{
			// Item Handlers
			BeastSoulShot.class,
			BeastSpice.class,
			BeastSpiritShot.class,
			BlessedSpiritShot.class,
			Book.class,
			Calculator.class,
			CharmOfCourage.class,
			Elixir.class,
			EnchantScrolls.class,
			ExtractableItems.class,
			FishShots.class,
			Harvester.class,
			ItemSkills.class,
			ItemSkillsTemplate.class,
			Maps.class,
			MercTicket.class,
			PetFood.class,
			Recipes.class,
			RollingDice.class,
			Seed.class,
			SevenSignsRecord.class,
			SoulShots.class,
			SpecialXMas.class,
			SpiritShot.class,
			SummonItems.class,
		},
		{
			// Punishment Handlers
			BanHandler.class,
			ChatBanHandler.class,
			JailHandler.class,
		},
		{
			// User Command Handlers
			ClanPenalty.class,
			ClanWarsList.class,
			Dismount.class,
			Unstuck.class,
			ExperienceGain.class,
			Loc.class,
			Mount.class,
			PartyInfo.class,
			Time.class,
			OlympiadStat.class,
			ChannelLeave.class,
			ChannelDelete.class,
			ChannelInfo.class,
			SiegeStatus.class,
		},
		{
			// TODO: Add configuration options for this voiced commands:
			// CastleVCmd.class,
			// SetVCmd.class,
			Config.ALLOW_WEDDING ? Wedding.class : null,
			Config.BANKING_SYSTEM_ENABLED ? Banking.class : null,
			Config.CHAT_ADMIN ? ChatAdmin.class : null,
			Config.MULTILANG_ENABLE && Config.MULTILANG_VOICED_ALLOW ? Lang.class : null,
			Config.ALLOW_CHANGE_PASSWORD ? ChangePassword.class : null,
			Config.ENABLE_OFFLINE_COMMAND && (Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) ? Offline.class : null,
			Config.ENABLE_ONLINE_COMMAND ? Online.class : null,
			Config.PREMIUM_SYSTEM_ENABLED ? Premium.class : null,
			Config.AUTO_POTIONS_ENABLED ? AutoPotion.class : null,
		},
		{
			// Target Handlers
			Area.class,
			AreaCorpseMob.class,
			AreaFriendly.class,
			AreaSummon.class,
			Aura.class,
			AuraCorpseMob.class,
			AuraFriendly.class,
			BehindArea.class,
			BehindAura.class,
			Clan.class,
			ClanMember.class,
			CommandChannel.class,
			CorpseClan.class,
			CorpseMob.class,
			EnemySummon.class,
			FlagPole.class,
			FrontArea.class,
			FrontAura.class,
			Ground.class,
			Holy.class,
			One.class,
			OwnerPet.class,
			Party.class,
			PartyClan.class,
			PartyMember.class,
			PartyNotMe.class,
			PartyOther.class,
			PcBody.class,
			Pet.class,
			Self.class,
			Servitor.class,
			Summon.class,
			TargetParty.class,
			Unlockable.class,
		},
	};
	
	public static void main(String[] args)
	{
		LOGGER.log(Level.INFO, "Loading Handlers...");
		
		final Map<IHandler<?, ?>, Method> registerHandlerMethods = new HashMap<>();
		for (IHandler<?, ?> loadInstance : LOAD_INSTANCES)
		{
			registerHandlerMethods.put(loadInstance, null);
			for (Method method : loadInstance.getClass().getMethods())
			{
				if (method.getName().equals("registerHandler") && !method.isBridge())
				{
					registerHandlerMethods.put(loadInstance, method);
				}
			}
		}
		
		registerHandlerMethods.entrySet().stream().filter(e -> e.getValue() == null).forEach(e -> LOGGER.log(Level.WARNING, "Failed loading handlers of: " + e.getKey().getClass().getSimpleName() + " seems registerHandler function does not exist."));
		
		for (Class<?>[] classes : HANDLERS)
		{
			for (Class<?> c : classes)
			{
				if (c == null)
				{
					continue; // Disabled handler
				}
				
				try
				{
					final Object handler = c.getDeclaredConstructor().newInstance();
					for (Entry<IHandler<?, ?>, Method> entry : registerHandlerMethods.entrySet())
					{
						if ((entry.getValue() != null) && entry.getValue().getParameterTypes()[0].isInstance(handler))
						{
							entry.getValue().invoke(entry.getKey(), handler);
						}
					}
				}
				catch (Exception e)
				{
					LOGGER.log(Level.WARNING, "Failed loading handler: " + c.getSimpleName(), e);
				}
			}
		}
		
		for (IHandler<?, ?> loadInstance : LOAD_INSTANCES)
		{
			LOGGER.log(Level.INFO, loadInstance.getClass().getSimpleName() + ": Loaded " + loadInstance.size() + " handlers.");
		}
		
		LOGGER.log(Level.INFO, "Handlers Loaded...");
	}
}