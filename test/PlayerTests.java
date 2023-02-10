import structures.basic.Player;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PlayerTests {

    @Test
    public void deckCreationTest(){
        Player player = new Player();
        assertTrue(player.getDeckSize() == 17);
    }
    @Test
    public void drawCard() {
        Player player = new Player();
        player.drawCard();
        assertTrue(player.getCard(1).getCardname().equals("Comodo Charger"));
        assertTrue((player.getDeckSize() == 16));
    }

    @Test
    public void drawEmptyDeck(){
        Player player = new Player();
        int i = 0;
        while(i < 17) {
            player.drawCard();
            player.removeFromHand(1);
            i++;
        }
        assertThrows(NoSuchElementException.class, player::drawCard);
    }
}
