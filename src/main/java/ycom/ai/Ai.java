package ycom.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ycom.game.Cover;
import ycom.game.Game;
import ycom.game.GameObject;
import ycom.game.Handler;
import ycom.game.ID;
import ycom.game.Soldier;

public class Ai {

	private Handler handler;
	private int team;
	private int enemyTeam;
	private GameObject[][] battleGround = new GameObject[Game.MAPSIZE][Game.MAPSIZE];
	private LinkedList<Soldier> teamMembers = new LinkedList<Soldier>();
	private Map<Integer, Soldier> enemyMembers = new HashMap<Integer, Soldier>();
	private boolean thinking = false;
	private Game game;
	private List<Point> possibleEnemies;
	private int it;
	
	public Ai(Handler handler, int team, Game game) {
		this.handler = handler;
		this.setTeam(team);
		switch (team) {
		
		case 1:
			this.setEnemyTeam(2);
			break;
		
		case 2:
			this.setEnemyTeam(1);
			break;
		}

		this.game = game;
		this.it = team * 4;
		for (int i = 0; i< 4; i++)
		{
			enemyMembers.put(i + it, null);
		}
		buildMap();		
		buildEnemies();		

	}

	/**
	 * Builds enemies on the battleground.
	 * Removes every enemy, then adds visible enemies, then adds guessed enemies. 
	 */
	private void buildEnemies() {
		for (int i = it; i < it + 4; i++)
			enemyMembers.remove(i);
		clearEnemiesFromBattleground();
		int redValue = handler.redValue - enemyMembers.size();
		int blueValue = handler.blueValue - enemyMembers.size(); 
		if (getTeam() == 1)
			guessEnemyPositions(redValue);
		else
			guessEnemyPositions(blueValue);

		System.out.println("possible enemies: " + possibleEnemies);
		
		if (getTeam() == 1) {
			System.out.println(enemyMembers);
			for (int i = 4; i < 4 + redValue; i++) {
				
				if (enemyMembers.get(i) == null) {
					System.out.println(i + "\t" + enemyMembers);
					int possibleX = possibleEnemies.get(i-4).x;
					int possibleY = possibleEnemies.get(i-4).y;
					Soldier guessedEnemy = new 	Soldier(possibleX,
														possibleY,
														null,
														ID.Soldier,
														2,
														game,
														handler,
														"Guessed Enemy",
														i);
					battleGround[possibleX][possibleY] = guessedEnemy;
					enemyMembers.put(i, guessedEnemy);
				}

			}				
		}
		else
			for (int i = 0; i < blueValue; i++) {
				if (enemyMembers.get(i) != null) {
					int possibleX = possibleEnemies.get(i).x;
					int possibleY = possibleEnemies.get(i).y;
					Soldier guessedEnemy = new 	Soldier(possibleX,
														possibleY,
														null,
														ID.Soldier,
														1,
														game,
														handler,
														"Guessed Enemy",
														i);
					battleGround[possibleX][possibleY] = guessedEnemy;
					enemyMembers.put(i, guessedEnemy);
				}
			}
		
		System.out.println("Guessed and visible enemies" + enemyMembers);

		syncEnemies();

		
	}

	private void syncEnemies() {
		
		List<Soldier> visibleEnemies = getVisibleEnemies();
		if (visibleEnemies.size() == 0)
			return;
		
		for (Soldier soldier: visibleEnemies) {
			int x;
			int y;
			
			Soldier uEnemy = enemyMembers.get(soldier.number);
			
			//this should never be null
			if (uEnemy != null) {
				battleGround[uEnemy.getmX()][uEnemy.getmY()] = null;
				enemyMembers.put(soldier.number, soldier);
				battleGround[soldier.getmX()][soldier.getmY()] = soldier;
			}
		}
		
		
	}

	private void buildMap() {
		LinkedList<Cover> covers = handler.getCovers();
		teamMembers = handler.getTeamMembers(getTeam());
		
		for (Cover cover : covers){
			battleGround[cover.getmX()][cover.getmY()] = cover;
		}
		
		for (Soldier soldier: teamMembers) {
			battleGround[soldier.getmX()][soldier.getmY()] = soldier;

		}
	}



	private void addVisibleEnemies() {
		getVisibleEnemies();

		for (int i = it; i < it + 4; i++) {
			Soldier enemy = enemyMembers.get(i);
			if (enemy != null)			
			battleGround[enemy.getmX()][enemy.getmY()] = enemy;
		}	
		
	}

	private void clearEnemiesFromBattleground() {
		for(int i=0; i< Game.MAPSIZE; i++)
			for (int j=0; j< Game.MAPSIZE; j++) {
				if (battleGround[i][j] != null && battleGround[i][j].getId() == ID.Soldier) {
					Soldier soldier = (Soldier)battleGround[i][j];
					if (soldier.team != getTeam())
						battleGround[i][j] = null;
				}
			}
		
	}

	
	/**
	 * Gets the visible enemies. Also refreshes the visible enemies.
	 * 
	 */	
	@SuppressWarnings("unchecked")
	private List<Soldier> getVisibleEnemies() {

		
		System.out.println(enemyMembers);
		Set<Soldier> enemiesSet = new HashSet<Soldier>();
		for(Soldier soldier: teamMembers){
			LinkedList<Soldier> enemies = handler.getEnemiesInVision(soldier);
				
			enemiesSet.addAll(enemies);
			System.out.println("added to the enemies: " + enemyMembers);
			

		}

		ArrayList<Soldier> enemiesList = new ArrayList<Soldier>();
		for (Soldier sold:enemiesSet) {
			enemiesList.add(sold);
		}
		return enemiesList;
		
	}
	
	public void tick() {
		if (isThinking())
			return;
		
		Soldier currentMember = null;
		/*for (Soldier soldier: teamMembers) {
			if (soldier.isSelected()) {
				currentMember = soldier;
				break;
			}
				
		}*/
		if (handler.getActiveSoldier().team == this.team) {
			currentMember = handler.getActiveSoldier();
		}
		if (currentMember == null)
			return;
		setThinking(true);
		MiniMax.minimaxStep(game, handler, this, currentMember, 4);
		
		//calculateNextMove(currentMember);	

		
	}
	
	private List<Point> guessEnemyPositions(int invisibleEnemies) {

		possibleEnemies = new ArrayList<Point>();
		int furthestRow = getFurthestRow(teamMembers);
		System.out.println("Furthest row: " + furthestRow);
		
		Point possibleHeight = guessEnemyScatter();
		
		for (int j = possibleHeight.x; j<possibleHeight.y; j++)
			for (int i = 0; i< Game.MAPSIZE; i++) {
				if (possibleEnemies.size() == invisibleEnemies)
					return possibleEnemies;
				
				if (battleGround[i][j] == null)
					continue;
				
				switch (getTeam()) {
				
				case 1:
					if (i>furthestRow) {
						if (battleGround[i][j].getId() == ID.Cover && battleGround[i+1][j] == null)
							possibleEnemies.add(new Point(i + 1, j));
					}						
					break;
				
				case 2:
					if (i<furthestRow) {
						if (battleGround[i][j].getId() == ID.Cover && battleGround[i-1][j] == null)
							possibleEnemies.add(new Point(i - 1, j));
					}
					break;
				
				
				}

		}
		
		return possibleEnemies;
		
	}
	
	private Point guessEnemyScatter() {
		Point scatter = new Point(100,-100);
		for (Soldier soldier:teamMembers) {
			if (soldier.getmY() < scatter.x)
				scatter.x = soldier.getmY();
			if (soldier.getmY() > scatter.y)
				scatter.y = soldier.getmY();
		}
		
		return scatter;
	}

	private int getFurthestRow(LinkedList<Soldier> teamMembers2) {
		int retValue = 0;
		if (getTeam() == 1) {
			retValue = 0;
			for (Soldier sold : teamMembers2) {
				if (sold.getmX() > retValue)
					retValue = sold.getmX();
			}
		}
		else {
			retValue = Game.MAPSIZE;
			for (Soldier sold : teamMembers2) {
				if (sold.getmX() < retValue)
					retValue = sold.getmX();
			}
		}

		return retValue;
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		for (int i=0; i< Game.MAPSIZE; i++) {
			for (int j=0; j< Game.MAPSIZE; j++) {
				if (battleGround[j][i] == null)
					sb.append("0\t");
				else
				switch(battleGround[j][i].getId()) {
				case Cover:
				{
					sb.append("-1\t");
					break;
				}
				case Soldier:
				{
					Soldier tempSoldier = (Soldier)battleGround[j][i];
					if (tempSoldier.team == getTeam()) {
						sb.append("1\t");
					}
					else
						sb.append("2\t");
					break;						
				}
					
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public int getTeam() {
		return team;
	}

	private void setTeam(int team) {
		this.team = team;
	}

	public int getEnemyTeam() {
		return enemyTeam;
	}

	public void setEnemyTeam(int enemyTeam) {
		this.enemyTeam = enemyTeam;
	}

	public boolean isThinking() {
		return thinking;
	}

	public void setThinking(boolean thinking) {
		this.thinking = thinking;
	}

}
