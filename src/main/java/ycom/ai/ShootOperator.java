package ycom.ai;

import java.util.ArrayList;
import java.util.LinkedList;

import ycom.game.BattleGroundOperations;
import ycom.game.Game;
import ycom.game.GameObject;
import ycom.game.Handler;
import ycom.game.ID;
import ycom.game.Soldier;

public class ShootOperator implements Operator{
	
	public static void shootSoldier(GameObject[][] battleGround, Soldier soldier, Soldier target) {
		soldier.decreaseMoves();
		int dmg = 3;
		if (target.isHunkered())
			dmg--;
		if (calculateCover(battleGround, soldier.getmX(), soldier.getmY(), target.getmX(), target.getmY()))
			dmg--;
		target.dmgHp(dmg);
		target.setTargeted(false);
		soldier.setTarget(null);	
		if (target.hp < 1) {
			BattleGroundOperations.removeSoldier(battleGround, target);
		}
	}
	
	private static boolean calculateCover(GameObject[][] battleGround, int sX, int sY, int tX, int tY) {
		int i = 0;
		while( i < 4) {
			GameObject tmp;
			switch (i) {
				case 0:
					if (tY == 0)
						break;
					tmp = battleGround[tX][tY-1];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sY < tY-1)
							return true;
					}
					break;
				case 1:
					if (tX == Game.MAPSIZE-1)
						break;
					tmp = battleGround[tX+1][tY];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sX > tX+1)
							return true;
					}
					break;
				case 2:
					if (tY == Game.MAPSIZE-1)
						break;
					tmp = battleGround[tX][tY+1];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sY > tY+1)
							return true;
					}
					break;
				case 3:
					if (tX == 0)
						break;
					tmp = battleGround[tX-1][tY];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sX < tX-1)
							return true;
					}
					break;
			}
			i++;				
		}
		return false;
	}

	public static LinkedList<Soldier> getAllTargets(Soldier soldier) {
		
		LinkedList<Soldier> shootable = new LinkedList<>();
		
		shootable = soldier.getShootable();
		System.out.println(shootable);
		return shootable;
		
	}

	public static boolean canShoot(Soldier currentSoldier, Soldier soldier) {
		int distance = Math.abs(currentSoldier.getmX() - soldier.getmX()) + Math.abs(currentSoldier.getmY() - soldier.getmY());
		if (currentSoldier.number == soldier.number || currentSoldier.team == soldier.team)
			return false;
		if (distance < soldier.VISIONDISTANCE)
			return true;
		return false;
	}


}
