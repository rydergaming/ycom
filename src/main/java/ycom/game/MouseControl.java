package ycom.game;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ycom.main.Pathfinder;

public class MouseControl implements MouseListener{
	
	Game game;
	Handler handler;
	Soldier activeSoldier;
	
	public MouseControl(Game game, Handler handler) {
		this.game = game;
		this.handler = handler;
		
	}
	
	public void mouseClicked(MouseEvent e) {	
		this.activeSoldier = handler.getActiveSoldier();
		if (activeSoldier == null) {
			System.out.println("No Active Soldier");
			System.exit(-1);
		}
			
		if (e.getButton() == e.BUTTON1) {
			int boardX = e.getX() - 200;
			int boardY = e.getY() - 12;
			Point position = calculateBoard(boardX,boardY);
			if (position == null)
				return;
			
			System.out.print("You clicked at: " + position.x + " " + position.y);
			System.out.println(". Got: " + game.battleGround[position.x][position.y]);
			System.out.println("Active soldier: " + activeSoldier);
			
			if (game.battleGround[position.x][position.y] != null) {
				GameObject obj = game.battleGround[position.x][position.y];
				if (obj.id == ID.Soldier) {
					Soldier target = (Soldier)obj;
					if (target.team != activeSoldier.team && target.isVisible()) {
						Soldier tmpTarget = activeSoldier.getTarget();
						if (tmpTarget != null)
							tmpTarget.setTargeted(false);
						activeSoldier.setTarget(null);
						activeSoldier.setTarget(target);
						target.setTargeted(true);
						System.out.println("Targeted: " + activeSoldier.getTarget());
					}
					
				}
					
			}
		}
		
		if (e.getButton() == e.BUTTON3) {
			
			System.out.println("RE-SPECT! WALK!");
			int boardX = e.getX() - 200;
			int boardY = e.getY() - 12;
			Point position = calculateBoard(boardX,boardY);
			if (position == null)
				return;
			int distance = activeSoldier.getDistance(position.x, position.y);
			if (distance>= activeSoldier.VISIONDISTANCE)
				return;
			GameObject obj = game.battleGround[position.x][position.y];
			if (obj == null) {
				activeSoldier.setMoveTarget(position);
				activeSoldier.setPath(Pathfinder
						.findPath(game, activeSoldier.mX, activeSoldier.mY, activeSoldier.getMoveTarget().x, activeSoldier.getMoveTarget().y));
				activeSoldier.appendPath(activeSoldier.getMoveTarget());
				activeSoldier.decreaseMoves();
				
				
			}

		}
		
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private Point calculateBoard(int boardX, int boardY) {
		if (boardX<0 || boardX > 400)
			return null;
		if (boardY<0 || boardY >400)
			return null;
		Point position = new Point(-1,-1);
		position.x = boardX / 40;
		position.y = boardY / 40;
		
		return position;
		
	}

}
