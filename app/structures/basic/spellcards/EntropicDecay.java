package structures.basic.spellcards;

import akka.actor.ActorRef;
import game.logic.Gui;
import structures.GameState;
import structures.basic.BigCard;
import structures.basic.MiniCard;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.SpecialUnits.*;

public class EntropicDecay extends SpellCard {

    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        this.handleSpellThief();
        return false;
    }

    @Override
    public void highlightTargets(ActorRef out) {

    }

    /*
     * Checks if unit has SpellThief by checking unit id, if so, applies the affect
     */
    @Override
    public void handleSpellThief(){
        Player enemy = GameState.getHumanPlayer();
        for (Unit unit : enemy.getUnits()){
            if (unit.getClass().equals(Pureblade.class)){
                Pureblade p = (Pureblade)unit;
                p.specialAbility();
                return;
            }
        }
    }
}
