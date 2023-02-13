package structures;

import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
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
	private Player currentPlayer; // store who's round it currently is
	private Player humanPlayer;

	
	public boolean gameInitalised = false;
	
	public boolean something = false;
	
	//Stack<Integer> myStack = new Array<Integer>();
	
	public static Queue<Integer> myQueue = new LinkedList<Integer>();		
	public Tile[][] board = new Tile[9][5];

	public Player getCurrentPlayer() {
		return  currentPlayer;
	}

	public void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
	}

	public void setHumanPlayer(Player player) {
		this.humanPlayer = player;
	}

	public Player getHumanPlayer() {
		return this.humanPlayer;
	}
	}
