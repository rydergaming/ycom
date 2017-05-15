package ycom.game;

import java.awt.Graphics;
import java.awt.List;
import java.io.Serializable;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import ycom.ai.Ai;

public class Handler implements Serializable{
	
	public LinkedList<GameObject> objectList = new LinkedList<GameObject>();
	private LinkedList<Soldier> soldiers = new LinkedList<Soldier>();
	public GameObject battleGround[][] = new GameObject[Game.MAPSIZE][Game.MAPSIZE];

	public int team;
	public int moveCounter = 0;
	public int blueValue = 4;
	public int redValue = 4;
	private boolean hasSelected = false;
	public boolean copying = false;
	private Ai blueAI, redAI;

	
	public Handler(int team){
		this.team = team;


		
	}
	public void addAI(Ai blueAI, Ai redAI) {
		this.blueAI = blueAI;
		this.redAI = redAI;
	}
	
	public void tick() {
		if (team == 1) {
			if (!blueAI.thinking) {
				blueAI.tick();
			}
		}
		if (team == 2) {
			if (!redAI.thinking) {
				redAI.tick();
			}
		}
	
		checkForEndMapRemoval();
		
		for (int i = 0; i < objectList.size(); i++) {
			GameObject obj = objectList.get(i);
			if (obj.id == ID.Soldier) {
				Soldier sold = (Soldier)obj;
				if (sold.hp <= 0) {
					battleGround[sold.getmX()][sold.getmY()] = null;
					removeObject(sold);
				}
				if (sold.team == team && !sold.isCompletedTurn() && !isHasSelected()){
					sold.setSelected(true);
					setHasSelected(true);
				}				
			}
			obj.tick();
		}
		if (team == 1) {
			if (moveCounter == blueValue) {
				moveCounter = 0;
				team = 2;
				for (Soldier sold: getTeamMembers(1)) {
					sold.setVisible(false);
				}
			}
		}
		if (team == 2) {
			if (moveCounter == redValue) {
				moveCounter = 0;				
				team = 1;
				for (Soldier sold: getTeamMembers(2)) {
					sold.setVisible(false);
				}
				endTurn();
			}
		}

	}
	
	public void checkForEndMapRemoval() {

		LinkedList<Soldier> blueTeam = getTeamMembers(1);
		LinkedList<Soldier> redTeam = getTeamMembers(2);
		
		if (redTeam.size() == 0 || blueTeam.size() == 0)
			return;

		for (Soldier sold: blueTeam) {
			if (sold.mX == Game.MAPSIZE) {
				Soldier weekest =  getWeakestSoldier(redTeam);
				int wx = weekest.mX;
				int wy = weekest.mY;
				int sx = sold.mX;
				int sy = sold.mY;
				battleGround[wx][wy] = null;
				battleGround[sx][sy] = null;
				//getPreviousSoldier(sold);
				objectList.remove((GameObject)weekest);
				//soldiers.remove(weekest);
				objectList.remove(sold);
				//soldiers.remove(sold);
				redValue--;
				blueValue--;
				return;

			}
		}
		
		for (Soldier sold: redTeam) {
			if (sold.mX == 0) {
				System.out.println("We have the stuff");
				Soldier weekest =  getWeakestSoldier(blueTeam);
				int wx = weekest.mX;
				int wy = weekest.mY;
				int sx = sold.mX;
				int sy = sold.mY;
				System.out.println("Weakest: " + weekest + "\tx,y: " + weekest.mX + "," + weekest.mY + "\tsold: " + sold + " \t x,y: " + sold.mX + "," + sold.mY);
				battleGround[wx][wy] = null;
				battleGround[sx][sy] = null;
				objectList.remove(weekest);
				//soldiers.remove(weekest);
				objectList.remove(sold);
				redValue--;
				blueValue--;
				return;
			}
		}
		
		
	}


	private Soldier getWeakestSoldier(LinkedList<Soldier> team) {
		int hp = 10;
		Soldier retSoldier = null;
		for (Soldier sold: team) {
			if (sold.hp<= hp)
				retSoldier = sold;
		}
		
		
		return retSoldier;
		
	}

	public void render(Graphics g) {
		while (copying) {};
		
		for (int i = 0; i < objectList.size(); i++) {
			objectList.get(i).render(g);
		}
		System.gc();
	}

	public void addObject(GameObject object) {
			this.objectList.add(object);
			if (object.id == ID.Soldier) {
				Soldier soldier = (Soldier)object;
				soldiers.add(soldier);
			}
	}
	
	public void addObjectFirst(GameObject object) {
		this.objectList.addFirst(object);
	}
	public void removeObject(GameObject object) {
		if (object.id == ID.Soldier) {
			Soldier soldier = (Soldier)object;
			if (soldier.team == 1)
				blueValue--;
			else
				redValue--;
			this.soldiers.remove(soldier);
		}
		
		this.objectList.remove(object);

	}

	public Soldier getActiveSoldier() {
		for (GameObject obj : objectList) {
			if (obj.id == ID.Soldier) {
				Soldier tempSoldier = (Soldier)obj;
				if (tempSoldier.isSelected()) {
					return tempSoldier;					
				}

			}
		}
		return null;
	}

	public boolean isHasSelected() {
		return hasSelected;
	}

	public void setHasSelected(boolean hasSelected) {
		this.hasSelected = hasSelected;
	}
	
	public void endTurn() {
		for (int i=0; i<objectList.size(); i++) {
			GameObject obj = objectList.get(i);
			if (obj.id == ID.Soldier) {
				Soldier soldier = (Soldier)obj;
				soldier.setCompletedTurn(false);
				soldier.setHunkered(false);
				soldier.setVisible(false);
				soldier.setSelected(false);
			}
		}
	}
	
	public LinkedList<Cover> getCovers() {
		LinkedList<Cover> covers = new LinkedList<Cover>();
		for(int i=0; i<objectList.size(); i++) {
			if (objectList.get(i).id == ID.Cover)
				covers.add((Cover)objectList.get(i));
		}
		
		return covers;
		
	}

	public LinkedList<Soldier> getTeamMembers(int team) {
		LinkedList<Soldier> members = new LinkedList<Soldier>();
		for(int i=0; i<objectList.size(); i++) {
			if (objectList.get(i).id == ID.Soldier) {
				Soldier tempSoldier = (Soldier)objectList.get(i);
				if (tempSoldier.team == team)
					members.add(tempSoldier);
			}

		}		
		return members;
		
	}

	public LinkedList<Soldier> getEnemiesInVision(Soldier soldier) {
		LinkedList<Soldier> enemies = new LinkedList<Soldier>();
		for (int i=0;i<Game.MAPSIZE; i++) {
			for (int j=0;j<Game.MAPSIZE; j++) {
				Soldier enemy = soldier.visibleEnemies(i, j);
				if (enemy != null) {
					//System.out.println(enemy);
					enemies.add(enemy);
					
				}
					
			}	
		}
		//System.out.println("enemies in handler: " +enemies);
		return enemies;
	}
	
	 @Override
	public Object clone() throws CloneNotSupportedException {
	        return super.clone();
	    }

	public Soldier getNextSoldier(Soldier soldier) {

		if (soldier.number == soldiers.getLast().number) {
			return soldiers.getFirst();
		}
		else {
			return soldiers.get(soldiers.indexOf(soldier)+1);
		}
	}
	
	public void setNextSoldier(Soldier soldier) {
		soldier.setCompletedTurn(true);
		Soldier nextSoldier = soldiers.listIterator(soldiers.indexOf(soldier)+1).next();
		this.setHasSelected(true);
		nextSoldier.setSelected(true);
	}
	
	public String getBattleground() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i< Game.MAPSIZE; i++) {
			for (int j=0; j< Game.MAPSIZE; j++) {
				if (battleGround[j][i] == null)
					sb.append("0\t");
				else
				switch(battleGround[j][i].getId()) {
				case Cover:
				{
					sb.append("-1\t");
					break;
				}
				case Soldier:
				{
					Soldier tempSoldier = (Soldier)battleGround[j][i];
					if (tempSoldier.team == 1) {
						sb.append("1\t");
					}
					else
						sb.append("2\t");
					break;						
				}
					
				}
			}
			sb.append("\n");
		}
		return sb.toString();

	}
	
	public void syncToBattleground(GameObject bg[][]) {
		if (copying)
			return;
		else
			copying = true;
		soldiers.clear();
		for (int i=0; i<objectList.size(); i++) {
			if (objectList.get(i).id == ID.Soldier) {
				removeObject(objectList.get(i));
			}
		}
		

		for (int i = 0; i<Game.MAPSIZE; i++) {
			for (int j = 0; j<Game.MAPSIZE; j++) {
				if (battleGround[i][j] != null) {
					if (battleGround[i][j].id == ID.Soldier) {
						battleGround[i][j] = null;
					}
				}
				battleGround[i][j] = bg[i][j];
				if (bg[i][j] != null)
					if (bg[i][j].id == ID.Soldier) {
						soldiers.add((Soldier)bg[i][j]);
						objectList.add(bg[i][j]);
					}
			}
		}
		
		Collections.sort(soldiers);
		copying = false;
		
	}
	
	public void moveBackSoldier(Soldier soldier) {
		int xValue = 0;
		
		if (soldier.team == 1 && soldier.mX < Game.MAPSIZE -1)
			return;
		if (soldier.team == 2 && soldier.mX > 0)
			return;
		if (soldier.team == 2)
			xValue = Game.MAPSIZE-1;
		
		
		for (int i = 0;i < Game.MAPSIZE -1; i++) {
			if (battleGround[xValue][i] == null) {
				battleGround[xValue][i] = soldier;
				battleGround[soldier.mX][soldier.mY] = null;

				soldier.mX = xValue;
				soldier.mY = i;
				return;
			}
		}
	}
}
