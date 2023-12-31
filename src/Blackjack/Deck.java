package Blackjack;
import java.util.ArrayList;
import java.util.Collections;

// OOPs Concepts: ArrayLists (Collections Framework) and Encapsulation

public class Deck {
    private ArrayList<Card> deck; // stores all the 52 cards.
    public Deck() {
        deck = new ArrayList<Card>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                if (j == 0) {
                    // we create card with ith suit and jth rank which has the value of 11 (Ace).
                    Card card = new Card(i, j, 11);
                    deck.add(card);
                }
                else if (j >= 10) {
                    // we create card with the ith suit and jth rank which has the value of 10 (Jack, Queen, King).
                    Card card = new Card(i, j, 10);
                    deck.add(card);
                }
                else {
                    Card card = new Card(i, j, j+1);
                    deck.add(card);
                }
            }
        }
    }

    public void shuffleDeck() { Collections.shuffle(deck);}
    public Card getCard(int i) { return deck.get(i);}
    public Card removeCard(int i) {return deck.remove(i);}
}