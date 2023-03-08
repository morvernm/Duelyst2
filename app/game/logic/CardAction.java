package game.logic;

import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;

public class CardAction extends AttackAction{
	private Card card;

	public CardAction(Unit unit, Tile tile) {
		super(unit, tile);
	}
	public CardAction(Card card) {
		this.card = card;
	}
	public Card getCard()  {
		return this.card;
	}
}
