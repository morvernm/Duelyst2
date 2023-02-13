package structures;

import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import structures.basic.Player;
import structures.basic.Tile;


/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {
	public static Player currentPlayer; // store who's round it currently is
	public boolean gameInitalised = false;
	
	
	public boolean something = false;
	
//	storing validMoves and valid Attacks
	public Set<Tile> validMoves = new HashSet<>();
	public Set<Tile> validAttacks = new HashSet<>();
	
//	stack of actions taken by the player
	public static Stack<Object> previousAction = new Stack<Object>();	
	
	public Tile[][] board = new Tile[9][5];
	
	public static Object getPreviousAction() {
		return previousAction.peek();
	}
	public static Player getPlayer() {
		return currentPlayer;
	}

}
