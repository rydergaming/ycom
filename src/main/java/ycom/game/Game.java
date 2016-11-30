package ycom.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ycom.visual.Window;

public class Game extends Canvas implements Runnable{
	
	public static final int WIDTH = 800, HEIGHT = WIDTH / 16 * 9;
	public static final int MAPSIZE = 10;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4354729341392677857L;
	
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	public int team = 1;
	public int[][] startGround;
	public GameObject[][] battleGround = new GameObject[MAPSIZE][MAPSIZE];
	private ArrayList<String> names = new ArrayList<String>(
		Arrays.asList(	"Tom Date", "Jonie Clemens", "Delroy Dean", "Nona Pressley", "Marley Wootton",
						"Bonnie Allan", "Helen Scrivener", "Maddox Lee", "Salina Spear"
				)
		
	);
	
	public Game(){
		new Window(WIDTH, HEIGHT, "YCOM", this);
		Random rnd = new Random();
		handler = new Handler(this, team);
		this.addMouseListener(new MouseControl(this, handler));
		this.addKeyListener(new KeyControl(this, handler));
		
		startGround = new int[][]{
			{0,	0,	0,	0,	0,	-1,	0,	0,	0,	0},
			{0,	0,	0,	0,	0,	-1,	0,	-1,	0,	0},
			{0,	0,	-1,	0,	0,	0,	0,	0,	0,	0},
			{1,	0,	-1,	0,	-1,	0,	0,	0,	0,	2},
			{1,	0,	0,	0,	0,	0,	0,	-1,	-1,	2},
			{1,	-1,	0,	0,	0,	0,	-1,	-1,	0,	2},
			{1,	0,	0,	-1,	0,	0,	0,	0,	0,	2},
			{0,	0,	0,	-1,	0,	-1,	0,	0,	0,	0},
			{0,	0,	0,	0,	0,	0,	-1,	0,	0,	0},
			{0,	0,	0,	-1,	0,	0,	0,	0,	0,	0}
			
		};
		
		for(int i= 0; i< MAPSIZE; i++)
			for (int j=0; j< MAPSIZE; j++) {
				switch (startGround[j][i]) {
				case -1:
					battleGround[i][j] = new Cover(i, j, null, ID.Cover, this);
					handler.addObjectFirst(battleGround[i][j]);
					break;
				case 1:
					battleGround[i][j] = new Soldier(i, j, null, ID.Soldier, startGround[j][i], this, handler, names.get(rnd.nextInt(names.size())));
					handler.addObject(battleGround[i][j]);
					break;
				case 2:
					battleGround[i][j] = new Soldier(i, j, null, ID.Soldier, startGround[j][i], this, handler, names.get(rnd.nextInt(names.size())));
					handler.addObject(battleGround[i][j]);
					break;
				case 0:
					battleGround[i][j] = null;
					break;
				}
			}
		handler.addObjectFirst(new BattleGround(0, 0, null, ID.Battleground, this));
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();		
		running = true;
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			
			if (running)
				render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();	
		
	}
	
	private void tick() {
		handler.tick();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		g.dispose();
		bs.show();
	}

}
