package structures;

import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import Arrays;

public class Utility {
    public Set<Tile> cardPlacements(UnitCard card, Player player, Player enemy, Tile[][] board){
        if (card.getManacost() > player.getMana()){
            return null;
        }

        Set<Tile> validTiles = new HashSet<Tile>(Arrays.asList(board));
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
        for (Unit unit : playerUnits){
            x = unit.getPosition().getTilex();
            y = unit.getPosition().getTiley();
            for (int i = -1 ; i <= 1 ; i++){
                for (int j = -1 ; j <= 1 ; j++){
                    validPlacements.add(board[x + i]y + j);
                }
            }
        }
        validPlacements.removeAll(playerUnits);
        validPlacements.removeAll(enemyUnits);
        return validPlacements;
    }

    public Set<Tile> cardPlacements(SpellCard card, Player player, Player enemy, Tile[][] board)

    public Set<Tile> getPlayerUnitPositions(Player player, Tile[][] board){
        Set<Tile> s = new HashSet<Tile>();
        for (Unit unit : player.getUnits()){
            /* Add unit to set of player positions */
            s.add(board[unit.getPosition().getTilex()][unit.getPosition().getTiley()]);
        }
        return s;

    }

    public Set<Tile> getEnemyUnitPositions(Player enemy, Tile[][] board){
        Set<Tile> s = new HashSet<Tile>();
        for (Unit unit : enemy.getUnits()){
            /* Add unit to set of enemy positions */
            s.add(board[unit.getPosition().getTilex()][unit.getPosition().getTiley()]);
        }
        return s;
    }

    public boolean validMove(Card card, Player player, Player enemy, Tile tile, Tile[][] board){
        if (card.getManacost() > player.getMana()){
            return false;
        }
        Set<Tile> s = cardPlacements(card, player, enemy, board);
        if (s.contains(tile)){
            return true;
        }
        return false;
    }

    public Set<Tile> showValidMoves(UnitCard card, Player player, Player enemy, Tile[][] board){
        Set<Tile> s = unitCardPlacements(card, player, enemy, board);
        return s;
    }
    
    public Set<Tile> showValidMoves(SpellCard card, Player player, Player enemy, Tile[][] board){
        int modifier = card.getMoveModifier()

        switch (modifier){

        }
    }


}
