package ycom.ai;

import ycom.game.Game;
import ycom.game.Handler;
import ycom.game.Soldier;

public class HunkerOperator implements Operator{
	
	public static void hunkerSoldier(Game game, Handler handler, Soldier soldier) {
		
		soldier.setHunkered(true);
		handler.setHasSelected(false);
		handler.tick();
	}

}
