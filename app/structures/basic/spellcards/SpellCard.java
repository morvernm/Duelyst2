package structures.basic.spellcards;
import akka.actor.ActorRef;
import structures.basic.*;

public abstract class SpellCard extends Card implements Playable{

    public abstract boolean castSpell(Unit target, Tile targetTile); // perform ability. Report back if successful.

    public abstract void highlightTargets(ActorRef out); // highlight valid targets for a particular spellcard.

    /*
     * Checks if unit has SpellThief by checcking unit id, if so, applies the affect
     */
}
