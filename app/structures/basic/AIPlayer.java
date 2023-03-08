package structures.basic;

import game.logic.Gui;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.NoSuchElementException;

import akka.actor.ActorRef;
import commands.BasicCommands;

public class AIPlayer extends Player{

    public AIPlayer() {
        super(20, 0, 2);
        // Draw first three cards
        for(int i = 0; i < 3; i++) {
            drawCard();
            
        }

    }

    @Override
    public void setMana(int mana){
        this.mana = Math.min(mana, 9); // cap the max amount of mana a player can have at 9, as per GUI.
        Gui.displayAIMana(this);
    }
    @Override
    public void setHealth(int health){
        this.health = health;
        Gui.displayAIHP(this);
    }
    @Override
    public void drawCard() {
        if (deck.isEmpty()) throw new NoSuchElementException("Deck is empty");
        if (cardsInHand == 6) { // if no space in hand, card is lost
            deck.drawTopCard();
            return;
        }
        // if is space in hand, find first free spot in hand and place card from deck in it.
        int i = 0;
        while(hand[i] != null && i < hand.length - 1) {
            i++;
        }
        Card current = deck.drawTopCard();
        current.setPositionInHand(i+1);

        hand[i] = current;
        cardsInHand++;
    }
    public void createAvatar(ActorRef out) {
		Unit enemyAvatar = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 101, Unit.class);
		enemyAvatar.setPositionByTile(GameState.board[5][2]); 
		GameState.board[5][2].setOccupier(enemyAvatar);
		BasicCommands.drawUnit(out, enemyAvatar, GameState.board[5][2]);
		Gui.setUnitStats(enemyAvatar, 20, 2);
		enemyAvatar.setHealth(20);
		enemyAvatar.setAttack(2);
		GameState.modifiyTotalUnits(1);
		GameState.enemy.setUnit(enemyAvatar);
    }
//    public Card[] getHand() {
//    	return this.hand;
//    }
}
