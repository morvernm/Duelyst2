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

public class EntropicDecay extends SpellCard {

    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        if(target == GameState.getHumanPlayer().getAvatar() ||
        target == GameState.getAIPlayer().getAvatar()) return false; // Players cannot target player avatars.
        // Kill target
        target.setHealth(0);
        Gui.playEffectAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom),targetTile); // play spell animation
        Utility.checkEndGame(target); // check if game has ended / play death animation
        return true;
    }

    @Override
    public void highlightTargets(ActorRef out) {
        ArrayList<Unit> targets = new ArrayList<>();
        // Get all units on board as targets
        targets = GameState.getAIPlayer().getUnits();
        targets.addAll(GameState.getHumanPlayer().getUnits());
        // Remove player avatars from targets (can only target non-avatar units)
        targets.remove(GameState.getAIPlayer().getAvatar());
        targets.remove(GameState.getHumanPlayer().getAvatar());
        // Get tile positions and highlight them
        Set<Tile> positions = Utility.getSpellTargetPositions(targets);
        Gui.highlightTiles(out,positions,2);
    }
}
