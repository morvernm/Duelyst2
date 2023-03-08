package game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import akka.stream.impl.fusing.Map;
import commands.BasicCommands;
import events.CardClicked;
import events.EndTurnClicked;
import structures.GameState;
import structures.basic.Card;

import structures.basic.SpecialUnits.Provoke;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.spellcards.EntropicDecay;
import structures.basic.spellcards.SpellCard;
import structures.basic.spellcards.Ykir;
import structures.basic.SpecialUnits.Windshrike;


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

	private static HashMap<SpellCard, ArrayList<SpellAction>> spellActions (GameState gameState){
		HashMap<SpellCard,ArrayList<SpellAction>> actions = new HashMap<>();
		Set<SpellCard> spellcards = new HashSet<>(); // used to store what spellcards the player has in their hand

		// Check if AI has spellcards in hand
		if(GameState.getAIPlayer().handIsEmpty()) {System.out.println("AI hand empty"); return null;}

		System.out.println("Checking for spell cards...");
		for(Card card: GameState.getCurrentPlayer().getHand()) {
			if (card instanceof EntropicDecay || card instanceof Ykir){
				// Add to list of possible actions if have mana to play that card
				System.out.println("Adding " + card.getCardname());
				if(GameState.getAIPlayer().getMana() >= card.getManacost()) {
					spellcards.add((SpellCard) card);
				}
			}
		}

		// If have spellcards, get their valid target units and associated tile positions. Else, return null
		if(spellcards.isEmpty()) {return null;}

		for(SpellCard card: spellcards) {
			Set<Tile> targets = Utility.getSpellTargetPositions(card.getTargets());
			if(targets == null){continue;}

			ArrayList<SpellAction> a = new ArrayList<>();
			for(Tile target: targets){
				SpellAction action = new SpellAction(target.getOccupier(), target, card);
				a.add(action);
			}
			System.out.print("Actions for " + card.getCardname() + " is null:");
			System.out.print(a.isEmpty());
			actions.put(card,a);
		}

		return actions;
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
//			Gui.highlightTiles(out, positions, 1);
//			try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
			
			// get all valid attacks where the unit may attack
			targets.addAll(Utility.determineTargets(gameState.getBoard()[unit.getPosition().getTilex()][unit.getPosition().getTiley()], positions, gameState.getHumanPlayer(), gameState.getBoard()));
//			Gui.highlightTiles(out, targets, 2);
//			try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}	

			
			// Add tiles and units to actions by creating AttackAction objects
			for (Tile tile : targets) {
				actions.add(new AttackAction(unit,tile));
			}	
			
			
//			Gui.removeHighlightTiles(out, gameState.getBoard());
			
		}
		
		
		for (AttackAction action : actions)
			System.out.println("Mac actions x = " + action.tile.getTilex() + " y = " + action.tile.getTiley() + " by " + action.unit);
		

		return actions;
	}
	/*
	 * this method gets cards from AI player's hand
	 * 
	 */
	private static Set<Card> getPlayerHand() {
		System.out.println("Getting player hand");
		Set<Card> cards = new HashSet<>();
		for(Card card: GameState.getAIPlayer().getHand()) {
			if(GameState.getAiPlayer().getHand().length == 0) { //if hand is empty
				System.out.println("No cards to play");
			}
			else {
				cards.add(card);
			}
		
		}
		System.out.println("number of cards in set " + cards.size());
		return cards; 
		}
	

	/*
	 * collects and evaluates and available moves AI player can make on the board:
	 * 1. best card to play
	 * 2. best attack 
	 * 3. best move
	 * 
	 * 
	 */
	private static void minimax(GameState gameState) {
		/*
		 * start the whole thing and return an action 
		 */

		
		// try the spells first
		minimaxSpells(gameState);
		miniMaxCards();
		try {

			for (int moves = 0; moves < 3; moves++) {
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


		} catch (NullPointerException exception) {
			
		} finally {
			//try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}	

			EndTurnClicked endTurn = new EndTurnClicked();
			endTurn.processEvent(out, gameState, message);
		}

	}
	
	/*
	 *  Attack Values:
	 *  
	 *  5 - One shot one kill (no counter attack)
	 *  4 - Attack enemy avatar but with non-avatar unit
	 *  3 - Attack and only damage non-avatar enemy unit with non-avatar unit
	 *  2 - Attack with my avatar
	 */

	// Variant of minimax just for spells;
	// perhaps could later be integrated to a more overarching logic that handles unit cards,
	// attacks, spell cards etc.
	public static void minimaxSpells(GameState gameState) {

		while (true) {
			// Get all possible spell actions and evaluate them
			HashMap<SpellCard, ArrayList<SpellAction>> actions = spellActions(gameState);

			// Check if there are any actions left to play
			if (actions == null || actions.isEmpty()) {
				System.out.println("No spells left to play");
				return;
			}
			evaluateSpells(actions, gameState);
			SpellAction bestSpell = bestSpell(actions,gameState);

			// If so, play best spell and deduct mana
			System.out.println(bestSpell==null);
			bestSpell.spellCard.castSpell(bestSpell.unit,bestSpell.tile);
			GameState.getAIPlayer().setMana(GameState.getAIPlayer().getMana() - bestSpell.spellCard.getManacost());
			// Remove from AI hand
			for(int i = 0; i < GameState.getAIPlayer().getHand().length; i++) {
				if(bestSpell.spellCard == GameState.getAIPlayer().getHand()[i]) {
					System.out.println("removing " + GameState.getAIPlayer().getHand()[i].getCardname() + " from AI hand.");
					GameState.getAIPlayer().removeFromHand(i + 1);
				}
			}
		}
	}


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

	private static void evaluateSpells(HashMap<SpellCard,ArrayList<SpellAction>> actions, GameState gameState) {

		if(actions == null) return;

		// Go through possible actions. Check what time of spellcard is used for action (i.e. to buff friendlies, or attack enemies). Evaluate accordingly.
		for(var entry: actions.entrySet()){
			for(SpellAction action: entry.getValue()) {
				if(entry.getKey() instanceof EntropicDecay) { // if entropic decay, high priority
					if(action.unit instanceof Provoke){ // prioritise getting rid of provoke targets
						action.value = 10;
					} else{
						action.value = 9;
					}
				} else { // else, must be Staff of Ykir. High priority again as buffs player's own avatar
					action.value = 9;
				}
			}
		}
	}

	// Method to find best spell. Once done, remove spell card so it cannot be played again.
	private static SpellAction bestSpell(HashMap<SpellCard, ArrayList<SpellAction>> actions, GameState gameState) {
		int maxValue = -1;
		SpellAction bestSpell = null;
		ArrayList<SpellCard> toRemove = new ArrayList<>(); // list of cards to remove from deck if we decide either to play that card, or find it has no actions left.

		for (var entry : actions.entrySet()) {
			System.out.println(entry.getKey().getCardname());
			System.out.println(entry.getValue().isEmpty());
			for (SpellAction spell : entry.getValue()) { // For every possible action for each spell card
				if (spell.value > maxValue) { // Find spell action with highest value
					System.out.println("adding new best spell");
					maxValue = spell.value;
					bestSpell = spell;
					toRemove.add(entry.getKey());
				}
			}
		}
		// Remove spell card which will play spell so it cannot be played again / remove cards that no longer have valid actions
		for(SpellCard forRemoval: toRemove) {
			actions.remove(forRemoval);
		}

		return bestSpell;
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
 * This method evaluates each card in the AI player's hand
 * Values:
 * 4 - provoke card
 * 3 - high attack
 * 2 - other special ability card
 * 1 - reg card
 * 
 */
	
	public static Set<CardAction> evaluateCards(Set<Card> cards) {
		System.out.println("evaluating cards");
		Set <CardAction> playableCards = new HashSet<>();
		if(cards.isEmpty()) { //if player has no cards in hand
			System.out.println("No cards to play!");
		}
		int highestAttack = -1;
		Card bestAttack = null;
			for (Card card: cards) {
				if(card == null || card.getCardname().equals("Staff of Y'Kir'") || card.getCardname().equals("Entropic Decay")) {
					continue;
				}
				System.out.println(card.getCardname());
				CardAction AICard = new CardAction(card);
				if(GameState.getAIPlayer().getMana() >= card.getManacost()) {
					playableCards.add(AICard);
					if(card.getBigCard().getAttack() > highestAttack) {
						bestAttack = card;
					}
					if(card.getCardname().equals("Rock Pulveriser")) { //provoke card change to .equals.
						AICard.value = 4;
						continue;
					}
					else if(card.getBigCard().getAttack() > highestAttack) { // prioritises Serpenti, as Serpenti has highest attack
						AICard.value = 3;
						continue;
					}
					else if(!card.getCardname().equals("Bloodshard Golem")) { //special cards
						AICard.value = 2;
						continue;
					}else if(!card.getCardname().equals("Hailstone Golem")) {
						AICard.value = 2;
						continue;
					}else { 												//reg cards
						AICard.value = 1;
						continue;
					}
				}
			}
			System.out.println("number of cards from evaluatecards " + playableCards.size());
			return playableCards;
		}
	
	public static Tile evaluateTiles(CardAction bestCard) {
		int minScore = Integer.MAX_VALUE;
		Tile bestTile = null;
		Set<Tile> validSummonPlacement = Utility.cardPlacements(bestCard.getCard(), GameState.getAIPlayer(), GameState.getHumanPlayer(), GameState.getBoard());
		for(Tile tile: validSummonPlacement) {
			int score = 0;
			score+= Math.abs(tile.getTilex() - GameState.getHumanPlayer().getAvatar().getPosition().getTilex());
			score+= Math.abs(tile.getTiley() - GameState.getHumanPlayer().getAvatar().getPosition().getTiley());
			if(score < minScore) {
				bestTile = tile;
				minScore = score;
			}
			System.out.println("best tile X " + bestTile.getTilex());
		
		}return bestTile;
	}
	
	/*
	 * pick the optimal card to play
	 */
	
	public static CardAction bestCard(Set <CardAction> AICards) {
		System.out.println("pick best card");
		CardAction bestCard = null;
		int maxValue = Integer.MIN_VALUE;
		for(CardAction card: AICards) {
			if(card.value > maxValue) {
				bestCard = card;	
				maxValue = card.value;
			}
			maxValue = Integer.MIN_VALUE;
			
		}
		//System.out.println("bestCardprint" + bestCard.getCard().getCardname() + "position in hand " + bestCard.getCard().getPositionInHand());
		return bestCard; //return bestCard for AI to play
	}
	
	public static void miniMaxCards() { 
		System.out.println("START MINIMAX CARDS");
		CardAction bestCard;
			Set<CardAction> cardActions = evaluateCards(getPlayerHand());
			System.out.println("Evaluate Cards size" + cardActions.size());
			bestCard = bestCard(cardActions);
			Tile destinationTile = evaluateTiles(bestCard);
			
			System.out.println("tile x : " + destinationTile.getTilex());
			System.out.println("tile y : " + destinationTile.getTiley());
			
			System.out.println("avatar position x" + GameState.getHumanPlayer().getAvatar().getPosition().getTilex());
			Utility.placeUnit(out, bestCard.getCard(), GameState.getAiPlayer(), destinationTile);
			try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}		
			}
}
	 
	
	

//}
