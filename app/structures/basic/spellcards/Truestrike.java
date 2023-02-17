package structures.basic.spellcards;

import game.logic.Gui;
import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Truestrike extends SpellCard {


    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        if (GameState.getCurrentPlayer().getUnits().contains(target)) {
            return false; // return false for friendly fire.
        }

        target.setHealth(target.getHealth() - 2); // else, perform spell and return true.
        Gui.setUnitHealth(target, target.getHealth() - 2);
        Gui.playEffectAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), targetTile);
        return true;
    }
}
