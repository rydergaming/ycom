package ycom.ai;

import java.util.ArrayList;
import java.util.LinkedList;

import ycom.game.Game;
import ycom.game.GameObject;
import ycom.game.ID;
import ycom.game.Soldier;

public class ShootOperator implements Operator{
	
	public static void shootSoldier(Game game, Soldier soldier, Soldier target) {
		soldier.decreaseMoves();
		int dmg = 3;
		if (target.isHunkered())
			dmg--;
		if (calculateCover(game, soldier.getmX(), soldier.getmY(), target.getmX(), target.getmY()))
			dmg--;
		target.dmgHp(dmg);
		target.setTargeted(false);
		soldier.setTarget(null);	
	}
	
	private static boolean calculateCover(Game game, int sX, int sY, int tX, int tY) {
		int i = 0;
		while( i < 4) {
			GameObject tmp;
			switch (i) {
				case 0:
					if (tY == 0)
						break;
					tmp = game.battleGround[tX][tY-1];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sY < tY-1)
							return true;
					}
					break;
				case 1:
					if (tX == Game.MAPSIZE-1)
						break;
					tmp = game.battleGround[tX+1][tY];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sX > tX+1)
							return true;
					}
					break;
				case 2:
					if (tY == Game.MAPSIZE-1)
						break;
					tmp = game.battleGround[tX][tY+1];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sY > tY+1)
							return true;
					}
					break;
				case 3:
					if (tX == 0)
						break;
					tmp = game.battleGround[tX-1][tY];
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


}
