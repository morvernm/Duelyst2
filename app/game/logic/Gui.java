package game.logic;
import actors.GameActor;
import akka.actor.ActorRef;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import commands.BasicCommands;
import structures.basic.Player;
import structures.basic.Tile;
import utils.BasicObjectBuilders;

/*
 * This class will be used to display and update information on the screen
 * 
 * 
 */

public class Gui {
	
	/*
	 *  to highlight tiles on the screen
	 *  Can be used for both movement and attacks
	 *  mode 1 = movement and summon 
	 *  mode 2 = attack 
	 */
	public static void highlightTiles(ActorRef out, Set<Tile> tiles, int mode) {
		
		for (Tile tile : tiles) {
			BasicCommands.addPlayer1Notification(out, "drawingAttackTile", 10);
			
			BasicCommands.drawTile(out, tile, mode);
			try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public static void removeHighlightTiles(ActorRef out, Tile[][] board) {

		Set<Tile> unhighlightedTiles = new HashSet<>();

		for (Tile[] tiles : board) {
			unhighlightedTiles.addAll(Arrays.asList(tiles).subList(0, board[0].length));
		}
		highlightTiles(out, unhighlightedTiles, 0);
	}

}