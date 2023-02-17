package events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CheckMoveLogic;
import demo.CommandDemo;
import play.shaded.ahc.io.netty.util.internal.SystemPropertyUtil;
import play.twirl.api.TemplateMagic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
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
		
		gameState.gameInitalised = true;
		
		gameState.something = true;
		
		// User 1 makes a change
		//CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
		//CheckMoveLogic.executeDemo(out);
		
		for (int i = 0; i < gameState.getBoard().length; i++) {
			for(int j = 0; j < gameState.getBoard()[0].length; j++) {
				gameState.getBoard()[i][j] = BasicObjectBuilders.loadTile(i, j);
				BasicCommands.drawTile(out, gameState.getBoard()[i][j], 0);
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
		unit.setPositionByTile(gameState.getBoard()[3][2]); 
		gameState.getBoard()[3][2].setOccupier(unit);
		BasicCommands.drawUnit(out, unit, gameState.getBoard()[3][2]);
		
		Player enemy = new Player();
		
		Unit enemyUnit = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 1, Unit.class);
		
		enemyUnit.setPositionByTile(gameState.getBoard()[5][2]); 
		//gameState.board[5][2].setOccupier(enemyUnit);
		BasicCommands.drawUnit(out, enemyUnit, gameState.getBoard()[5][2]);
		
		enemy.setUnit(enemyUnit);
		
		
//		Unit enemyUnitTwo = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 2, Unit.class);
//		enemyUnitTwo.setPositionByTile(gameState.board[4][1]); 
//		BasicCommands.drawUnit(out, enemyUnitTwo, gameState.board[4][1]);
//		enemy.setUnit(enemyUnitTwo);
//		
//		Unit enemyUnit3 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 3, Unit.class);
//		enemyUnit3.setPositionByTile(gameState.board[2][3]); 
//		BasicCommands.drawUnit(out, enemyUnit3, gameState.board[2][3]);
//		enemy.setUnit(enemyUnit3);
		
		Unit enemyUnit4 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 4, Unit.class);
		enemyUnit4.setPositionByTile(gameState.getBoard()[4][4]); 
		//gameState.board[4][4].setOccupier(enemyUnit4);
		BasicCommands.drawUnit(out, enemyUnit4, gameState.getBoard()[4][4]);
		enemy.setUnit(enemyUnit4);
		
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
		
		Unit enemyUnit8 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 7, Unit.class);
		enemyUnit8.setPositionByTile(gameState.getBoard()[1][1]); 
		gameState.getBoard()[1][1].setOccupier(enemyUnit8);
		BasicCommands.drawUnit(out, enemyUnit8, gameState.getBoard()[1][1]);
		enemy.setUnit(enemyUnit8);
		
		Unit enemyUnit9 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 6, Unit.class);
		enemyUnit9.setPositionByTile(gameState.getBoard()[0][4]); 
		//gameState.board[0][4].setOccupier(enemyUnit9);
		BasicCommands.drawUnit(out, enemyUnit9, gameState.getBoard()[0][4]);
		enemy.setUnit(enemyUnit9);
		
		
		Set<Tile> positions = new HashSet<Tile>();
		positions.add(gameState.getBoard()[3][3]);
		positions.add(gameState.getBoard()[2][2]);
		positions.add(gameState.getBoard()[4][3]);
		positions.add(gameState.getBoard()[1][2]);
		positions.add(gameState.getBoard()[2][1]);
		positions.add(gameState.getBoard()[4][2]);
		positions.add(gameState.getBoard()[4][1]);
		//System.out.println(gameState.board[3][2].getOccupier().hasAttacked());
		
		
		Gui.highlightTiles(out, Utility.determineTargets(gameState.getBoard()[3][2], positions, enemy, gameState.getBoard()), 2);
		
	}
}


