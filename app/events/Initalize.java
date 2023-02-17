package events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CheckMoveLogic;
import demo.CommandDemo;
import game.logic.Gui;
import play.shaded.ahc.io.netty.util.internal.SystemPropertyUtil;
import play.twirl.api.TemplateMagic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import commands.BasicCommands;
import game.logic.Utility;
import game.logic.Gui;

/*
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 * @author Dr. Richard McCreadie
 * 
 */

public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		// hello this is a change
		Player humanPlayer = createHumanPlayer(out);
		gameState.setCurrentPlayer(humanPlayer);
		gameState.setHumanPlayer(humanPlayer);
		gameState.gameInitalised = true;
		
		gameState.something = true;
		
		/*
		 *  Create the board of Tile Objects
		 */
		for (int i = 0; i < gameState.board.length; i++) {
			for(int j = 0; j < gameState.board[0].length; j++) {
				gameState.board[i][j] = BasicObjectBuilders.loadTile(i, j);
				BasicCommands.drawTile(out, gameState.board[i][j], 0);
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		
		/*
		 * Place the Human Avatar on the board
		 */
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
		unit.setPositionByTile(gameState.board[3][2]); 
		gameState.board[3][2].setOccupier(unit);
		BasicCommands.drawUnit(out, unit, gameState.board[3][2]);
		humanPlayer.setUnit(unit);
		
		
				
		
		
		GameState.enemy = new Player();
		
		Unit enemyUnit = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 1, Unit.class);
		GameState.enemy.equals(enemyUnit);
		
		enemyUnit.setPositionByTile(gameState.board[5][2]); 
		gameState.board[5][2].setOccupier(enemyUnit);
		BasicCommands.drawUnit(out, enemyUnit, gameState.board[5][2]);
		
		GameState.enemy.setUnit(enemyUnit);
		
		
//		Unit enemyUnitTwo = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 2, Unit.class);
//		enemyUnitTwo.setPositionByTile(gameState.board[4][1]); 
//		BasicCommands.drawUnit(out, enemyUnitTwo, gameState.board[4][1]);
//		enemy.setUnit(enemyUnitTwo);
//		
//		Unit enemyUnit3 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 3, Unit.class);
//		enemyUnit3.setPositionByTile(gameState.board[2][3]); 
//		BasicCommands.drawUnit(out, enemyUnit3, gameState.board[2][3]);
//		enemy.setUnit(enemyUnit3);
		
//		Unit enemyUnit4 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 4, Unit.class);
//		enemyUnit4.setPositionByTile(gameState.board[4][4]); 
//		//gameState.board[4][4].setOccupier(enemyUnit4);
//		BasicCommands.drawUnit(out, enemyUnit4, gameState.board[4][4]);
//		enemy.setUnit(enemyUnit4);
		
//		Unit enemyUnit5 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 5, Unit.class);
//		enemyUnit5.setPositionByTile(gameState.board[4][3]); 
//		BasicCommands.drawUnit(out, enemyUnit5, gameState.board[4][3]);
//		enemy.setUnit(enemyUnit5);
//		
//		Unit enemyUnit6 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 6, Unit.class);
//		enemyUnit6.setPositionByTile(gameState.board[2][2]); 
//		BasicCommands.drawUnit(out, enemyUnit6, gameState.board[2][2]);
//		enemy.setUnit(enemyUnit6);
//		
//		Unit enemyUnit7 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 6, Unit.class);
//		enemyUnit7.setPositionByTile(gameState.board[2][1]); 
//		BasicCommands.drawUnit(out, enemyUnit7, gameState.board[2][1]);
//		enemy.setUnit(enemyUnit7);
		
//		Unit enemyUnit8 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 7, Unit.class);
//		enemyUnit8.setPositionByTile(gameState.board[1][1]); 
//		gameState.board[1][1].setOccupier(enemyUnit8);
//		BasicCommands.drawUnit(out, enemyUnit8, gameState.board[1][1]);
//		enemy.setUnit(enemyUnit8);
//		
//		Unit enemyUnit9 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 6, Unit.class);
//		enemyUnit9.setPositionByTile(gameState.board[0][4]); 
//		//gameState.board[0][4].setOccupier(enemyUnit9);
//		BasicCommands.drawUnit(out, enemyUnit9, gameState.board[0][4]);
//		enemy.setUnit(enemyUnit9);
		
		
	
		
//		Set<Tile> validMoves= Utility.determineValidMoves(gameState.board, gameState.board[3][2].getOccupier());
//		
//		Gui.highlightTiles(out, validMoves, 1);
//		
//		
//		Gui.highlightTiles(out, Utility.determineTargets(gameState.board[3][2], validMoves, enemy, gameState.board), 2);
		
//		/*
//		 * moving units
//		 */
//		// update reference to null 
//		gameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setOccupier(null);
//		BasicCommands.moveUnitToTile(out, unit, gameState.board[5][1], true);
//		unit.setPositionByTile(gameState.board[5][1]); 
//		//BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.move);
//		gameState.board[5][1].setOccupier(unit);
//		try {Thread.sleep(4000);} catch (InterruptedException e) {e.printStackTrace();}
//		Gui.removeHighlightTiles(out, gameState.board);
		
	}
	
	
	/*
	 * Create the Human Player
	 */
	
	public Player createHumanPlayer(ActorRef out) {
		Player player1 = new Player();
		for (int i = 0; i < 3; i++) {
			player1.drawCard();
		}

		return player1;
	}
	
	
}


