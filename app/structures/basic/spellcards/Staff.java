package structures.basic.spellcards;

import akka.actor.ActorRef;
import structures.basic.BigCard;
import structures.basic.MiniCard;
import structures.basic.Tile;
import structures.basic.Unit;

public class Staff extends SpellCard {


    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        return false;
    }

    @Override
    public void highlightTargets(ActorRef out) {

    }
}
