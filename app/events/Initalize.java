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
import structures.basic.AIPlayer;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.SpecialUnits.FireSpitter;
import structures.basic.SpecialUnits.Pyromancer;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import structures.basic.SpecialUnits.SilverguardKnight;
import structures.basic.SpecialUnits.Windshrike;

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
		
		Player humanPlayer = createHumanPlayer(out);

		GameState.setTotalUnits(); 

		gameState.setHumanPlayer(humanPlayer);
		gameState.setCurrentPlayer(gameState.getHumanPlayer());
		gameState.gameInitalised = true;
		gameState.emptyPreviousAction();

		
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
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 100, Unit.class);
		unit.setPositionByTile(gameState.board[3][2]); 
		gameState.board[3][2].setOccupier(unit);
		BasicCommands.drawUnit(out, unit, gameState.board[3][2]);

		gameState.getHumanPlayer().setUnit(unit);

		GameState.modifiyTotalUnits(1);
		
		Gui.setUnitStats(unit, gameState.getHumanPlayer().getHealth(), 2);
		unit.setHealth(gameState.getHumanPlayer().getHealth());
		unit.setAttack(2);
			

		/*
		 * TEST
		 */
////		
//		Unit unitTwo = (FireSpitter) BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 69, FireSpitter.class);
//		unitTwo.setPositionByTile(gameState.board[1][2]);
////
//		gameState.board[1][2].setOccupier(unitTwo);
//		BasicCommands.drawUnit(out, unitTwo, gameState.board[1][2]);
//		gameState.getHumanPlayer().setUnit(unitTwo);
//		B
//		Gui.setUnitStats(unitTwo, 4, 3);
//
//		unitTwo.setHealth(4);
//		unitTwo.setAttack(3);
////
////		GameState.modifiyTotalUnits(1);

		/*
		 * Enemy avatar stuff
		 */
		
		GameState.enemy = new AIPlayer();
		
		Unit enemyUnit = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 101, Unit.class);

		
		enemyUnit.setPositionByTile(gameState.board[5][2]); 
		gameState.board[5][2].setOccupier(enemyUnit);
		
		BasicCommands.drawUnit(out, enemyUnit, gameState.board[5][2]);
		
		Gui.setUnitStats(enemyUnit, 20, 2);
		
		enemyUnit.setHealth(20);
		enemyUnit.setAttack(2);
		GameState.modifiyTotalUnits(1);


		try {Thread.sleep(100);}catch (InterruptedException e){e.printStackTrace();}
		
		unit.setHealth(humanPlayer.getHealth());
		enemyUnit.setHealth(GameState.enemy.getHealth());

		GameState.getAIPlayer().setUnit(enemyUnit);

		/*
		 * TEST
		 */
		
		Unit unitTwo = (Pyromancer)BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Pyromancer.class);
		unitTwo.setPositionByTile(gameState.board[5][3]); 
		
		gameState.board[5][3].setOccupier(unitTwo);
		BasicCommands.drawUnit(out, unitTwo, gameState.board[5][3]);
		gameState.getAiPlayer().setUnit(unitTwo);
		
		Gui.setUnitStats(unitTwo, 3, 3);
		
		unitTwo.setHealth(4);
		unitTwo.setAttack(3);
		
		GameState.modifiyTotalUnits(1);
		
		Unit unit3 = (SilverguardKnight)BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight, 70, SilverguardKnight.class);
		unit3.setPositionByTile(gameState.board[5][1]); 
		
		gameState.board[5][1].setOccupier(unit3);
		BasicCommands.drawUnit(out, unit3, gameState.board[5][1]);
		gameState.getAiPlayer().setUnit(unit3);
		
		Gui.setUnitStats(unit3, 3,1);
		
		unit3.setHealth(3);
		unit3.setAttack(1);
		
		GameState.modifiyTotalUnits(1);

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


