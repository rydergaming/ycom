package ycom.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Cover extends GameObject{

	public Cover(int mX, int mY, Image sprite, ID id, Game game) {
		super(mX, mY, sprite, id, game);
		this.x = mX * 40 + 200;
		this.y = mY * 40 + 12;
	}

	@Override
	public void tick() {
		//System.out.println("ID:" +mX + " " +mY + "\tCoords:" +x + " " + y);
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 41, 41);
		
	}

	@Override
	public Cover clone() {
		Cover clone = new Cover(this.mX, this.mY, this.sprite, this.id, this.game);
		
		return clone;
	}
}
