package structures.basic;

public class SpellCard extends Card{
	public static int type;
	
	
	public SpellCard(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
		super(id, cardname, manacost, miniCard, bigCard);
		type = 3;
	}
	
	private void ability() {
		// TO DO include code here
	}

}
