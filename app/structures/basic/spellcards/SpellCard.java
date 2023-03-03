package structures.basic.spellcards;
import akka.actor.ActorRef;
import structures.basic.*;

import java.util.ArrayList;

public abstract class SpellCard extends Card implements Playable{

    public abstract boolean castSpell(Unit target, Tile targetTile); // perform ability. Report back if successful.

    public abstract void highlightTargets(ActorRef out); // highlight valid targets for a particular spellcard.

    public abstract ArrayList<Unit> getTargets(); // get targets of this card

}
