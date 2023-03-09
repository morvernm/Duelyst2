package structures.basic.SpecialUnits;

import structures.GameState;

import structures.basic.Unit;

public class AzureHerald extends Unit {

    private final String name = "Azure Herald";
    
    public static void specialAbility(Unit avatar) {
        avatar.setHealth(Math.min(avatar.getHealth() + 3, 20));
    }
}
