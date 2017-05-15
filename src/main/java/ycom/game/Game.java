package ycom.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ycom.ai.Ai;
import ycom.visual.Window;

public class Game extends Canvas implements Runnable, Cloneable{
	
	public static final int WIDTH = 800, HEIGHT = ((WIDTH / 16 * 9) * 2)-200;
	public static final int MAPSIZE = 10;
	private static final long serialVersionUID = 4354729341392677857L;
	
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	public int team = 1;
	private int[][] startGround;
	public static String currentGround = "";
	
	//public GameObject[][] battleGround = new GameObject[MAPSIZE][MAPSIZE];
	public ArrayList<String> names = new ArrayList<String>(
		Arrays.asList(	"Tom Date", "Jonie Clemens", "Delroy Dean", "Nona Pressley", "Marley Wootton",
						"Bonnie Allan", "Helen Scrivener", "Maddox Lee", "Salina Spear"
				)
		
	);
	Ai blueTeam;
	Ai redTeam;
	
	public Game(){
		new Window(WIDTH, HEIGHT, "YCOM", this);
		handler = new Handler(team);

		
		Random rnd = new Random();
		
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
		int soldierCount = 0;
		for(int i= 0; i< Game.MAPSIZE; i++)
			for (int j=0; j< Game.MAPSIZE; j++) {
				switch (startGround[j][i]) {
				case -1:
					handler.battleGround[i][j] = new Cover(i, j, null, ID.Cover, this);
					handler.addObjectFirst(handler.battleGround[i][j]);
					break;
				case 1:
					
					handler.battleGround[i][j] = new Soldier(i, j, null, ID.Soldier, startGround[j][i], this, handler, "Blue Member", soldierCount);
					soldierCount++;
					handler.addObject(handler.battleGround[i][j]);
					break;
				case 2:
					
					handler.battleGround[i][j] = new Soldier(i, j, null, ID.Soldier, startGround[j][i], this, handler, "Red Member", soldierCount);
					soldierCount++;
					handler.addObject(handler.battleGround[i][j]);
					break;
				case 0:
					handler.battleGround[i][j] = null;
					break;
				}
			}
		handler.addObjectFirst(new BattleGround(0, 0, null, ID.Battleground, this));

		this.addMouseListener(new MouseControl(this, handler));
		this.addKeyListener(new KeyControl(this, handler));
		blueTeam = new Ai(handler, 1, this);
		redTeam = new Ai(handler, 2, this);
		handler.addAI(blueTeam, redTeam);

		System.out.println(blueTeam);
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
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		drawCurrentBG(g);
		
		handler.render(g);
		g.dispose();
		bs.show();
	}
	
	 private void drawCurrentBG(Graphics g) {
			g.setColor(Color.WHITE);
			g.setFont(new java.awt.Font("Monospaced", 0, 12));
			drawString(g, currentGround, WIDTH/2-175, HEIGHT/2+100);

		
	}
 void drawString(Graphics g, String text, int x, int y) {
	    for (String line : text.split("\n"))
	        g.drawString(line, x, y += g.getFontMetrics().getHeight());
        /*for (String line : text.split("\t"))
            g.drawString(line, x += g.getFontMetrics().getHeight(), y);*/
	}


	@Override
	    public Object clone() throws CloneNotSupportedException {
	        return super.clone();
	    }

}
