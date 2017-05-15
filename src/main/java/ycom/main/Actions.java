package ycom.main;

import java.awt.Point;

import ycom.game.Game;
import ycom.game.GameObject;
import ycom.game.Handler;
import ycom.game.ID;
import ycom.game.Soldier;

/**
 * Class for actions
 * 
 * @author Ryder
 *
 */
public class Actions {

	public static void moveSoldier(Handler handler, Soldier activeSoldier, Point position) {
		activeSoldier.setMoveTarget(position);
		activeSoldier.setPath(Pathfinder
				.findPath(handler, activeSoldier.getmX(), activeSoldier.getmY(), activeSoldier.getMoveTarget().x, activeSoldier.getMoveTarget().y));
		activeSoldier.appendPath(activeSoldier.getMoveTarget());
		activeSoldier.decreaseMoves();
	}
	
	public static void shootSoldier(Handler handler, Soldier soldier, Soldier target) {
		soldier.decreaseMoves();
		int dmg = 3;
		if (target.isHunkered())
			dmg--;
		if (calculateCover(handler, soldier.getmX(), soldier.getmY(), target.getmX(), target.getmY()))
			dmg--;
		target.dmgHp(dmg);
		target.setTargeted(false);
		soldier.setTarget(null);	
	}
	
	private static boolean calculateCover(Handler handler, int sX, int sY, int tX, int tY) {
		int i = 0;
		while( i < 4) {
			GameObject tmp;
			switch (i) {
				case 0:
					if (tY == 0)
						break;
					tmp = handler.battleGround[tX][tY-1];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sY < tY-1)
							return true;
					}
					break;
				case 1:
					if (tX == Game.MAPSIZE-1)
						break;
					tmp = handler.battleGround[tX+1][tY];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sX > tX+1)
							return true;
					}
					break;
				case 2:
					if (tY == Game.MAPSIZE-1)
						break;
					tmp = handler.battleGround[tX][tY+1];
					if (tmp != null && tmp.getId() == ID.Cover) {
						if (sY > tY+1)
							return true;
					}
					break;
				case 3:
					if (tX == 0)
						break;
					tmp = handler.battleGround[tX-1][tY];
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
	
	public static void hunkerSoldier(Game game, Handler handler, Soldier soldier) {
		
		soldier.setHunkered(true);
		handler.setHasSelected(false);
		handler.tick();
	}
}
