package structures.basic.spellcards;

import akka.actor.ActorRef;
import game.logic.Gui;
import structures.GameState;
import structures.basic.BigCard;
import structures.basic.MiniCard;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;

public class EntropicDecay extends SpellCard {

    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        this.handleSpellThief();
        return false;
    }

    @Override
    public void highlightTargets(ActorRef out) {

    }

    @Override
    public void handleSpellThief(){
        Player enemy = GameState.getAIPlayer();
        for (Unit unit : enemy.getUnits()){
            if (unit.getId() == 13 || unit.getId() == 1){
                unit.modAttack(1);
                unit.modHealth(1);
                Gui.setUnitStats(unit, unit.getHealth(), unit.getAttack());
                return;
            }
        }
    }
}
