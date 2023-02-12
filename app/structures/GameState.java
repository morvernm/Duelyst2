package structures;

import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
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

	
	public boolean gameInitalised = false;
	
	public boolean something = false;
	
	Stack<Object> myStack = new Stack<>();
	
	public Tile[][] board = new Tile[9][5];
	
	
	
	
	
}
