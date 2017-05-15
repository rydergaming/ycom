package ycom.ai;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.Writer;
import java.util.LinkedList;

import ycom.game.BattleGroundOperations;
import ycom.game.Game;
import ycom.game.GameObject;
import ycom.game.Handler;
import ycom.game.ID;
import ycom.game.Soldier;

public class MiniMax {
	
	
	static Handler oldHandler = null;
	static Game oldGame = null;
	
	/**
	 * Gives an optimal move for the player.
	 * @param game The Game, use to sync and render.
	 * @param handler Used to get the next soldier.
	 * @param ai The player to whom we're giving an optimal move.
	 * @param soldier the currently selected soldier.
	 * @param depth how many turns we need to simulate.
	 * @return the optimal move based on the current guessing.
	 */
	
	public static Object minimaxStep(Game game, GameObject battleGround[][], Handler handler, Ai ai, Soldier soldier, int depth) {
		int max = Integer.MIN_VALUE;
		Object returnOp = null;
		int v;

		int actualDepth = depth * (handler.blueValue + handler.redValue);
		int melyseg = actualDepth -1;
		GameObject[][] bg = BattleGroundOperations.copyBattleGround(battleGround);
		Soldier currentSoldier = BattleGroundOperations.currentSoldier(bg);
		for(Object op: AllOperators.getOperators(currentSoldier)) {
			
			v = -1000;
			String classType = op.getClass().toString().split("@")[0];
			
			Game.currentGround = BattleGroundOperations.bgToString(bg);
			
			GameObject[][] newBG = BattleGroundOperations.copyBattleGround(bg);
			
			for (int i=0; i< Game.MAPSIZE; i++)
				for (int j=0; j< Game.MAPSIZE; j++) {					
					if (newBG[i][j] != null && newBG[i][j].getId() == ID.Soldier) {
						if (ShootOperator.canShoot(currentSoldier, (Soldier)newBG[i][j])) {
							ShootOperator.shootSoldier(newBG, currentSoldier, (Soldier)newBG[i][j]);
							Game.currentGround = BattleGroundOperations.bgToString(newBG);
							game.render();
							v = minimaxValue(game, newBG, handler, ai, BattleGroundOperations.nextSoldier(newBG, currentSoldier.number), melyseg);
							if (v >= max) {
								max = v;
								returnOp = op;
							}
						}
					}
				}
			
			switch (classType) {
			
		
			case "class java.awt.Point":
				newBG = BattleGroundOperations.copyBattleGround(bg);
				if (!canMoveThere(newBG, (Point)op))
					continue;
				MoveOperator.moveSoldier(newBG, currentSoldier, (Point)op);	
				//handler.moveBackSoldier(soldier);
				//handler.syncToBattleground(battleGround);
				Game.currentGround = BattleGroundOperations.bgToString(newBG);
				game.render();
				v = minimaxValue(game, newBG, handler, ai, BattleGroundOperations.nextSoldier(newBG, currentSoldier.number), melyseg);
				if (v >= max) {
					max = v;
					returnOp = op;
				}
				
				break;
			

				
			case "class java.lang.Boolean":
				
				break;
			
			
			}
			
			//handler.tick();
		}

		return returnOp;
	}
	

	private static int minimaxValue(Game game, GameObject battleGround[][], Handler handler, Ai ai, Soldier soldier, int depth) {
	
		int retValue = 0;
		System.out.println("Actual Depth: " + depth);


		GameObject[][] bg = BattleGroundOperations.copyBattleGround(battleGround);
		
		System.out.println("active Soldier: " + soldier + " id: " + soldier.number);
		if (depth == 0 ) {
			System.out.println("Depth is 0 or stuff");
			return Heuristics.bgHeur(ai, handler, bg);
		}

		
			retValue = Integer.MIN_VALUE;
			for(Object op: AllOperators.getOperators(soldier)) {
				String classType = op.getClass().toString().split("@")[0];

				GameObject[][] newBG = BattleGroundOperations.copyBattleGround(bg);
				for (int i=0; i< Game.MAPSIZE; i++)
					for (int j=0; j< Game.MAPSIZE; j++) {
						if (newBG[i][j] != null && newBG[i][j].getId() == ID.Soldier) {
							if (ShootOperator.canShoot(soldier, (Soldier)newBG[i][j])) {
								ShootOperator.shootSoldier(newBG, soldier, (Soldier)newBG[i][j]);
								Game.currentGround = BattleGroundOperations.bgToString(newBG);
								game.render();
								retValue = minimaxValue(game, newBG, handler, ai, BattleGroundOperations.nextSoldier(newBG, soldier.number), depth -1);
							}
						}
					}
				
				switch (classType) {	
				
				case "class ycom.game.Soldier":
					newBG = BattleGroundOperations.copyBattleGround(bg);
					ShootOperator.shootSoldier(newBG, soldier, (Soldier)op);
					Game.currentGround = BattleGroundOperations.bgToString(newBG);
					game.render();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}



					retValue = minimaxValue(game, newBG, handler, ai, BattleGroundOperations.nextSoldier(newBG, BattleGroundOperations.nextSoldier(newBG, soldier.number).number), depth -1);

					
					break;

				
				case "class java.awt.Point":
					//System.out.println("In da move operator with soldier: " + soldier);
					//System.out.println((Point)op);
					if (!canMoveThere(bg, (Point)op))
						continue;
					MoveOperator.moveSoldier(bg, soldier, (Point)op);	
					//handler.moveBackSoldier(soldier);
					//handler.syncToBattleground(battleGround);
					game.render();
					System.out.println(handler.getBattleground());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					retValue = minimaxValue(game, bg, handler, ai, BattleGroundOperations.nextSoldier(bg, BattleGroundOperations.nextSoldier(bg, soldier.number).number), depth -1);
					
					break;
				

					
				case "class java.lang.Boolean":
					
					break;
				
				
				}
				//handler.tick();
			}
			
	
		return retValue;
	}

	
	private static boolean canMoveThere(GameObject battleGround[][], Point op) {
		if (battleGround[op.x][op.y] == null)
			return true;
		return false;
		
	}
	
	private static void resetState(Game game, Handler handler) {
		game  = oldGame;
		handler = oldHandler;
		
	}

	private static void saveState(Game game, Handler handler) throws CloneNotSupportedException {

			oldGame = (Game) game.clone();
			oldHandler = (Handler) handler.clone();

		
	}
}
