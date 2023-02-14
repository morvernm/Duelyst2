package structures.basic.spellcards;
import structures.basic.BigCard;
import structures.basic.Card;
import structures.basic.MiniCard;

public abstract class SpellCard extends Card {

    public SpellCard(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
        super(id, cardname, manacost, miniCard, bigCard);
    }

    public void ability() {
        /* Put spell abilities here. */
    }

}
