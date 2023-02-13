package structures;

import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import structures.basic.Card;
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
	private static int turnNumber;
	private static Stack<Object> stack;
	private boolean gameInitalised;
	private boolean gameOver;
	private Tile[][] board;
	private boolean humanTurn;
	private Player humanPlayer;
	private Player aiPlayer;
	

	public GameState(){
		public Tile[][] board = new Tile[9][5];
		turnNumber = true;
		stack = new Stack<Object>();

		gameInitalised = true;
		gameOver = false;
		
	}

	public boolean checkValidMove(int x, int y){
		Tile tile = board[x][y];
		Card card = stack.peek();

		
	}

	public int getStatus(){
		return status;
	}

	public Object peekStack(){
		return stack.peek();
	}

	public Tile[][] getBoard(){
		return board;
	}

	public Player getPlayer(boolean input){
		if (humanTurn){
			if (input){
				return humanPlayer;
			}
			return aiPlayer;
		}
		if (input){
			return 
		}
	




	
	
	
	
	
}
