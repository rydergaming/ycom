package ycom.ai;

import java.awt.Point;

import ycom.game.Game;
import ycom.game.Handler;
import ycom.game.Soldier;

public class MiniMax {
	
	
	static Handler oldHandler = null;
	static Game oldGame = null;
	
	
	public static Object minimaxStep(Game game, Handler handler, Ai ai, Soldier soldier, int depth) {
		int max = Integer.MIN_VALUE;
		Object returnOp = null;
		int v;
		while(!handler.isHasSelected())	{
			System.out.println("Waiting for handler to have active soldier");
		}
		for(Object op: AllOperators.getOperators(soldier)) {
			
			//resetState(game, handler);
			//System.out.println("All operators for soldier: " + soldier + " are: " + AllOperators.getOperators(soldier));
			v = -1000;
			String classType = op.getClass().toString().split("@")[0];
			System.out.println("Current soldier: " + soldier);
			System.out.println("Current Operator: " + op);
			
			switch (classType) {
			
			case "class java.awt.Point":
				
				MoveOperator.moveSoldier(game, soldier, (Point)op);	
				soldier.tick();
				int melyseg = depth -1;
				v = minimaxValue(game, handler, ai, handler.getNextSoldier(soldier), melyseg);
				if (v >= max) {
					max = v;
					returnOp = op;
				}
				
				break;
			
			case "class ycom.game.Soldier":
				
				break;
				
			case "class java.lang.Boolean":
				
				break;
			
			
			}
			
			//handler.tick();
		}

		return returnOp;
	}
	


	private static int minimaxValue(Game game, Handler handler, Ai ai, Soldier soldier, int depth) {
	
		int retValue = 0;

		if (handler.getTeamMembers(ai.getEnemyTeam()).size() == 0 || depth == 0 ) {
			System.out.println("Depth is 0 or stuff");
			return Heuristics.bgHeur(ai, handler, game.battleGround);
		}

			retValue = Integer.MIN_VALUE + 10;
			for(Object op: AllOperators.getOperators(soldier)) {
				
				switch (op.getClass().toString()) {
				
				case "java.awt.Point":
					
					MoveOperator.moveSoldier(game, soldier, (Point)op);	
					
					soldier.setCompletedTurn(true);
					soldier.tick();
					retValue = minimaxValue(game, handler, ai, handler.getNextSoldier(soldier), depth -1);
					
					break;
				
				case "ycom.game.Soldier":
					
					break;
					
				case "java.lang.Boolean":
					
					break;
				
				
				}
				//handler.tick();
			//}
			
			
			
		}
		
		
		return retValue;
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
