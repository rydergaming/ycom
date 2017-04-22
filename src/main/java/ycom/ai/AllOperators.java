package ycom.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import ycom.game.Soldier;

public class AllOperators implements Operator{

	@SuppressWarnings("null")
	public static LinkedList<Object> getOperators(Soldier soldier) {

		LinkedList<Object> allOperator = new LinkedList<>();
		LinkedList<Point> moveOperator = MoveOperator.getAllMoves(soldier);
		LinkedList<Soldier> shootOperator = ShootOperator.getAllTargets(soldier);
		allOperator.addAll(moveOperator);
		allOperator.addAll(shootOperator);
		allOperator.add(true);
		
		
		return allOperator;
		
	}
	
	

}
