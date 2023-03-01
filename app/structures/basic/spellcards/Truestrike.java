package structures.basic.spellcards;

import akka.actor.ActorRef;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.*;
import structures.basic.SpecialUnits.Pureblade;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.Set;

public class Truestrike extends SpellCard {


    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        if (GameState.getCurrentPlayer().getUnits().contains(target)) {
            return false; // return false for friendly fire.
        }
        this.handleSpellThief();
        System.out.println(target.getHealth() - 2);
        target.setHealth(Math.max(target.getHealth() - 2, 0)); // else, perform spell and return true. Capped so doesn't go below 0.
        Gui.playEffectAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), targetTile);
        Utility.checkEndGame(target);
        return true;
    }

    @Override
    public void highlightTargets(ActorRef out) {
        ArrayList<Unit> units;
        if(GameState.getCurrentPlayer() == GameState.getAIPlayer()) {
            units = GameState.getHumanPlayer().getUnits();
        }
        else {
            units = GameState.getAIPlayer().getUnits();
        }
        Set<Tile> positions = Utility.getSpellTargetPositions(units);
        Gui.highlightTiles(out,positions,2);
    }

    /*
     * Checks if unit has SpellThief by checcking unit id, if so, applies the affect
     */

    @Override
    public void handleSpellThief(){
        return;
    }
}
