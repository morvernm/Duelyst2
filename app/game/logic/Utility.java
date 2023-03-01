package game.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


import akka.actor.ActorRef;
import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
//import akka.parboiled2.Position;
import commands.BasicCommands;
import events.CardClicked;
import structures.GameState;

import structures.basic.Player;

import structures.basic.SpecialUnits.Provoke;
import structures.basic.SpecialUnits.SilverguardKnight;
import structures.basic.SpecialUnits.*;

import structures.basic.SpecialUnits.FireSpitter;
import structures.basic.SpecialUnits.Pyromancer;
import structures.basic.SpecialUnits.RangedAttack;

import structures.basic.SpecialUnits.Windshrike;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.Position;

import structures.basic.UnitAnimationType;

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
    private static ActorRef out;

    public Utility(ActorRef out) {
        Utility.out = out;
    }

	public static Set<Tile> determineTargets(Tile tile, Set<Tile> positions, Player enemy, Tile[][] board) {

        // Using Set so that the Tile Objects do not repeat for the last condition
        Set<Tile> validAttacks = new HashSet<>();

        if (tile.getOccupier() instanceof RangedAttack) {
            System.out.println("Determine Target - Ranged Attack");
            if (tile.getOccupier().hasAttacked()) {
                return null;
            } else if (!tile.getOccupier().hasAttacked()) {
                return RangedAttack.specialAbility(board);
            }
        }
        
        if (positions == null && !checkProvoker(tile).isEmpty()){
        	Set<Position> p = checkProvoker(tile);
        	for (Position pos : p)
        		validAttacks.add(board[pos.getTilex()][pos.getTiley()]);
        	return validAttacks;
        }
        

        // Has Attacked already
        if (tile.getOccupier().hasAttacked()) {
            return null;
            
        // Has moved but has not attacked - consider only the current position
        } else if (tile.getOccupier().hasMoved() && !tile.getOccupier().hasAttacked()) {
        	validAttacks = getValidTargets(tile, enemy, board);
        	
        // Has not moved nor attacked - consider all possible movements as well.
        } else if (!tile.getOccupier().hasMoved() && !tile.getOccupier().hasAttacked()) {
            System.out.println("has NOT moved NOR attacked");

            for (Tile position : positions) {
                validAttacks.addAll(getValidTargets(position, enemy, board));
            }
        }
        return validAttacks;
    }
    
    public static Set<Position> checkProvoker(Tile tile) {
    	
    	Set<Position> provoker = new HashSet<>();
     	
    	for (Unit unit : GameState.getOtherPlayer().getUnits()) {
        	
        	int tilex = tile.getTilex();
    		int tiley = tile.getTiley();
   
    		if (Math.abs(tilex - unit.getPosition().getTilex()) < 2 && Math.abs(tiley - unit.getPosition().getTiley()) < 2) {
    			if (unit instanceof Provoke) {
    				System.out.println("Provoker in the house");
    				provoker.add(unit.getPosition());
    			}	
    		}	
        }
		return provoker;
    }
    

    public static Set<Tile> getValidTargets(Tile tile, Player enemy, Tile[][] board) {

        Set<Tile> validAttacks = new HashSet<>();
        
        Set<Position> provoker = checkProvoker(tile);
        if (!provoker.isEmpty()) {
        	for (Position pos : provoker) {
        		validAttacks.add(board[pos.getTilex()][pos.getTiley()]);
        	}
        	return validAttacks;
        }
        	 

        for (Unit unit : enemy.getUnits()) {
            int unitx = unit.getPosition().getTilex();
            int unity = unit.getPosition().getTiley();

            if (Math.abs(unitx - tile.getTilex()) < 2 && Math.abs(unity - tile.getTiley()) < 2) {
                validAttacks.add(board[unitx][unity]);
            }
        } 
        
//        for (Tile t : validAttacks) {
//        	System.out.println("GVT -> x = " + tile.getTilex() + " x = " + tile.getTiley());
//        }      
        
        return validAttacks;
    }

    
   
    /*
     * Attacking logic
     */
	public static void adjacentAttack(Unit attacker, Unit defender) {
	
		if (!attacker.hasAttacked()) {
						
			Gui.performAttack(attacker);
			
			BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.hit);
			try {Thread.sleep(750);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.idle);
			BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.idle);
			
			defender.setHealth(defender.getHealth()-attacker.getAttack());
			
			// check if silverguard Knight, one or more, are on the board
			checkSilverKnight(defender);
			
			Gui.setUnitStats(defender, defender.getHealth(), defender.getAttack());
			
			attacker.setAttacked(); // commented out to test that unit dies
			
			checkEndGame(defender);
			counterAttack(attacker, defender);
		}
	}
	
	/**
	 * Checks if a SilverGuard Knight(SK) is present on the board;
	 * If the damdaged unit is the Human's avatar then buff all friendly SK units
	 * 
	 * @param unit
	 */
	public static void checkSilverKnight(Unit defender) {
		if(defender.getId() == 100) {
			for (Unit  unit: GameState.getCurrentPlayer().getUnits()) {
				if (unit instanceof SilverguardKnight && GameState.getHumanPlayer().getUnits().contains(unit)) {
					((SilverguardKnight) unit).buffAttack();
				}
			}
		}
	}
	
	public static void distancedAttack(Unit attacker, Unit defender, Player enemy) {
        System.out.println("Distanced Attack Activated");

        if (!attacker.hasAttacked() && attacker instanceof RangedAttack) {
            Gui.performAttack(attacker);
            BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.idle);
            
            EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
            //try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
    		BasicCommands.playProjectileAnimation(out, projectile, 0, GameState.getBoard()[attacker.getPosition().getTilex()][attacker.getPosition().getTiley()], GameState.getBoard()[defender.getPosition().getTilex()][defender.getPosition().getTiley()]);
    		
    		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
    		BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.hit);
			try {Thread.sleep(750);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.idle);
			BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.idle);
			
			// check if silverguard Knight, one or more, are on the board
			checkSilverKnight(defender);
			
            defender.setHealth(defender.getHealth()-attacker.getAttack());
            Gui.setUnitStats(defender, defender.getHealth(), defender.getAttack());
            
            
            
            attacker.setAttacked(); // commented out to test that unit dies

            checkEndGame(defender);
            counterAttack(attacker, defender);
        }

        if (!attacker.hasAttacked() && !attacker.hasMoved()) {

            // Get the valid tiles from which the unit can attack
            ArrayList<Tile> validTiles = getValidAttackTiles(defender);

            int minScore = Integer.MAX_VALUE;
            Tile closestTile = null;

            // Find the closest/optimal position to attack from by scoring each option
            for (Tile tile : validTiles) {
                int score = 0;
                score += Math.abs(tile.getTilex() - attacker.getPosition().getTilex());
                score += Math.abs(tile.getTiley() - attacker.getPosition().getTiley());
                if (score <= minScore && tile.getOccupier() == null) {
                    minScore = score;
                    closestTile = tile;
                }

            }

            // move unit to the closest tile
            if (closestTile != null) {

                System.out.println("The closest tile is: x = " + closestTile.getTilex() + " and y = " + closestTile.getTiley() + " score " + minScore);

                moveUnit(attacker, closestTile);
                if (minScore < 2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                adjacentAttack(attacker, defender);

            }
        }
    }
	
	 /*
     * Gets the valid attack positions for distanced attacks (move first and then attack)
     */

    
	public static ArrayList<Tile> getValidAttackTiles(Unit unit) {
        ArrayList<Tile> validTiles = new ArrayList<>();

        for (Tile tile : GameState.validMoves) {
            int unitx = unit.getPosition().getTilex();
            int unity = unit.getPosition().getTiley();
            if (Math.abs(unitx - tile.getTilex()) < 2 && Math.abs(unity - tile.getTiley()) < 2) {
                validTiles.add(tile);
            }

        }

        for (Tile tile : validTiles) {
            System.out.println("tile: x = " + tile.getTilex() + " and y = " + tile.getTiley());
        }

        return validTiles;
    }

	
	public static void placeUnit(ActorRef out, Card card, Player player, Tile tile){
		System.out.println("placeUnit Utility");
		
		/* Set unit id to number of total units on board + 1 */
        String unit_conf = StaticConfFiles.getUnitConf(card.getCardname());
        int unit_id = GameState.getTotalUnits();
        
        Unit unit = null;

        
        if (card.getCardname().equals("Silverguard Knight")) {
        	unit = (SilverguardKnight) BasicObjectBuilders.loadUnit(unit_conf, unit_id, SilverguardKnight.class);
        } else if (card.getCardname().equals("Fire Spitter")) {
            unit = (FireSpitter) BasicObjectBuilders.loadUnit(unit_conf, unit_id, FireSpitter.class);
        } else if (card.getCardname().equals("Pyromancer")) {
            unit = (Pyromancer) BasicObjectBuilders.loadUnit(unit_conf, unit_id, Pyromancer.class);
        } else if (card.getCardname().equals("Serpenti")) {
        	unit = (Serpenti) BasicObjectBuilders.loadUnit(unit_conf, unit_id, Serpenti.class);
        } else if (card.getCardname().equals("Azurite Lion"))  {
        	unit = (AzuriteLion) BasicObjectBuilders.loadUnit(unit_conf, unit_id, AzuriteLion.class);
        } else {
            unit = BasicObjectBuilders.loadUnit(unit_conf, unit_id, Unit.class);
        }

        unit.setPositionByTile(tile);
        tile.setOccupier(unit);
		GameState.modifiyTotalUnits(1);
		
		//player.setUnit(unit);

		EffectAnimation effect = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
        BasicCommands.playEffectAnimation(out, effect, tile);
		BasicCommands.drawUnit(out, unit, tile);
		player.setUnit(unit);

		BigCard bigCard = card.getBigCard();
		
		int attack = bigCard.getAttack();
		int health = bigCard.getHealth();
		unit.setMaxHealth(health);
		
		//Gui.setUnitStats(unit, health, attack);
        unit.setAttack(attack);
        unit.setHealth(health);

		GameState.modifiyTotalUnits(1);

		Gui.setUnitStats(unit, health, attack);

		int positionInHand = card.getPositionInHand();
		player.removeFromHand(positionInHand);
		BasicCommands.deleteCard(out, positionInHand);
		
		
        player.updateMana(-card.getManacost());
        CardClicked.currentlyHighlighted.remove(card);
        
		if (GameState.getHumanPlayer() == player){
			BasicCommands.setPlayer1Mana(out, player);
		}
		else {
			BasicCommands.setPlayer2Mana(out, player);
		}


    }

    public static Set<Tile> cardPlacements(Card card, Player player, Player enemy, Tile[][] board){
//        if (card.getManacost() > player.getMana()){
//             return null;
//        }
    	System.out.println("cardPlacement Utility");
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
                	if (x + i > 8 || y +j > 4 || x + i <0 || y +j<0)
                		continue;
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
        if (card.getManacost() > player.getMana()){
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
    
    

	public static void checkEndGame(Unit defender) {
		//unit death
		if(defender.getHealth() <= 0) {
			BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.death);
			try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
			GameState.board[defender.getPosition().getTilex()][defender.getPosition().getTiley()].setOccupier(null); //remove unit from tiles
			BasicCommands.deleteUnit(out, defender); //delete unit from board
			
//		AI unit
			if(GameState.getAiPlayer().getUnits().contains(defender)) {
				GameState.getAiPlayer().removeUnit(defender); 
				
				//GameState.getAiPlayer().setHealth(0); //for testing purposes <= DOES THIS NEED TO GO???
				
				if(GameState.getAiPlayer().getHealth() <= 0) {
					BasicCommands.addPlayer1Notification(out, "Player 1 wins!", 20);
					//game over:
				}
				
//		Human unit
			}else if(GameState.getHumanPlayer().getUnits().contains(defender)) {
				GameState.getHumanPlayer().removeUnit(defender);
				if(GameState.getHumanPlayer().getHealth() <= 0) {
					BasicCommands.addPlayer1Notification(out, "You lose!", 20);
				}
			}	
		}
	}
	

    public static void moveUnit(Unit unit, Tile tile) {

	    if(!unit.hasMoved() && !unit.hasAttacked()) {
	        GameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setOccupier(null); //clear unit from tile
	
	        BasicCommands.moveUnitToTile(out, unit, tile); //move unit to chosen tiles
	        unit.setPositionByTile(tile); //change position of unit to new tiles
	
	        tile.setOccupier(unit); //set unit as occupier of tiles
	
	        unit.setMoved();
	        
	        Gui.removeHighlightTiles(out, GameState.board); //clearing board
	    } else {
	    	BasicCommands.addPlayer1Notification(out, "Unit cannot move again this turn", 2);
	    }

    }
    
    public static boolean checkProvoked(Unit unit) {
		for (Unit other : GameState.getOtherPlayer().getUnits()) {
		        	
        	int unitx = unit.getPosition().getTilex();
    		int unity = unit.getPosition().getTiley();
    	
    		if (Math.abs(unitx - other.getPosition().getTilex()) < 2 && Math.abs(unity - other.getPosition().getTiley()) < 2) {
    			if (other instanceof Provoke) {
    				((Provoke) other).disableUnit(unit);
    				System.out.println("Unit is provoked!");
    				return true;
    			}	
    		}	
        }
		return false;
    }
    

    public static Set<Tile> determineValidMoves(Tile[][] board, Unit unit) {

        Set<Tile> validTiles = new HashSet<>();
        /*
         * Check for provoke units
         */
        if (checkProvoked(unit))
        	return null;   

        if (unit.getClass().equals(Windshrike.class) && !unit.hasMoved() && !unit.hasAttacked()) {
            return ((Windshrike) unit).specialAbility(board);
    
        } else if (!unit.hasMoved() && !unit.hasAttacked()) {
            int x = unit.getPosition().getTilex();
            int y = unit.getPosition().getTiley();

            // check one behind
            int newX = x - 1;
            if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
                validTiles.add(board[newX][y]);
            }
            // if the nearby unit is a friendly unit, check the tile behind the friendly unit
            if (GameState.getCurrentPlayer().getUnits().contains(board[newX][y].getOccupier()) || board[newX][y].getOccupier() == null) {
                newX = x - 2;
                if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
                    validTiles.add(board[newX][y]);
                }
            }

            // check one ahead
            newX = x + 1;
            if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
                validTiles.add(board[newX][y]);
            }
            // if one ahead is a friendly unit, check the tile ahead of the friendly unit
            if (GameState.getCurrentPlayer().getUnits().contains(board[newX][y].getOccupier()) || board[newX][y].getOccupier() == null) {
                newX = x + 2;
                if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
                    validTiles.add(board[newX][y]);
                }
            }

            // check one up
            int newY = y - 1;
            if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
                validTiles.add(board[x][newY]);
            }
            // if one up a friendly unit, check two up
            if (GameState.getCurrentPlayer().getUnits().contains(board[x][newY].getOccupier()) || board[x][newY].getOccupier() == null) {
                newY = y - 2;
                if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
                    validTiles.add(board[x][newY]);
                }
            }

            // check one down
            newY = y + 1;
            if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
                validTiles.add(board[x][newY]);
            }
            // if one up a friendly unit, check two up
            if (GameState.getCurrentPlayer().getUnits().contains(board[x][newY].getOccupier()) || board[x][newY].getOccupier() == null) {
                newY = y + 2;
                if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
                    validTiles.add(board[x][newY]);
                }
            }

            // diagonal tiles
            if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1].getOccupier() == null) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x+1][y].getOccupier()) || board[x+1][y].getOccupier() == null) {
                    validTiles.add(board[x + 1][y + 1]);
                } else if (GameState.getCurrentPlayer().getUnits().contains(board[x][y+1].getOccupier()) || board[x][y+1].getOccupier() == null) {
                    validTiles.add(board[x + 1][y + 1]);
                }
            }

            if (x - 1 >= 0 && y - 1 >= 0 && board[x - 1][y - 1].getOccupier() == null) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x-1][y].getOccupier()) || board[x-1][y].getOccupier() == null) {
                    validTiles.add(board[x - 1][y - 1]);
                } else if (GameState.getCurrentPlayer().getUnits().contains(board[x][y-1].getOccupier()) || board[x][y-1].getOccupier() == null) {
                    validTiles.add(board[x - 1][y - 1]);
                }
            }

            if (x + 1 < board.length && y - 1 >= 0 && board[x + 1][y - 1].getOccupier() == null) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x+1][y].getOccupier()) || board[x+1][y].getOccupier() == null) {
                    validTiles.add(board[x + 1][y - 1]);
                } else if (GameState.getCurrentPlayer().getUnits().contains(board[x][y-1].getOccupier()) || board[x][y-1].getOccupier() == null) {
                    validTiles.add(board[x + 1][y - 1]);
                }
            }

            if (x - 1 >= 0 && y + 1 < board[0].length && board[x - 1][y + 1].getOccupier() == null) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x-1][y].getOccupier()) || board[x-1][y].getOccupier() == null) {
                    validTiles.add(board[x - 1][y + 1]);
                } else if (GameState.getCurrentPlayer().getUnits().contains(board[x][y+1].getOccupier()) || board[x][y+1].getOccupier() == null) {
                    validTiles.add(board[x - 1][y + 1]);
                }
            }

        } else {
            // cannot move, return empty set
            return validTiles;
        }
        return validTiles;
    }

    public static void counterAttack(Unit attacker, Unit countAttacker) {
        int x = countAttacker.getPosition().getTilex();
        int y = countAttacker.getPosition().getTiley();

        int range = 1;

        if (countAttacker.getHealth() > 0) {
            for (int i = Math.max(0, x - range); i <= Math.min(GameState.board.length - 1, x + range); i++) {
                for (int j = Math.max(0, y - range); j <= Math.min(GameState.board[0].length - 1, y + range); j++) {
                    if (i == x && j == y) {
                        continue; // this is where the unit (countAttacker) is
                    } else if (attacker.getPosition().getTilex() == i & attacker.getPosition().getTiley() == j) {
                        //adjacentAttack(countAttacker, attacker);
                    	Gui.performAttack(countAttacker);
                    	
                        //attacker.setAttacked(); // - believe we dont need this
                    	
            			BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.hit);
            			BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.idle);
            			try {Thread.sleep(750);} catch (InterruptedException e) {e.printStackTrace();}
						BasicCommands.playUnitAnimation(out, countAttacker, UnitAnimationType.idle);
						
						
						// check if silverguard Knight, one or more, are on the board
						checkSilverKnight(attacker);
						
                        int newHealth = attacker.getHealth() - countAttacker.getAttack();
                        attacker.setHealth(newHealth);
                        Gui.setUnitStats(attacker, attacker.getHealth(), attacker.getAttack());
                        
                        checkEndGame(attacker);
                    	
                    }
                }
            }
        }
    }

	// Get positions of potential targets of a spell.
	public static Set<Tile> getSpellTargetPositions(ArrayList<Unit> targets) {
		Set<Tile> positions = new HashSet<>();

		for (Unit unit : targets) {
			int unitx = unit.getPosition().getTilex();
			int unity = unit.getPosition().getTiley();
			positions.add(GameState.getBoard()[unitx][unity]);
		}
		return positions;
	}

	public static Set<Tile> boardToSet(Tile[][] board){
		Set<Tile> s = new HashSet<Tile>();
		for (Tile[] a : board){
			s.addAll(Arrays.asList(a));
		}
		return s;
	}

}
