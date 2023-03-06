package game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import akka.stream.impl.fusing.Map;
import events.EndTurnClicked;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;



public class Minimax implements Runnable{
	
	private static ActorRef out = null;
	private GameState gameState = null;
	private static JsonNode message = null;

	
	@SuppressWarnings("static-access")
	public Minimax(ActorRef out, GameState gameState, JsonNode message) {
		this.out = out;
		this.gameState = gameState;
		this.message = message;
	}

	@Override
	public void run() {
		minimax(this.gameState);
	}
	
	private static ArrayList<AttackAction> actions(GameState gameState){
		
		System.out.println("ACTIONS IN MINIMAX");
		ArrayList<AttackAction> actions = new ArrayList<>();
		
		
		
		for(Unit unit : gameState.getAIPlayer().getUnits()) {
			Set<Tile> targets = new HashSet<>();
			if (unit.hasAttacked()){
				continue;
			}
			// get all valid positions where the unit can go
			Set<Tile> positions = Utility.determineValidMoves(gameState.getBoard(), unit);
			Gui.highlightTiles(out, positions, 1);
			try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
			
			// get all valid attacks where the unit may attack
			targets.addAll(Utility.determineTargets(gameState.getBoard()[unit.getPosition().getTilex()][unit.getPosition().getTiley()], positions, gameState.getHumanPlayer(), gameState.getBoard()));
			Gui.highlightTiles(out, targets, 2);
			try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}	

			
			// Add tiles and units to actions by creating AttackAction objects
			for (Tile tile : targets) {
				actions.add(new AttackAction(unit,tile));
			}	
			
			
			Gui.removeHighlightTiles(out, gameState.getBoard());
			
		}
		
		
		for (AttackAction action : actions)
			System.out.println("Mac actions x = " + action.tile.getTilex() + " y = " + action.tile.getTiley() + " by " + action.unit);
		

		return actions;
	}
	/*
	 * get cards from AI player's hand
	 */
	private static void getPlayerHand() {
//		Set<CardAction> specialCards = new HashSet<>();//storing the specialAbility unit cards in the player's hand
//		Set<CardAction> regularCards = new HashSet<>(); //to store cards with no special ability
//		Set<CardAction> playableCards = new HashSet<>();
		Set<Card> cards = new HashSet<>();
		for(Card card: GameState.getCurrentPlayer().getHand()) {
			if(GameState.getCurrentPlayer().getHand().length == 0) { //if hand is empty
				System.out.println("No cards to play");
			}
			else {
				cards.add(card);
			}
		
		}
//		evaluateCards(specialCards,regularCards);
		evaluateCards(cards);
		}
	private static void minimax(GameState gameState) {
		/*
		 * start the whole thing and return an action 
		 */
		for (int moves = 0; moves < 2; moves++) {
			
			ArrayList<AttackAction> acts = actions(gameState);
			if (acts == null) {
				System.out.println("No more actions left on the board");
				break;
			}
			
			Set<AttackAction> actions = new HashSet<>(evaluateAttacks(acts, gameState));
			AttackAction action = bestAttack(actions);
			
			if (action.unit.hasAttacked())
				return;
			if (Math.abs(action.unit.getPosition().getTilex() - action.tile.getTilex()) < 2 && Math.abs(action.unit.getPosition().getTiley() - action.tile.getTiley()) < 2) {
				if (action.unit.hasAttacked())
					continue;
				System.out.println("Launching an adjacent attack");
				Utility.adjacentAttack(action.unit, action.tile.getOccupier());
			
			} else {
				if (action.unit.hasAttacked())		
					continue;
				System.out.println("Launching a distanced attack");
				
				Utility.distancedAttack(action.unit, action.tile.getOccupier(), gameState.getHumanPlayer());	
				try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		
//		EndTurnClicked endTurn = new EndTurnClicked();
//		endTurn.processEvent(out, gameState, message);
		
	}
	
	/*
	 *  Attack Values:
	 *  
	 *  5 - One shot one kill (no counter attack)
	 *  4 - Attack enemy avatar but with non-avatar unit
	 *  3 - Attack and only damage non-avatar enemy unit with non-avatar unit
	 *  2 - Attack with my avatar
	 */
	private static Set<AttackAction> evaluateAttacks(ArrayList<AttackAction> a, GameState gameState) {
		
		System.out.println("EVALUATing attacks...");
		if (a == null) {
			return null;
		}
		Set<AttackAction> actions = new HashSet<>(a);
		for (AttackAction action : actions) {
			if (action.tile.getOccupier().getHealth() <= action.unit.getAttack()) {
				action.value = 5;
				System.out.println("Action" + action.tile + " and " + action.unit + " value = " + action.value);
			} else if (action.tile.getOccupier().equals(gameState.getHumanPlayer().getUnits().get(0)) && !action.unit.equals(gameState.getAIPlayer().getUnits().get(0))) {
				action.value = 4;
				System.out.println("Action" + action.tile + " and " + action.unit + " value = " + action.value);
			} else if (!action.unit.equals(gameState.getAIPlayer().getUnits().get(0)) && !action.tile.getOccupier().equals(gameState.getHumanPlayer().getUnits().get(0))) {
				action.value = 3;
				System.out.println("Action" + action.tile + " and " + action.unit + " value = " + action.value);
			} else {
				action.value = 2;
				System.out.println("Action" + action.tile + " and " + action.unit + " value = " + action.value);
			}
		}
		return actions;
	}
	
	private static AttackAction bestAttack(Set<AttackAction> actions) {
		System.out.println("PICKING BEST ATTACK");
		Integer maxValue = -1;
		AttackAction bestAttack = null;
		
		for (AttackAction action : actions) {
			if (action.value > maxValue) {
				maxValue = action.value;
				bestAttack = action;
			}
		}
		System.out.println("Action" + bestAttack.tile + " and " + bestAttack.unit + " value = " + bestAttack.value);		
		return bestAttack;
	}
	
	/*
	 * evaluate which card the AI player should play.
	 */
//	continue working on this tomorrow!
	
	public static void evaluateCards(Set <Card> cards) {
		Set <CardAction> playableCards = new HashSet<>();
		if(playableCards.isEmpty()) { //if player has no cards in hand
			System.out.println("No cards to play!");
			return;
		}
		for (Card card: cards) {
			if(card.getCardname() != "Bloodshard Golem" || card.getCardname() != "Hailstone Golem") {
				playableCards.add(new CardAction(card,6));
			}else {
				playableCards.add(new CardAction(card,5));
			}
			int highestAttack = card.getBigCard().getAttack();
//			Card bestAttackCard = card; //best attack card  == this card. 
			if(card.getBigCard().getAttack() > highestAttack) { //also prioritises Serpenti, as Serpenti has highest attack
//				Card bestAttackCard = card;
				playableCards.add(new CardAction(card,7)); 
			}
		
//			Set <Tile> validTiles = 
			if(Utility.cardPlacements(card, GameState.getCurrentPlayer(), GameState.getHumanPlayer(), GameState.getBoard())cards ) {
				
			}
		}
	}
	
	/*
	 * pick the optimal card to play
	 */
	
	public static Card bestCard() {
		return null; //return bestCard for AI to play
	}
	
	//not sure if this should be separate or integrate w attack miniMax
	public static void miniMaxCards() { 
		getPlayerHand();
	}
	 
	
	

}
