package ycom.game;

import java.awt.Graphics;
import java.lang.management.GarbageCollectorMXBean;
import java.util.LinkedList;

public class Handler {
	
	public LinkedList<GameObject> objectList = new LinkedList<GameObject>();
	public int team;
	public int moveCounter = 0;
	private boolean hasSelected = false;
	Game game;
	
	public Handler(Game game, int team){
		this.game = game;
		this.team = team;
	}
	
	public void tick() {
		for (int i = 0; i < objectList.size(); i++) {
			GameObject obj = objectList.get(i);
			if (obj.id == ID.Soldier) {
				Soldier sold = (Soldier)obj;
				if (sold.hp <= 0) {
					game.battleGround[sold.mX][sold.mY] = null;
					removeObject(sold);
					return;
				}
				if (sold.team == team && sold.isCompletedTurn() == false && !isHasSelected()){
					sold.setSelected(true);
					setHasSelected(true);
				}				
			}
			obj.tick();
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
	}
	
	public void addObjectFirst(GameObject object) {
		this.objectList.addFirst(object);
	}
	public void removeObject(GameObject object) {
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
			}
		}
		tick();
	}
}
