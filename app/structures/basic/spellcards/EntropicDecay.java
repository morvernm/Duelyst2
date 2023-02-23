package structures.basic.spellcards;

import akka.actor.ActorRef;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.*;

import java.util.ArrayList;
import java.util.Set;

public class EntropicDecay extends SpellCard {

    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        if(target == GameState.getHumanPlayer().getAvatar() ||
        target == GameState.getAIPlayer().getAvatar()) return false; // Players cannot target player avatars.
        if(GameState.getCurrentPlayer().getUnits().contains(target)) return false; // 'Friendly fire will not be tolerated!'
        // Kill target
        target.setHealth(0);
        return true;
    }

    @Override
    public void highlightTargets(ActorRef out) {
        ArrayList<Unit> targets = new ArrayList<>();
        Player enemyPlayer = null;
        // If human player casting, get AI player's units. Else, get human player's units
        if(GameState.getCurrentPlayer() == GameState.getHumanPlayer()) {
            targets = GameState.getAIPlayer().getUnits();
            enemyPlayer = GameState.getAIPlayer();
        }else{
            targets = GameState.getHumanPlayer().getUnits();
            enemyPlayer = GameState.getHumanPlayer();
        }
        // Remove player avatar from targets
        targets.remove(enemyPlayer.getAvatar());
        // Get tile positions and highlight them
        Set<Tile> positions = Utility.getSpellTargetPositions(targets);
        Gui.highlightTiles(out,positions,2);
    }
}
