package ycom.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.text.Font;
import ycom.main.Pathfinder;

public class Soldier extends GameObject{

	public int hp = 10;
	public int team;
	private boolean visible = false;
	private boolean selected = false;
	private boolean completedTurn = false;
	public static final int VISIONDISTANCE = 5;
	private int movesLeft = 2;
	private LinkedList<Soldier> shootable = new LinkedList<Soldier>();
	private Soldier target;
	private boolean isTargeted = false;
	private Point moveTarget;
	private ArrayList<Point> path = new ArrayList<Point>();
	private Handler handler;
	private String name;
	private boolean hunkered;
	
	public Soldier(int mX, int mY, Image sprite, ID id, int team, Game game, Handler handler, String name) {
		super(mX, mY, sprite, id, game);
		this.team = team;
		this.handler = handler;
		if (this.team == handler.team)
			setVisible(true);
		this.x = mX * 40 + 200;
		this.y = mY * 40 + 12;
		this.name = name;
		setMoveTarget(new Point(mX,mY));

		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() {
		if (movesLeft == 0 && !isCompletedTurn()) {
			setSelected(false);
			setCompletedTurn(true);
			handler.moveCounter++;
			handler.setHasSelected(false);

			if (handler.moveCounter < 4) {
				handler.team = 1;
			}
			if (handler.moveCounter >= 4)
				handler.team = 2;
			
			
			if (handler.moveCounter > 7) {
				handler.moveCounter = 0;
				handler.team = 1;
				handler.endTurn();
			}

			System.out.println(handler.moveCounter + "\t" + handler.team);
			//System.out.println(handler.getActiveSoldier().team);
		}
		
		if (getMoveTarget().x != mX || getMoveTarget().y != mY) {
			if (path.size() != 0) {
				game.battleGround[mX][mY] = null;
				mX = path.get(0).x;
				mY = path.get(0).y;				
				this.x = mX * 40 + 200;
				this.y = mY * 40 + 12;
				game.battleGround[mX][mY] = this;
				path.remove(0);
			}

		}
	}

	@Override
	public void render(Graphics g) {
		if (this.team == handler.team)
			setVisible(true);
		if (!isVisible())
			return;
		if (isSelected()){
			g.setColor(Color.yellow);
			g.fillRect(x, y, 41, 41);
			drawVision(g);
			drawStats(g);
			
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
	private boolean isInVision(int x, int y) {
		if (Pathfinder.getDistance(mX, mY, x, y) < VISIONDISTANCE) {
			return true;
		}
		return false;
	}
	private void drawVision(Graphics g) {

		for (int i = 0 ; i< Game.MAPSIZE;i++)
			for(int j = 0 ; j < Game.MAPSIZE; j++) {
				if (game.battleGround[i][j] != null)
					if (game.battleGround[i][j].getClass().getName() == "ycom.game.Soldier") {
						Soldier tempSoldier = (Soldier)game.battleGround[i][j];

						if (isInVision(tempSoldier.mX, tempSoldier.mY))
							tempSoldier.setVisible(true);
						else
							tempSoldier.setVisible(false);
					}
				int total = getDistance(i, j);
				if (total < VISIONDISTANCE) {
					g.setColor(Color.WHITE);
					g.drawRect((i-mX)* 40 + x, (j-mY)*40 + y, 41, 41);
				}
				
			}
	}
	
	private void drawStats(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new java.awt.Font("Dialog", 0, 20));
		g.drawString(name, 10, 30);
		g.drawString("HP: " + Integer.toString(hp), 10, 60);
		g.drawString("Moves Left: " + Integer.toString(movesLeft), 10, 90);
		g.drawString("Hunkered? " + hunkered, 10, 120);
	}
	
	private void drawSelectedStats(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new java.awt.Font("Dialog", 0, 20));
		g.drawString(name, 210 + 40 * Game.MAPSIZE, 30);
		g.drawString("HP: " + Integer.toString(hp), 210 + 40 * Game.MAPSIZE, 60);
		g.drawString("Hunkered? " + hunkered, 210 + 40 * Game.MAPSIZE, 90);
	}

	public String getName() {
		return name;
	}
	public void dmgHp(int dmg) {
		hp -= dmg;		
	}
	public void decreaseMoves() {
		movesLeft--;
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
			movesLeft = 2;
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
		int dx = Math.abs(i - mX);
		int dy = Math.abs(j - mY);
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

	public void setHunkered(boolean hunkered) {
		this.hunkered = hunkered;
	}
}
