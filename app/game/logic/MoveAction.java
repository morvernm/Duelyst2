package game.logic;

import structures.basic.Tile;
import structures.basic.Unit;

public class MoveAction {

    public Unit attacker;
    public Tile moveToTile;
    public Integer value;


    public MoveAction(Unit unit, Tile moveTile) {
        this.attacker = unit;
        this.moveToTile = moveTile;
        this.value = -1;
    }
}
