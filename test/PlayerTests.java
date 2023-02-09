import structures.basic.Player;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PlayerTests {

    @Test
    public void deckCreationTest(){
        Player player = new Player();
        assertTrue(player.getDeckSize() == 20);
    }
    @Test
    public void drawCard() {
        Player player = new Player();
        player.drawCard();
        assertTrue(player.getHandCard(1).getCardname().equals("Comodo Charger"));
        System.out.println(player.getHandCard(1).getCardname());
        assertTrue((player.getDeckSize() == 19));
    }
}
