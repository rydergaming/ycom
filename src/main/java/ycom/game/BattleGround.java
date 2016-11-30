package ycom.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import ycom.main.ReadMap;

public class BattleGround extends GameObject{


	public BattleGround(int mX, int mY, Image sprite, ID id, Game game) {
		super(mX, mY, sprite, id, game);
		this.x = mX + 200;
		this.y = mY;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		for(int i=0; i<Game.MAPSIZE*40+40; i+=40) {
			g.drawLine(x, y+i+12, x+400, y+i+12);
			g.drawLine(x+i, y+12, x+i, y+412);
		}

		
		
	}

}
