package ycom.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import ycom.game.Game;
import ycom.game.GameObject;
import ycom.game.Soldier;
import ycom.main.Pathfinder;

public class MoveOperator implements Operator{
	
	public static void moveSoldier(GameObject battleGround[][], Soldier activeSoldier, Point position) {
		/*activeSoldier.setMoveTarget(position);
		activeSoldier.setPath(Pathfinder
				.findPath(game, activeSoldier.getmX(), activeSoldier.getmY(), activeSoldier.getMoveTarget().x, activeSoldier.getMoveTarget().y));
		activeSoldier.appendPath(activeSoldier.getMoveTarget());*/
		battleGround[activeSoldier.getmX()][activeSoldier.getmY()] = null;
		activeSoldier.setmX(position.x);
		activeSoldier.setmY(position.y);				
		activeSoldier.setX(activeSoldier.getmX() * 40 + 200);
		activeSoldier.setY(activeSoldier.getmY() * 40 + 12);
		battleGround[activeSoldier.getmX()][activeSoldier.getmY()] = activeSoldier;
		activeSoldier.decreaseMoves();
		//activeSoldier.tick();
	}

	public static LinkedList<Point> getAllMoves(Soldier soldier) {
		LinkedList<Point> possibleMoves = new LinkedList<>();
		
		int x = soldier.getmX();
		int y = soldier.getmY();
		int ms = Game.MAPSIZE;
		
		switch (soldier.team) {
		
		case 1:

			if (x + 1 < ms)
			possibleMoves.add(new Point(x +1, y));
			if (x + 2 < ms)
			possibleMoves.add(new Point(x +2, y));
			if (x + 3 < ms)
			possibleMoves.add(new Point(x +3, y));
			
			if (x + 1 < ms && y >= 0)
			possibleMoves.add(new Point(x +1, y-1));
			if (x + 2 < ms && y >= 0)
			possibleMoves.add(new Point(x +2, y-1));
			
			if (x + 1 < ms && y + 1 < ms)
			possibleMoves.add(new Point(x +1, y+1));
			if (x + 2 < ms && y + 1 < ms)
			possibleMoves.add(new Point(x +2, y+1));
			
			if (x + 1 < ms && y + 2 < ms)
			possibleMoves.add(new Point(x +1, y+2));
			
			if (x + 1 < ms && y >= 0)
			possibleMoves.add(new Point(x +1, y-2));
			
			break;
			
		case 2:
			
			if (x - 1 >= 0)
			possibleMoves.add(new Point(x -1, y));
			if (x - 2 >= 0)
			possibleMoves.add(new Point(x -2, y));
			if (x - 3 >= 0)
			possibleMoves.add(new Point(x -3, y));
			
			if (x - 1 >= 0 && y >= 0)
			possibleMoves.add(new Point(x -1, y-1));
			if (x - 2 >= 0 && y >= 0)
			possibleMoves.add(new Point(x -2, y-1));
			
			if (x - 1 >= 0 && y + 1 < ms)
			possibleMoves.add(new Point(x -1, y+1));
			if (x - 2 >= 0 && y + 1 < ms)
			possibleMoves.add(new Point(x -2, y+1));
			
			if (x - 1 >= 0 && y + 2 < ms)
			possibleMoves.add(new Point(x -1, y+2));
			
			if (x - 1 >= 0 && y >= 0)
			possibleMoves.add(new Point(x -1, y-2));
			
			break;
			
		}

		
		return possibleMoves;
	}

}
