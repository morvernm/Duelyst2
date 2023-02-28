package structures.basic.spellcards;

import akka.actor.ActorRef;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;

import java.util.ArrayList;
import java.util.Set;


public class Ykir extends SpellCard {

	@Override
	public boolean castSpell(Unit target, Tile targetTile) {
		if (GameState.getCurrentPlayer() == GameState.getHumanPlayer()) {
			if (target.getId() != 100)
				return false;
		} else {
			if (target.getId() != 101)
				return false;
		}
			
		
		target.setAttack(target.getAttack() + 2);
		
        Gui.setUnitStats(target, target.getHealth(), target.getAttack());
        Gui.playEffectAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), targetTile);
        return true;

	}
	@Override
	public void highlightTargets(ActorRef out) {
		// TODO Auto-generated method stub
		ArrayList<Unit> targets = new ArrayList<>();
		
		Unit avatar = null;
        
        for (Unit unit : GameState.getCurrentPlayer().getUnits()) {
        	if (unit.getId() == 100 || unit.getId() == 101) {
        		avatar = unit;
        	}
        }
        targets.add(avatar);
        Set<Tile> positions = Utility.getSpellTargetPositions(targets);
        Gui.highlightTiles(out, positions, 2);
	}

}
