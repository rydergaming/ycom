package ycom.main;

import java.awt.Point;
import java.util.ArrayList;

import ycom.game.Game;

public class Pathfinder {
	
	static ArrayList<Point> open = new ArrayList<Point>();
	static ArrayList<Point> closed = new ArrayList<Point>();
	static double minCost = 100;
	static Game game;
	
	public static ArrayList<Point> findPath(Game game, int sx, int sy, int tx, int ty) {
		open.clear();
		closed.clear();
		if (getDistance(sx, sy, tx, ty) <= 1) {
			open.add(new Point(tx,ty));
			return open;
		}
		Pathfinder.game = game;
		
		Point point = getNeighbor(new Point(sx, sy), tx, ty);
		open.add(point);
		//System.out.println("We're searching for the path");
		while (point.x != tx && point.y != ty) {

			System.out.println(open);
			closed.add(point);
			point = getNeighbor(point,tx,ty);
			open.add(point);
			

			
			if (minCost == 1) {
				open.add(new Point(tx,ty));
				return open;
			}
		}		
		return open;
		
	}
	private static ArrayList<Point> getNeighbors(int x, int y) {
		ArrayList<Point> neighbors = new ArrayList<Point>();
		for (int i= x - 1; i<=x + 1; i++) {
			for (int j= y-1; i <= y + 1; j++) {
				
			}
		}
		return neighbors;
	}
	public static ArrayList<Point> findPathOld(Game game, int sx, int sy, int tx, int ty) {
		open.clear();
		closed.clear();
		minCost = 100;
		open.add(new Point(sx,sy));
		closed.add(new Point(sx,sy));
		Pathfinder.game = game;
		
		Point point = getNeighbor(new Point(sx, sy), tx, ty);
		open.add(point);
		//System.out.println("We're searching for the path");
		while (point.x != tx && point.y != ty) {

			System.out.println(open);
			point = getNeighbor(point,tx,ty);
			open.add(point);
			closed.add(point);

			
			if (minCost == 1) {
				open.add(new Point(tx,ty));
				return open;
			}
		}			
		return open;
	}
	
	private static Point getNeighbor(Point point, int tx, int ty) {
		Point closestNeighbor = new Point();
		minCost = 100;
		for (int i=point.x-1;i<=point.x+1; i++)
			for (int j = point.y-1; j <= point.y + 1; j++ ) {
				if (i< 0 || i>=Game.MAPSIZE)
					continue;
				if (j < 0 || j >= Game.MAPSIZE)
					continue;
				
				if (game.battleGround[i][j] != null) {
					closed.add(new Point(i,j));
					continue;
				}
				double distance = getDistance(i,j,tx,ty);
				if (distance < minCost) {
					if (game.battleGround[i][j] == null) {
						if (closed.contains(new Point(i,j)))
							continue;
						minCost = distance;
						closestNeighbor.x = i;
						closestNeighbor.y = j;
						if (minCost == 1) {
							return closestNeighbor;
						}
					}

				}

			}
		return closestNeighbor;
		
	}
	
	public static double getDistance(int sx, int sy, int tx, int ty) {
		/*int dx = Math.abs(sx - tx);
		int dy = Math.abs(sy - ty);
		return dx + dy;*/
		int dx = sx - tx;
	    int dy = sy - ty;
	    return Math.sqrt((dx*dx)+(dy*dy));
	}
	
	public static int getDistanceWhole(int sx, int sy, int tx, int ty) {
		int dx = Math.abs(sx - tx);
		int dy = Math.abs(sy - ty);
		return dx + dy;
	}
	
	private static int clamp(int i, int min, int max) {
		if (i<min)
			return min;
		if (i>=max)
			return max-1;
		return i;
	}

}
