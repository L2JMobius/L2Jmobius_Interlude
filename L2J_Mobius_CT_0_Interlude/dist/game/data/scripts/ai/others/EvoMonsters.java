package ai.others;

import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Attackable;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.SocialAction;

import ai.AbstractNpcAI;

/**
 * Angel spawns...when one of the angels in the keys dies, the other angel will spawn.
 */
public class EvoMonsters extends AbstractNpcAI
{
	private static final Map<Integer, Integer> ANGELSPAWNS = new HashMap<>();
	static
	{
		ANGELSPAWNS.put(50001, 50002);
		ANGELSPAWNS.put(50002, 50003);
		ANGELSPAWNS.put(50003, 50004);
	}
	private static final int LAST_MOB_ID = 50004; // ID do último monstro
	
	private EvoMonsters()
	{
		addKillId(ANGELSPAWNS.keySet());
		addKillId(LAST_MOB_ID); // adiciona o último monstro à lista de monstros que devem ser mortos
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (npc.getId() == LAST_MOB_ID)
		{
			// O último monstro foi morto, aplicar efeito ao jogador
			killer.sendPacket(new ExShowScreenMessage(Config.MOB_LEVEL_4_TXT, Config.MOB_LEVEL_TIME_4));
			killer.broadcastPacket(new SocialAction(0, 3));
		}
		else
		{
			// Não é o último monstro, spawnar o próximo
			final Attackable newNpc = (Attackable) addSpawn(ANGELSPAWNS.get(npc.getId()), npc);
			newNpc.setRunning();
			
			// Adiciona uma mensagem de morte para o monstro que acabou de morrer
			switch (npc.getId())
			{
				case 50001:
					newNpc.broadcastPacket(new ExShowScreenMessage(Config.MOB_LEVEL_1_TXT, Config.MOB_LEVEL_TIME_1));
					break;
				case 50002:
					newNpc.broadcastPacket(new ExShowScreenMessage(Config.MOB_LEVEL_2_TXT, Config.MOB_LEVEL_TIME_2));
					break;
				case 50003:
					newNpc.broadcastPacket(new ExShowScreenMessage(Config.MOB_LEVEL_3_TXT, Config.MOB_LEVEL_TIME_3));
					break;
			}
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new EvoMonsters();
	}
}
