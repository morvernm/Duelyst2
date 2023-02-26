package structures.basic.spellcards;

import akka.actor.ActorRef;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.Set;

public class Truestrike extends SpellCard {


    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        System.out.println(target.getHealth() - 2);
        target.setHealth(Math.max(target.getHealth() - 2, 0)); // else, perform spell and return true. Capped so doesn't go below 0.
        Gui.playEffectAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), targetTile);
        Utility.checkEndGame(target);
        return true;
    }

    @Override
    public void highlightTargets(ActorRef out) {
        // Get all units on board as targets
        ArrayList<Unit> units;
        units = GameState.getHumanPlayer().getUnits();
        units.addAll(GameState.getAIPlayer().getUnits());
        Set<Tile> positions = Utility.getSpellTargetPositions(units);
        Gui.highlightTiles(out,positions,2);
    }
}
