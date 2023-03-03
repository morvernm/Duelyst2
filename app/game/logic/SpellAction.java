package game.logic;

import structures.basic.Tile;
import structures.basic.Unit;

public class SpellAction extends AttackAction{

    // track position of spell card in player's hand so can be removed later.
    int spellCardHandPos;

    public SpellAction(Unit unit, Tile tile){
        super(unit, tile);
    }
}
