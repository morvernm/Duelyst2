package structures.basic.spellcards;

import game.logic.Gui;
import structures.GameState;
import structures.basic.BigCard;
import structures.basic.MiniCard;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Sundrop extends SpellCard {


    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        // Check the player owns this unit. This spell card can only be applied to friendlies
        if(!GameState.getCurrentPlayer().getUnits().contains(target)) return false;

        // Add health to the unit. Cap so the new value doesn't exceed the unit's max health
        // TODO Cap so the new value doesn't exceed the unit's max health// will become easier once unit cards implemented
        target.setHealth(Math.min(target.getHealth() + 5, target.getMaxHealth()));
        Gui.playEffectAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff),targetTile);
        return true;
    }
}
