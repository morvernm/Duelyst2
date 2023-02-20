package game.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;

import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.BigCard;
import structures.basic.Card;
import structures.basic.UnitAnimationSet;
import structures.basic.EffectAnimation;
import structures.basic.Playable;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


public class Utility {
	/*
	 * This class is the utility class where methods with some main logic of the game will be provided
	 * 
	 */	
	public static void placeUnit(ActorRef out, Card card, Player player, Tile tile){
		
		/* Set unit id to number of total units on board + 1 */
        String unit_conf = StaticConfFiles.getUnitConf(card.getCardname());
        int unit_id = GameState.getTotalUnits();
        Unit unit = BasicObjectBuilders.loadUnit(unit_conf, unit_id, Unit.class);
        unit.setPositionByTile(tile);

		GameState.modifiyTotalUnits(1);
		
		player.setUnit(unit);

		EffectAnimation effect = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
        BasicCommands.playEffectAnimation(out, effect, tile);
		BasicCommands.drawUnit(out, unit, tile);


		BigCard bigCard = card.getBigCard();
		int attack = bigCard.getAttack();
		int health = bigCard.getHealth();
		Gui.setUnitStats(unit, health, attack);
        unit.setAttack(attack);
        unit.setHealth(health);

		GameState.modifiyTotalUnits(1);
        tile.setOccupier(unit);

		
		Gui.setUnitStats(unit, health, attack);

		int positionInHand = card.getPositionInHand();
		player.removeFromHand(positionInHand);
		BasicCommands.deleteCard(out, positionInHand);
		player.setUnit(unit);
        player.updateMana(-card.getManacost());
		if (GameState.getHumanPlayer() == player){
			BasicCommands.setPlayer1Mana(out, player);
		}
		else {
			BasicCommands.setPlayer2Mana(out, player);
		}


    }

    public static Set<Tile> cardPlacements(Card card, Player player, Player enemy, Tile[][] board){
        if (card.getManacost() > player.getMana()){
             return null;
        }

        Set<Tile> validTiles = new HashSet<Tile>();


        Set<Tile> playerUnits = getPlayerUnitPositions(player, board);
        Set<Tile> enemyUnits = getEnemyUnitPositions(enemy, board);
        /* if card can be played on all squares, return the board - occupied squares */
        if (card.getMoveModifier()){
            validTiles.removeAll(playerUnits);
            validTiles.removeAll(enemyUnits);
            return validTiles;
        }

        int x, y;
        Set<Tile> validPlacements =  new HashSet<Tile>();

        /* Add squares around player units to set. Return this minus occupied squares */
        for (Tile tile : playerUnits){
            x = tile.getTilex();
            y = tile.getTiley();
            for (int i = -1 ; i <= 1 ; i++){
                for (int j = -1 ; j <= 1 ; j++){
                    validPlacements.add(board[x + i][y + j]);
                }
            }
        }
        validPlacements.removeAll(playerUnits);
        validPlacements.removeAll(enemyUnits);
        return validPlacements;
    }
    public static Set<Tile> getPlayerUnitPositions(Player player, Tile[][] board){
        Set<Tile> s = new HashSet<Tile>();
        for (Unit unit : player.getUnits()){
            /* Add unit to set of player positions */
            s.add(board[unit.getPosition().getTilex()][unit.getPosition().getTiley()]);
        }
        return s;

    }
    public static Set<Tile> getEnemyUnitPositions(Player enemy, Tile[][] board){
        Set<Tile> s = new HashSet<Tile>();
		ArrayList<Unit> uList = enemy.getUnits();
        for (Unit unit : uList){
            /* Add unit to set of enemy positions */
            s.add(board[unit.getPosition().getTilex()][unit.getPosition().getTiley()]);
        }
        return s;
    }

    public static boolean validMove(ActorRef out, Card card, Player player, Player enemy, Tile tile, Tile[][] board){
        if (card.getManacost() >= player.getMana()){
            return false;
        }
        Set<Tile> s = cardPlacements(card, player, enemy, board);
        if (s.contains(tile)){
            return true;
        }
        return false;
    }
    
    

    public static Set<Tile> showValidMoves(Card card, Player player, Player enemy, Tile[][] board){
        Set<Tile> s = cardPlacements(card, player, enemy, board);
        return s;
    }
    
    

	public static Set<Tile> determineTargets(Tile tile, Set<Tile> positions, Player enemy, Tile[][] board){
		
		// Using Set so that the Tile Objects do not repeat for the last condition
		Set<Tile> validAttacks = new HashSet<>();
		
		// Has Attacked already
		if (tile.getOccupier().hasAttacked()) {
			return null;
		// Has moved but has not attacked - consider only the current position
		} else if (tile.getOccupier().hasMoved() && !tile.getOccupier().hasAttacked()) {
			return getValidTargets(tile, enemy, board);
		
		// Has not moved nor attacked - consider all possible movements as well. 
		} else if (!tile.getOccupier().hasMoved() && !tile.getOccupier().hasAttacked()) {
			System.out.println("has NOT moved NOR attacked");
			
			for (Tile position : positions) {
				validAttacks.addAll(getValidTargets(position,enemy,board));
			}		
		}
		return validAttacks;
	}
	
	
	
	
	
	public static Set<Tile> getValidTargets(Tile tile, Player enemy, Tile[][] board){
		
		Set<Tile> validAttacks = new HashSet<>();
		
		for (Unit unit : enemy.getUnits()) {
			int unitx = unit.getPosition().getTilex();
			int unity = unit.getPosition().getTiley();
			
			if (Math.abs(unitx - tile.getTilex()) < 2 && Math.abs(unity- tile.getTiley()) < 2 ) {
				validAttacks.add(board[unitx][unity]);
			}
		}
		return validAttacks;
	}

	public static Set<Tile> determineValidMoves(Tile[][] board, Unit unit) {

		Set<Tile> validTiles = new HashSet<>();

		if (!unit.hasMoved() && !unit.hasAttacked()) {
			int x = unit.getPosition().getTilex();
			int y = unit.getPosition().getTiley();
			// check one behind
			int newX = x - 1;
			if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
				validTiles.add(board[newX][y]);
				// if one behind empty, check two behind
				newX = x - 2;
				if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
					validTiles.add(board[newX][y]);
				}
			}
			// check one ahead
			newX = x + 1;
			if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
				validTiles.add(board[newX][y]);
				// if one ahead empty, check two ahead
				newX = x + 2;
				if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
					validTiles.add(board[newX][y]);
				}
			}
			// check one up
			int newY = y - 1;
			if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
				validTiles.add(board[x][newY]);
				// if one up empty, check two up
				newY = y - 2;
				if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
					validTiles.add(board[x][newY]);
				}
			}
			// check one down
			newY = y + 1;
			if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
				validTiles.add(board[x][newY]);
				// if one up empty, check two up
				newY = y + 2;
				if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
					validTiles.add(board[x][newY]);
				}
			}

			// diagonal tiles
			if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1].getOccupier() == null) {
				validTiles.add(board[x + 1][y + 1]);
			}

			if (x - 1 >= 0 && y - 1 >= 0 && board[x - 1][y - 1].getOccupier() == null) {
				validTiles.add(board[x - 1][y - 1]);
			}

			if (x + 1 < board.length && y - 1 >= 0 && board[x + 1][y - 1].getOccupier() == null) {
				validTiles.add(board[x + 1][y - 1]);
			}

			if (x - 1 >= 0 && y + 1 < board[0].length && board[x - 1][y + 1].getOccupier() == null) {
				validTiles.add(board[x - 1][y + 1]);
			}

		} else {
			// cannot move, so what happens? just return empty set?
			return validTiles;
		}
		return validTiles;
	}	

	public static Set<Tile> boardToSet(Tile[][] board){
		Set<Tile> s = new HashSet<Tile>();
		for (Tile[] a : board){
			s.addAll(Arrays.asList(a));
		}
		return s;
	}
	
	
}
