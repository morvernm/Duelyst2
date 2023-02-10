package game.logic;
import akka.actor.ActorRef;
import java.util.ArrayList;

import commands.BasicCommands;
import structures.basic.Tile;
import utils.BasicObjectBuilders;

/*
 * This class will be used to display and update information on the screen
 * 
 * 
 */

public class Gui {
	
	
	
	public static void highlightTiles(ActorRef out, ArrayList<Tile> tiles, int mode) {
		
		for (Tile tile : tiles) {
			BasicCommands.addPlayer1Notification(out, "drawingAttackTile", 10);
			
			BasicCommands.drawTile(out, tile, 2);
			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

}
