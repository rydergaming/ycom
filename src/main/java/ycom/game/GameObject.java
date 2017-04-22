package ycom.game;

import java.awt.Graphics;
import java.awt.Image;

public abstract class GameObject {
	
	protected int x, y;
	protected int mX;
	protected int mY;
	protected ID id;
	protected Image sprite;
	protected Game game;

	public GameObject(int mX, int mY, Image sprite, ID id, Game game){
		this.setmX(mX);
		this.setmY(mY);
		this.id = id;
		this.sprite = sprite;
		this.game = game;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public void setId(ID id) {
		this.id = id;
	}
	public ID getId() {
		return id;
	}

	public int getmX() {
		return mX;
	}

	public void setmX(int mX) {
		this.mX = mX;
	}

	public int getmY() {
		return mY;
	}

	public void setmY(int mY) {
		this.mY = mY;
	}
}
