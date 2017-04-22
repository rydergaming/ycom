package ycom.ai;

import java.util.LinkedList;

import ycom.game.GameObject;
import ycom.game.Handler;
import ycom.game.Soldier;

public class Heuristics {
	
	public static int bgHeur(Ai ai, Handler handler, GameObject[][] battleGround) {
		
		int retValue = 0;
		LinkedList<Soldier> ownTeam = handler.getTeamMembers(ai.getTeam());
		LinkedList<Soldier> enemyTeam = handler.getTeamMembers(ai.getEnemyTeam());;	
	
		
		if (enemyTeam.size()> 0)
			retValue = (ownTeam.size() / enemyTeam.size()) * 100;
		else
			retValue = Integer.MAX_VALUE;
		
		return retValue;
	}

}
