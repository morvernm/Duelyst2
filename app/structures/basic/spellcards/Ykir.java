package structures.basic.spellcards;

import akka.actor.ActorRef;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.Set;

public class Ykir extends SpellCard{
    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        if(target != GameState.getCurrentPlayer().getAvatar()) return false; // This card must be played only on the player's own avatar.
        target.setAttack(target.getAttack() + 2);
        Gui.playEffectAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), targetTile);
        return true;
    }

    @Override
    public void highlightTargets(ActorRef out) {
        ArrayList<Unit> targets = new ArrayList<>();
        targets.add(GameState.getCurrentPlayer().getAvatar());
        Set<Tile> positions = Utility.getSpellTargetPositions(targets);
        Gui.highlightTiles(out, positions, 2);
    }
}
