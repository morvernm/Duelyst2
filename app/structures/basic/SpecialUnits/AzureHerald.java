package structures.basic.SpecialUnits;

import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;

import java.util.Set;

public class AzureHerald extends Unit {

    private final String name = "Azure Herald";

    //constructor ??
    public AzureHerald() {
        super();
        specialAbility(GameState.getHumanPlayer().getUnits().get(0));
    }

    public static void specialAbility(Unit avatar) {
        avatar.setHealth(Math.min(avatar.getHealth() + 3, 20));
    }
}
