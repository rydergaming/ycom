package ycom.game;

import java.awt.Point;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javafx.scene.input.KeyCode;
import ycom.main.Pathfinder;

public class KeyControl implements KeyListener{
	
	Handler handler;
	Game game;
	private static final int SPACE = 32;
	private static final int VK_H = 72;
	
	public KeyControl(Game game, Handler handler) {
		this.game = game;
		this.handler = handler;

	}
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == SPACE) {
			Soldier soldier = handler.getActiveSoldier();
			if (soldier.getTarget() != null) {
				Soldier target = soldier.getTarget();
				if (Pathfinder.getDistance(soldier.mX, soldier.mY, target.mX, target.mY) <= soldier.VISIONDISTANCE) {
					soldier.decreaseMoves();
					int dmg = 3;
					if (target.isHunkered())
						dmg--;
					if (calculateCover(soldier.mX, soldier.mY, target.mX, target.mY))
						dmg--;
					target.dmgHp(dmg);
					target.setTargeted(false);
					soldier.setTarget(null);	
				}

			}			
		}
		if (e.getKeyCode() == VK_H) {
			Soldier soldier = handler.getActiveSoldier();
			soldier.setHunkered(true);
			System.out.println(soldier.getName() + " is hunkering.");
		}
		
	}

	private boolean calculateCover(int sX, int sY, int tX, int tY) {
		int i = 0;
		while( i < 4) {
			GameObject tmp;
			switch (i) {
				case 0:
					if (tY == 0)
						break;
					tmp = game.battleGround[tX][tY-1];
					if (tmp != null && tmp.id == ID.Cover) {
						if (sY < tY-1)
							return true;
					}
					break;
				case 1:
					if (tX == Game.MAPSIZE-1)
						break;
					tmp = game.battleGround[tX+1][tY];
					if (tmp != null && tmp.id == ID.Cover) {
						if (sX > tX+1)
							return true;
					}
					break;
				case 2:
					if (tY == Game.MAPSIZE-1)
						break;
					tmp = game.battleGround[tX][tY+1];
					if (tmp != null && tmp.id == ID.Cover) {
						if (sY > tY+1)
							return true;
					}
					break;
				case 3:
					if (tX == 0)
						break;
					tmp = game.battleGround[tX-1][tY];
					if (tmp != null && tmp.id == ID.Cover) {
						if (sX < tX-1)
							return true;
					}
					break;
			}
			i++;				
		}
		return false;
	}
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
