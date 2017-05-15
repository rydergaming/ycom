package ycom.game;

import java.util.ArrayList;
import java.util.Collections;

public class BattleGroundOperations {
	
	public static GameObject[][] copyBattleGround(GameObject[][] BG) {
		GameObject[][] newBG = new GameObject[Game.MAPSIZE][Game.MAPSIZE];
		
		for (int i = 0; i< Game.MAPSIZE; i++) {
			for (int j= 0; j< Game.MAPSIZE; j++)
				newBG[i][j] = null;
		}
		
		for (int i=0;i<Game.MAPSIZE; i++) {
			for (int j=0; j<Game.MAPSIZE; j++) {
				if (BG[i][j] == null) {
					newBG[i][j] = null;
					continue;
				}
					
				switch (BG[i][j].id) {
				
				case Soldier:
					Soldier clonedSoldier = ((Soldier) BG[i][j]).clone();
					newBG[i][j] = clonedSoldier;
					
					break;
					
				case Cover:
					
					Cover clonedCover = ((Cover) BG[i][j]).clone();
					newBG[i][j] = clonedCover;
					
					break;
				}
			}
		}
		
		
		return newBG;
	}
		
		
	public static Soldier nextSoldier(GameObject[][] bg, int soldNumber) {
		
		ArrayList<Soldier> soldiers = new ArrayList<>();
		
		Soldier nextSoldier = null;
		
		for (int i= 0; i< Game.MAPSIZE; i++)
			for (int j=0; j<Game.MAPSIZE; j++) {
				if (bg[i][j] != null)
					if (bg[i][j].id == ID.Soldier) {
						soldiers.add((Soldier)bg[i][j]);
					}
			}
		
		Collections.sort(soldiers);
		
		for (Soldier sold: soldiers) {
			if (sold.number > soldNumber) {
				nextSoldier = sold;
				return nextSoldier;
			}

				
		}
		
		if (nextSoldier == null)
			nextSoldier = soldiers.get(0);
		
		return nextSoldier;
		
	}
	
	public static Soldier currentSoldier(GameObject[][] bg, int soldNumber) {
		
		for (int i= 0; i< Game.MAPSIZE; i++)
			for (int j=0; j<Game.MAPSIZE; j++) {
				if (bg[i][j] != null)
				if (bg[i][j].id == ID.Soldier) {
					if (((Soldier)bg[i][j]).number == soldNumber)
						return (Soldier)bg[i][j];
				}
			}
		
		
		return null;		
	}
	
	public static Soldier currentSoldier(GameObject[][] bg) {
		
		for (int i= 0; i< Game.MAPSIZE; i++)
			for (int j=0; j<Game.MAPSIZE; j++) {
				if (bg[i][j] != null)
				if (bg[i][j].id == ID.Soldier) {
					if (((Soldier)bg[i][j]).isSelected())
						return (Soldier)bg[i][j];
				}
			}
		
		
		return null;		
	}
	
	public static String bgToString(GameObject[][] battleGround) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i< Game.MAPSIZE; i++) {
			for (int j=0; j< Game.MAPSIZE; j++) {
				if (battleGround[j][i] == null)
					sb.append("0    ");
				else
				switch(battleGround[j][i].getId()) {
				case Cover:
				{
					sb.append("X    ");
					break;
				}
				case Soldier:
				{
					Soldier tempSoldier = (Soldier)battleGround[j][i];
					if (tempSoldier.team == 1) {
						sb.append(tempSoldier.hp + "    ");
					}
					else
						sb.append("-" + tempSoldier.hp +"    ");
					break;						
				}
					
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}


	public static void removeSoldier(GameObject[][] battleGround, Soldier target) {
		for (int i=0; i< Game.MAPSIZE; i++) {
			for (int j=0; j< Game.MAPSIZE; j++) {
				if (battleGround[i][j] != null && battleGround[i][j].getId() == ID.Soldier) {
					Soldier selected = (Soldier)battleGround[i][j];
					if (selected.number == target.number) {
						battleGround[i][j] = null;
					}
				}
			}
		}
		
	}
	

}
