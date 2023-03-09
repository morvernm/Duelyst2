package structures.basic.SpecialUnits;

import structures.GameState;

import structures.basic.Unit;

import java.util.Set;

public class BlazeHound extends Unit {

    private final String name = "Blaze Hound";

    
    public static void specialAbility() {
        GameState.getHumanPlayer().drawCard();
        GameState.getAIPlayer().drawCard();
    }
}
