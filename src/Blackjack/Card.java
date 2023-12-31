package Blackjack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics2D;

// OOPS concepts utilised:
// Encapsulation: Private instance variables (suit, rank, value, xPosition, yPosition) are used to encapsulate
//                the state of a Card object. Public methods (getSuit(), getRank(), getValue()) provide controlled
//                access to the private variables.
// Exception handling

public class Card {
    private int suit, rank, value, xPosition, yPosition;
    //suit of the card (Clubs [0], Diamonds [1], Hearts [3], or Spades [4])
    //rank of the card (Ace[0], 2[1], 3[2], 4[3], 5[4], 6[5], 7[6], 8[7], 9[8], 10[9], Jack[10], Queen[11], or King[12])
    public Card() { suit = 0; rank = 0; value = 0; }
    public Card(int s, int r, int v) {
        suit = s;
        rank = r;
        value = v;
    }

    public int getSuit() { return suit; }
    public int getRank() { return rank; }
    public int getValue() { return value; }

    public void printCard(Graphics2D g2, boolean dealerTurn, boolean faceDown, int cardNumber) throws IOException {
        BufferedImage deckImg = ImageIO.read(new File("images/cardSpriteSheet.png"));

        // dimensions of the sprite sheet image in pixels.
        int imgWidth = 1499;
        int imgHeight = 618;

        // two-dimensional array to store the individual card pictures.
        BufferedImage[][] cardPictures = new BufferedImage[4][13];
        BufferedImage backOfACard = ImageIO.read(new File("images/backsideOfACard.jpeg"));

        //we assign the relative card pictures to the 2-D array.
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 13; r++) {
                cardPictures[c][r] = deckImg.getSubimage(r*imgWidth/13, c*imgHeight/4, imgWidth/13, imgHeight/4);
            }
        }

        // x and y coordinates for the cards to be printed.
        if (dealerTurn) {yPosition = 105;}
        else {yPosition = 430;}
        xPosition = 550 + 125*cardNumber;

        // face down or face up (image from the 2-D array).
        if (faceDown) {g2.drawImage(backOfACard, xPosition, yPosition, null );}
        else {g2.drawImage(cardPictures[suit][rank], xPosition, yPosition, null);}
    }
}