package structures;

import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import structures.basic.Player;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;


/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {

	private static Player currentPlayer; // store who's round it currently is
	private static Player humanPlayer;
	
	public static Player enemy;

	public boolean gameInitalised = false;
	public boolean something = false;
	
//	storing validMoves and valid Attacks
	public Set<Tile> validMoves = new HashSet<>();
	public Set<Tile> validAttacks = new HashSet<>();
	
//	stack of actions taken by the player
	public static Stack<Object> previousAction = new Stack<Object>();	
	
	public Tile[][] board = new Tile[9][5];
	
	public static Object getPreviousAction() {
		return previousAction.pop();
	}	
	
	public static void setPreviousAction(Object action) {
		previousAction.push(action);
	}
	
	public static Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
	}

	public void setHumanPlayer(Player player) {
		this.humanPlayer = player;
	}

	public static Player getHumanPlayer() {
		return humanPlayer;
	}

	public static Player getAIPlayer() {
		return enemy;
	}

}

