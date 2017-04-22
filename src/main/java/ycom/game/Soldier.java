package ycom.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import ycom.ai.Operator;
import ycom.main.Pathfinder;

public class Soldier extends GameObject implements Cloneable{

	public int hp = 5;
	public int team;
	public int number;
	private boolean visible = false;
	private boolean selected = false;
	private boolean completedTurn = false;
	public static final int VISIONDISTANCE = 4;
	private int movesLeft = 1;
	private Soldier target;
	private boolean isTargeted = false;
	private Point moveTarget;
	private ArrayList<Point> path = new ArrayList<Point>();
	private Handler handler;
	private String name;
	private boolean hunkered;
	
	public Soldier(int mX, int mY, Image sprite, ID id, int team, Game game, Handler handler, String name, int number) {
		super(mX, mY, sprite, id, game);
		this.team = team;
		this.handler = handler;
		if (this.team == handler.team)
			setVisible(true);
		this.x = mX * 40 + 200;
		this.y = mY * 40 + 12;
		this.name = name;
		this.number = number;
		setMoveTarget(new Point(mX,mY));

		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() {
		if (movesLeft == 0 && !isCompletedTurn()) {
			setSelected(false);
			setCompletedTurn(true);
			//handler.moveCounter++;
			handler.setHasSelected(false);
			//this.visible = false;
			//System.out.println(handler.getActiveSoldier().team);
		}

		if (this.team == handler.team) {
			setVisible(true);
			
			for (int i = 0 ; i< Game.MAPSIZE;i++)
				for(int j = 0 ; j < Game.MAPSIZE; j++)
					visibleEnemies(i, j);
		}
	}

	@Override
	public void render(Graphics g) {		

		
		if (!isVisible())
			return;

		if (isSelected()){
			g.setColor(Color.yellow);
			g.fillRect(x, y, 41, 41);
			drawVision(g);
			drawStats(g);
			//System.out.println(handler.team + " " + handler.moveCounter);
			
		}
		switch(team) {
			case 1:
				g.setColor(Color.blue);
				break;
			case 2:
				g.setColor(Color.RED);
				break;
		}
		
		g.fillRect(x+5, y+5, 30, 30);
		if (isTargeted) {
			g.setColor(Color.yellow);
			g.drawOval(x, y, 41, 41);
			drawSelectedStats(g);
		}
		
	}

	private void drawVision(Graphics g) {

		for (int i = 0 ; i< Game.MAPSIZE;i++)
			for(int j = 0 ; j < Game.MAPSIZE; j++) {
				visibleEnemies(i, j);
				int total = getDistance(i, j);
				if (total < VISIONDISTANCE) {
					g.setColor(Color.WHITE);
					g.drawRect((i-getmX())* 40 + x, (j-getmY())*40 + y, 41, 41);
				}
				
			}
		
		drawMoveablePlaces(g);
	}
	
	private void drawMoveablePlaces(Graphics g) {
		
		if (team == 1) {
			for (int i = 0; i < Game.MAPSIZE; i++)
				for (int j= 0; j<Game.MAPSIZE; j++) {
					int dist = getDistance(i, j);
					if (dist < VISIONDISTANCE) {
						g.setColor(Color.GREEN);
						if (i > getmX())
							g.drawRect((i-getmX())* 40 + x, (j-getmY())*40 + y, 41, 41);
					}
				}
		}
		else {
			for (int i = 0; i < Game.MAPSIZE; i++)
				for (int j= 0; j<Game.MAPSIZE; j++) {
					int dist = getDistance(i, j);
					if (dist < VISIONDISTANCE) {
						g.setColor(Color.GREEN);
						if (i < getmX())
							g.drawRect((i-getmX())* 40 + x, (j-getmY())*40 + y, 41, 41);
					}
				}
		}
			
		
	}

	public Soldier visibleEnemies(int i, int j) {
		if (game.battleGround[i][j] != null)
			if (game.battleGround[i][j].getId() == ID.Soldier) {
				Soldier tempSoldier = (Soldier)game.battleGround[i][j];

				if (tempSoldier.team == team)
					tempSoldier.setVisible(true);
				else {
					
				
				if (isInVision(tempSoldier.getmX(), tempSoldier.getmY())) {
					tempSoldier.setVisible(true);
					//System.out.println(tempSoldier);
					return tempSoldier;
				}
				
				if (tempSoldier.isVisible())
					return null;
				

				else
					tempSoldier.setVisible(false);
				}
			}
		return null;
	}
	
	private boolean isInVision(int x, int y) {
		if (Pathfinder.getDistanceWhole(getmX(), getmY(), x, y) < VISIONDISTANCE) {
			return true;
		}
		return false;
	}
	
	private void drawStats(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new java.awt.Font("Dialog", 0, 20));
		g.drawString(name, 10, 30);
		g.drawString("HP: " + Integer.toString(hp), 10, 60);
		g.drawString("Moves Left: " + Integer.toString(movesLeft), 10, 90);
		g.drawString("Hunkered? " + hunkered, 10, 120);
		g.drawString("Team: " + team, 10, 150);
		g.drawString("Number: " + number, 10, 180);
	}
	
	private void drawSelectedStats(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new java.awt.Font("Dialog", 0, 20));
		g.drawString(name, 210 + 40 * Game.MAPSIZE, 30);
		g.drawString("HP: " + Integer.toString(hp), 210 + 40 * Game.MAPSIZE, 60);
		g.drawString("Hunkered? " + hunkered, 210 + 40 * Game.MAPSIZE, 90);
		g.drawString("Team: " + team, 210 + 40 * Game.MAPSIZE, 120);
		g.drawString("Number: " + number, 210 + 40 * Game.MAPSIZE, 180);
	}

	public String getName() {
		return name;
	}
	public void dmgHp(int dmg) {
		hp -= dmg;		
	}
	public void decreaseMoves() {
		movesLeft--;
		handler.moveCounter++;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		
	}

	public boolean isCompletedTurn() {
		return completedTurn;
	}

	public void setCompletedTurn(boolean completedTurn) {
		this.completedTurn = completedTurn;
		if (!completedTurn) {
			movesLeft = 1;
		}
		
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Soldier getTarget() {
		return target;
	}

	public void setTarget(Soldier target) {
		this.target = target;
	}

	public boolean isTargeted() {
		return isTargeted;
	}

	public void setTargeted(boolean isTargeted) {
		this.isTargeted = isTargeted;
	}

	public Point getMoveTarget() {
		return moveTarget;
	}

	public void setMoveTarget(Point moveTarget) {
		this.moveTarget = moveTarget;
	}
	
	public int getDistance(int i, int j) {
		int dx = Math.abs(i - getmX());
		int dy = Math.abs(j - getmY());
		return dx + dy;
	}

	public ArrayList<Point> getPath() {
		return path;
	}

	public void setPath(ArrayList<Point> path) {
		this.path = path;
	}

	public void appendPath(Point point) {
		path.add(point);
	}

	public boolean isHunkered() {
		return hunkered;
	}

	/**
	 * Sets the hunkered status of the soldier.
	 * If the value is true, it ends the soldier's turn as well.
	 */
	public void setHunkered(boolean hunkered) {
		this.hunkered = hunkered;
		if (this.hunkered) {
			while(movesLeft-->0){
				handler.moveCounter++;
			}
			this.movesLeft = 0;

		}

	}
	
	@Override
	protected Object clone() {
		Soldier clone = new Soldier(mX, mY, sprite, id, team, game, handler, name, number);
		return clone;
		
	}

	public LinkedList<Soldier> getShootable() {
		
		return handler.getEnemiesInVision(this);
	}
	
}
