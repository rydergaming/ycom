package ycom.game;

import java.awt.Graphics;
import java.awt.List;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Handler {
	
	public LinkedList<GameObject> objectList = new LinkedList<GameObject>();
	private LinkedList<Soldier> soldiers = new LinkedList<Soldier>();
	public int team;
	public int moveCounter = 0;
	public int blueValue = 4;
	public int redValue = 4;
	private boolean hasSelected = false;
	Game game;
	
	public Handler(Game game, int team){
		this.game = game;
		this.team = team;
		
	}
	
	public void tick() {
		Soldier currentSoldier = this.getActiveSoldier();
		
		if (currentSoldier != null && currentSoldier.isCompletedTurn()) {
			currentSoldier.setSelected(false);
			this.getNextSoldier(currentSoldier).setSelected(true);
			setHasSelected(true);
		}
		
		for (int i = 0; i < objectList.size(); i++) {
			GameObject obj = objectList.get(i);
			if (obj.id == ID.Soldier) {
				Soldier sold = (Soldier)obj;
				if (sold.hp <= 0) {
					game.battleGround[sold.getmX()][sold.getmY()] = null;
					removeObject(sold);
					continue;
				}
				if (!isHasSelected())
					if (sold.team == team && sold.isCompletedTurn() == false && !isHasSelected()){
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
				for (Soldier sold: getTeamMembers(2)) {
					sold.setVisible(false);
				}
			}
		}
		if (team == 2) {
			if (moveCounter == redValue) {
				moveCounter = 0;
				endTurn();
				team = 1;
				for (Soldier sold: getTeamMembers(1)) {
					sold.setVisible(false);
				}
			}
		}

	}
	
	public void render(Graphics g) {
		for (int i = 0; i < objectList.size(); i++) {
			objectList.get(i).render(g);
		}
		System.gc();
	}

	public void addObject(GameObject object) {
			this.objectList.add(object);
			if (object.id == ID.Soldier) {
				Soldier soldier = (Soldier)object;
				this.soldiers.add(soldier);
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
			return soldiers.listIterator(soldiers.indexOf(soldier)).next();
		}
	}
}
