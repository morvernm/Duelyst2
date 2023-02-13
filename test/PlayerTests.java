import structures.basic.Player;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class PlayerTests {

    @Test
    public void deckCreationTest(){
        Player player = new Player();
        assertTrue(player.getDeck().getDeckSize() == 20);
    }
    @Test
    public void drawCard() {
        Player player = new Player();
        player.drawCard();
        assertTrue(player.getCard(1).getCardname().equals("Comodo Charger"));
        assertTrue((player.getDeck().getDeckSize() == 19));
    }

    @Test
    public void drawEmptyDeck(){
        Player player = new Player();
        int i = 0;
        while(i < 20) {
            player.drawCard();
            player.removeFromHand(1);
            i++;
        }
        assertThrows(NoSuchElementException.class, player::drawCard);
    }

    @Test
    public void overDraw() {
        Player player = new Player();
        int i = 0;
        while(i < 6) {
            player.drawCard();
            i++;
        }
        assertEquals("Ironcliff Guardian", player.getDeck().drawTopCard().getCardname());
    }
}
