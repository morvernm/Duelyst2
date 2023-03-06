package game.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import events.EndTurnClicked;
import structures.GameState;
import structures.basic.Player;
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

	public static ArrayList<MoveAction> moves(GameState gameState) {
		System.out.println("MOVES IN MINIMAX");
		ArrayList<MoveAction> moves = new ArrayList<>();
		Set<Tile> positions;

		for (Unit unit : gameState.getAIPlayer().getUnits()) {
			positions = Utility.determineValidMoves(gameState.getBoard(), unit);
			if (unit.hasAttacked() && unit.hasMoved()) {
				continue;
			} else if (!unit.hasMoved() && !unit.hasAttacked()) {
				positions = Utility.determineValidMoves(gameState.getBoard(), unit);

			}

			for (Tile tile : positions) {
				moves.add(new MoveAction(unit, tile));
			}
		}
		return moves;

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

			System.out.println("Let's move");

			ArrayList<MoveAction> possibleMoves = moves(gameState);
			if (possibleMoves == null) {
				System.out.println("No more moves left on the board");
			}

			Set<MoveAction> movess = new HashSet<>(evaluateMoves(possibleMoves, gameState));
			MoveAction bestMove = bestMove(movess);
			Utility.moveUnit(bestMove.attacker, bestMove.moveToTile);
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

	private static Set<MoveAction> evaluateMoves(ArrayList<MoveAction> a, GameState gameState) {
		System.out.println("Evaluating moves...");
		if (a == null) {
			return null;
		}

		Set<MoveAction> moves = new HashSet<>(a);
		for (MoveAction move : moves) {
			if (!move.attacker.hasMoved() && !move.attacker.hasAttacked()) {
				Set<Tile> tiles = Utility.determineValidMoves(gameState.board, move.attacker);
				ArrayList<Unit> enemyUnits = gameState.getHumanPlayer().getUnits();

				int minScore = Integer.MAX_VALUE;
				for (Tile tile : tiles) {
					for (Unit enemy : enemyUnits) {
						int score = 0;
						score = score + Math.abs(tile.getTilex() - enemy.getPosition().getTilex());
						score = score + Math.abs(tile.getTiley() - enemy.getPosition().getTilex());
						score = score + enemy.getHealth(); // total score considers the health of the unit too
						if (!enemy.equals(gameState.getHumanPlayer().getUnits().get(0))) {
							score = score + 5; // prioritise moving towards avatar
						}
						if (score < minScore && tile.getOccupier() == null) {
							move.value = score;
						}
					}
				}
			}
		}
		return moves;
	}

	//				ArrayList<Unit> lowHealthUnit = new ArrayList<>();
//				for (Tile tile : tiles) {
//					for (Unit unit : enemyUnits) {
//						if (unit.getHealth() == unitHealth) {
//							int enemyX = unit.getPosition().getTilex();
//							int enemyY = unit.getPosition().getTiley();
//							int tileX = tile.getTilex();
//							int tileY = tile.getTiley();
//							// moving directly towards unit
//							if (enemyY - tileY == 0 || enemyX - tileX == 0 && unit.getId() == 0) {
//								move.value = 5;
//							}
//						}
//					}
//				}

	private static MoveAction bestMove(Set<MoveAction> moves) {
		System.out.println("PICKING BEST MOVE");
		Integer maxValue = Integer.MAX_VALUE;
		MoveAction bestMove = null;

		for (MoveAction move : moves) {
			if (move.value < maxValue) {
				maxValue = move.value;
				bestMove = move;
			}
		}
		if (bestMove != null) {
			System.out.println("Move" + bestMove.attacker + " value = " + bestMove.value);
		} else {
			System.out.println("No available moves");
		}
		return bestMove;

	}
	

}
