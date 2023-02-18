package game.logic;

import actors.GameActor;
import akka.actor.ActorRef;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import akka.japi.Effect;
import commands.BasicCommands;
import structures.basic.*;
import akka.actor.CoordinatedShutdown;

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
		
	/*
	 To update cards in hand.
	 */
	public static void displayCard(Card card, int position) {
		BasicCommands.drawCard(out, card, position, 0);
	}

	public static void displayHumanHP(Player player){
		BasicCommands.setPlayer1Health(out, player);
	}

	public static void displayHumanMana(Player player) {
		BasicCommands.setPlayer1Mana(out, player);
	}

	public static void displayAIMana(Player player) {
		BasicCommands.setPlayer2Mana(out, player);
	}

	// draw unit on board
	public static void drawUnit(Unit unit, Tile tile) {
		BasicCommands.drawUnit(out, unit, tile);
	}

	public static void highlightCard(Card card, int position) {
		BasicCommands.drawCard(out, card, position,1);
	}

	public static void setUnitHealth(Unit unit, int health) {
		BasicCommands.setUnitHealth(out, unit, health);
	}

	public static void playEffectAnimation(EffectAnimation effect, Tile tile) {
		BasicCommands.playEffectAnimation(out, effect, tile);
	}

	public static void deleteCard(int position) {
		BasicCommands.deleteCard(out, position);
	}

}

