
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import game.logic.Utility;
import play.libs.Json;
import structures.GameState;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * To test attacking functionality of the game
 * 
 * @author bozhidarayvazov
 *
 */

public class AttackTest {
	
	@Test
	public void distancedAttackTest() {
		
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Unit avatar = GameState.getCurrentPlayer().getAvatar();
		
		
		Unit defender = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Unit.class);
		defender.setPositionByTile(gameState.board[4][2]); 
		gameState.board[4][2].setOccupier(defender);
		
		gameState.getAiPlayer().setUnit(defender);
		GameState.modifiyTotalUnits(1);
		defender.setHealth(4);
		defender.setAttack(3);
		
		/**
		 * test the attacking function
		 */
		
		assertTrue(defender.getHealth() == 4);
		
		Utility.distancedAttack(avatar, defender, GameState.getOtherPlayer());
		
		assertTrue(defender.getHealth() == (4 - avatar.getAttack()));
		
		/**
		 * 
		 * Test the movement to target functionality as well
		 */
		
		assertTrue(avatar.getPosition().getTilex() == GameState.getBoard()[3][2].getTilex());
		assertTrue(avatar.getPosition().getTiley() == GameState.getBoard()[3][2].getTiley());
		
		
	}
	
	@Test
	public void distancedAttackTestTwo() {
		
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Unit avatar = GameState.getCurrentPlayer().getAvatar();
		
		
		Unit defender = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Unit.class);
		defender.setPositionByTile(gameState.board[4][2]); 
		gameState.board[4][2].setOccupier(defender);
		
		gameState.getAiPlayer().setUnit(defender);
		GameState.modifiyTotalUnits(1);
		defender.setHealth(4);
		defender.setAttack(3);
		
		/**
		 * 
		 * Test the movement to target functionality as well
		 */
		assertTrue(avatar.getPosition().getTilex() == GameState.getBoard()[1][2].getTilex());
		assertTrue(avatar.getPosition().getTiley() == GameState.getBoard()[1][2].getTiley());
		
		Utility.distancedAttack(avatar, defender, GameState.getOtherPlayer());
				
		assertTrue(avatar.getPosition().getTilex() == GameState.getBoard()[3][2].getTilex());
		assertTrue(avatar.getPosition().getTiley() == GameState.getBoard()[3][2].getTiley());
		
		
	}
	

}
