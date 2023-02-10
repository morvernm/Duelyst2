package game.logic;
import akka.actor.ActorRef;
import java.util.ArrayList;
import actors.GameActor;
import akka.actor.CoordinatedShutdown;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import utils.BasicObjectBuilders;

/*
 * This class will be used to display and update information on the screen
 * 
 * 
 */

public class Gui {
	private static ActorRef out;

	public Gui(ActorRef out) {
		Gui.out = out;
	}
	
	/*
	 *  to highligh tiles on the screen
	 *  Can be used for both movement and attacks
	 *  mode 1 = movement and summon 
	 *  mode 2 = attack 
	 */
	public static void highlightTiles(ActorRef out, ArrayList<Tile> tiles, int mode) {
		
		for (Tile tile : tiles) {
			BasicCommands.addPlayer1Notification(out, "drawingAttackTile", 10);
			
			BasicCommands.drawTile(out, tile, mode);
			//try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	/*
	 To update cards in hand.
	 */
	public static void displayCard(Card card, int position, int mode) {
		BasicCommands.drawCard(out, card, position, mode);
	}

	public static void displayHumanHP(Player player){
		BasicCommands.setPlayer1Health(out, player);
	}

	public static void displayHumanMana(Player player) {
		BasicCommands.setPlayer1Mana(out, player);
	}

}
