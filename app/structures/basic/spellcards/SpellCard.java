package structures.basic.spellcards;
import structures.basic.*;

public abstract class SpellCard {

    public abstract boolean castSpell(Unit target, Tile targetTile); // perform ability. Report back if successful.

}
