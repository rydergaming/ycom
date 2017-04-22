package ycom.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import ycom.game.Game;
import ycom.game.Soldier;
import ycom.main.Pathfinder;

public class MoveOperator implements Operator{
	
	public static void moveSoldier(Game game, Soldier activeSoldier, Point position) {
		/*activeSoldier.setMoveTarget(position);
		activeSoldier.setPath(Pathfinder
				.findPath(game, activeSoldier.getmX(), activeSoldier.getmY(), activeSoldier.getMoveTarget().x, activeSoldier.getMoveTarget().y));
		activeSoldier.appendPath(activeSoldier.getMoveTarget());*/
		game.battleGround[activeSoldier.getmX()][activeSoldier.getmY()] = null;
		activeSoldier.setmX(position.x);
		activeSoldier.setmY(position.y);				
		activeSoldier.setX(activeSoldier.getmX() * 40 + 200);
		activeSoldier.setY(activeSoldier.getmY() * 40 + 12);
		game.battleGround[activeSoldier.getmX()][activeSoldier.getmY()] = activeSoldier;
		activeSoldier.decreaseMoves();
		//activeSoldier.tick();
	}

	public static LinkedList<Point> getAllMoves(Soldier soldier) {
		LinkedList<Point> possibleMoves = new LinkedList<>();
		
		int x = soldier.getmX();
		int y = soldier.getmY();
		
		switch (soldier.team) {
		
		case 1:
			possibleMoves.add(new Point(x +1, y));
			possibleMoves.add(new Point(x +2, y));
			possibleMoves.add(new Point(x +3, y));
			
			possibleMoves.add(new Point(x +1, y-1));
			possibleMoves.add(new Point(x +2, y-1));
			
			possibleMoves.add(new Point(x +1, y+1));
			possibleMoves.add(new Point(x +2, y+1));
			
			possibleMoves.add(new Point(x +1, y+2));
			
			possibleMoves.add(new Point(x +1, y-2));
			
			break;
			
		case 2:
			
			possibleMoves.add(new Point(x -1, y));
			possibleMoves.add(new Point(x -2, y));
			possibleMoves.add(new Point(x -3, y));
			
			possibleMoves.add(new Point(x -1, y-1));
			possibleMoves.add(new Point(x -2, y-1));
			
			possibleMoves.add(new Point(x -1, y+1));
			possibleMoves.add(new Point(x -2, y+1));
			
			possibleMoves.add(new Point(x -1, y+2));
			
			possibleMoves.add(new Point(x -1, y-2));
			
			break;
			
		}

		
		return possibleMoves;
	}

}
