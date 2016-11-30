package ycom.visual;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import ycom.game.Game;

public class Window extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8424736621593377093L;
	
	public Window(int width, int height, String title, Game game) {
		JFrame frame = new JFrame(title);
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		game.start();
		
	}
	
	

}
