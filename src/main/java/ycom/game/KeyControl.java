package ycom.game;

import java.awt.Point;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javafx.scene.input.KeyCode;
import ycom.ai.HunkerOperator;
import ycom.ai.ShootOperator;
import ycom.main.Actions;
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
		Soldier soldier = handler.getActiveSoldier();
		if (soldier == null)
			return;
		
		if (e.getKeyCode() == SPACE) {
			
			if (soldier.getTarget() != null) {
				Soldier target = soldier.getTarget();
				//Extract this so AI can use it
				if (Pathfinder.getDistance(soldier.getmX(), soldier.getmY(), target.getmX(), target.getmY()) <= soldier.VISIONDISTANCE) {
					ShootOperator.shootSoldier(game, soldier, target);
				}

			}			
		}
		if (e.getKeyCode() == VK_H) {

			HunkerOperator.hunkerSoldier(game, handler, soldier);
			System.out.println(soldier.getName() + " is hunkering.");
			
		}
		
	}


	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
