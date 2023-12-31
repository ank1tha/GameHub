package Blackjack;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;
import java.util.Scanner;
import java.io.FileWriter;
public class Game {
    ArrayList<Card> dealerHand;
    ArrayList<Card> playerHand;
    public boolean faceDown;
    public boolean dealerWon;
    public volatile boolean roundOver;
    // keyword volatile usage: to indicate that a variable's value may be changed by multiple threads simultaneously
    // adds synchronization between the threads to ensure that changes made by one thread are visible to other threads,

    JFrame frame;
    Deck deck;
    GameComponent atmosphereComponent;
    GameComponent cardComponent;
    // Two GameComponents: (1) background images, buttons and overall atmosphere (2) cards display

    JButton btnHit; //we have three buttons in this game.
    JButton btnStand;
    JButton btnDouble;
    JButton btnExit;

    public Game(JFrame f) {
        deck = new Deck();
        deck.shuffleDeck();
        dealerHand = new ArrayList<Card>();
        playerHand = new ArrayList<Card>();
        atmosphereComponent = new GameComponent(dealerHand, playerHand);
        frame = f;
        faceDown = true;
        dealerWon = true;
        roundOver = false;
    }

    public void formGame() {
        frame.setTitle("BlackJack");
        frame.setSize(1120, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.decode("#1c4c62"));
        Color lightblue = new Color(70, 133, 123);

        btnHit = new JButton("Hit");
        btnHit.setBounds(540, 660, 120, 50);
        btnHit.setFont(new Font("Courier", Font.BOLD, 20));
        btnHit.setBackground(lightblue);
        btnHit.setForeground(Color.WHITE);
        btnHit.setContentAreaFilled(false);
        btnHit.setBorderPainted(false);

        btnStand = new JButton("Stand");
        btnStand.setBounds(700, 660, 120, 50);
        btnStand.setFont(new Font("Courier", Font.TRUETYPE_FONT, 20));
        btnStand.setBackground(lightblue);
        btnStand.setForeground(Color.WHITE);
        btnStand.setContentAreaFilled(false);
        btnStand.setBorderPainted(false);

        btnDouble = new JButton("Double");
        btnDouble.setBounds(870, 660, 120, 50);
        btnDouble.setFont(new Font("Courier", Font.BOLD|Font.TRUETYPE_FONT, 20));
        btnDouble.setBackground(lightblue);
        btnDouble.setForeground(Color.WHITE);
        btnDouble.setContentAreaFilled(false);
        btnDouble.setBorderPainted(false);

        btnExit = new JButton("Exit");
        btnExit.setBounds(910, 330, 120, 50);
        btnExit.setFont(new Font("Courier", Font.BOLD|Font.TRUETYPE_FONT, 20));
        btnExit.setBackground(Color.decode("#46857b") );
        btnExit.setForeground(Color.WHITE);
        btnExit.setContentAreaFilled(false);
        btnExit.setBorderPainted(false);

        frame.add(btnHit); //we add the buttons the JFrame
        frame.add(btnStand);
        frame.add(btnDouble);
        frame.add(btnExit);

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "You have left the casino with " +  Main.currentBalance + ".");
                if (Main.HighScore < Main.currentBalance) {updateHighScore();}
                frame.dispose(); // Remove all components from the frame
            }
        });

        atmosphereComponent.setBounds(0, 0, 1130, 665);  //we set the bounds of the component.
        frame.add(atmosphereComponent); //we add the component to the frame.
        frame.setVisible(true); //we make the frame visible.
    }

    private static void updateHighScore() {
        try {
            File file = new File("src/users.csv");
            Scanner scanner = new Scanner(file);

            List<String> updatedLines = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String storedUsername = parts[0];

                if (Main.username.equals(storedUsername)) {
                    parts[2] = String.valueOf(Main.currentBalance);
                }
                updatedLines.add(String.join(",", parts));
            }

            scanner.close();

            // Update the file with the modified lines
            FileWriter writer = new FileWriter(file);
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() { //this method starts the game: the cards are drawn and are printed out.

        for(int i = 0; i<2; i++) { //we add the first two cards on top of the deck to dealer's hand.
            dealerHand.add(deck.getCard(i));
        }
        for(int i = 2; i<4; i++) { //we add the third and fourth card on top of the deck to the player's hand.
            playerHand.add(deck.getCard(i));
        }
        for (int i = 0; i < 4; i++) { //we then remove these cards from the game.
            deck.removeCard(0);
        }

        cardComponent = new GameComponent(dealerHand, playerHand); //we initialize our component which accepts two card arraylists.
        cardComponent.setBounds(0, 0, 1130, 665); //we set the bounds of our component.
        frame.add(cardComponent); //we add the component to the grame.
        frame.setVisible(true); //we make the frame visible.

        // checking for a blackjack
        checkHand(dealerHand);
        checkHand(playerHand);

        btnHit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCard(playerHand);
                checkHand(playerHand);
                if (getSumOfHand(playerHand)<17 && getSumOfHand(dealerHand)<17){
                    addCard(dealerHand);
                    checkHand(dealerHand);
                }
            }
        });

        btnDouble.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCard(playerHand);
                addCard(playerHand);
                checkHand(playerHand);
                if (getSumOfHand(playerHand)<17 && getSumOfHand(dealerHand)<17){
                    addCard(dealerHand);
                    checkHand(dealerHand);
                }
            }
        });

        btnStand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while (getSumOfHand(dealerHand)<17) {
                    addCard(dealerHand);
                    checkHand(dealerHand);
                }
                if ((getSumOfHand(dealerHand)<21) && getSumOfHand(playerHand)<21) {
                    if(getSumOfHand(playerHand) > getSumOfHand(dealerHand)) {
                        faceDown = false;
                        dealerWon = false;
                        JOptionPane.showMessageDialog(frame, "Player has won because of a better hand!");
                        rest();
                        roundOver = true;
                    }
                    else {
                        faceDown = false;
                        JOptionPane.showMessageDialog(frame, "Dealer has won because of a better hand!");
                        rest();
                        roundOver = true;
                    }
                }
            }
        });
    }

    public void checkHand (ArrayList<Card> hand) {
        if (hand.equals(playerHand)) { //checks if the parameter is player's hand.
            if(getSumOfHand(hand) == 21){
                faceDown = false;
                dealerWon = false;
                JOptionPane.showMessageDialog(frame, "Player got a BlackJack! Player has won!");
                rest();
                roundOver = true;
            }
            else if (getSumOfHand(hand) > 21) {
                faceDown = false; JOptionPane.showMessageDialog(frame, "Player busted! Dealer has won!");
                rest();
                roundOver = true;
            }
        }
        else {
            if(getSumOfHand(hand) == 21) {
                faceDown = false;
                JOptionPane.showMessageDialog(frame, "Dealer got a BlackJack! Dealer has won!");
                rest();
                roundOver = true;
            }
            else if (getSumOfHand(hand) > 21) {
                faceDown = false;
                dealerWon = false;
                JOptionPane.showMessageDialog(frame, "Dealer busted! Player has won!");
                rest();
                roundOver = true;
            }
        }
    }

    public void addCard(ArrayList<Card> hand) {
        hand.add(deck.getCard(0));
        deck.removeCard(0);
        faceDown = true;
    }




    public int aceCountInHand(ArrayList<Card> hand){
        int aceCount = 0;
        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getValue() == 11) {
                aceCount++;
            }
        }
        return aceCount;
    }

    public int getSumWithHighAce(ArrayList<Card> hand) {
        int handSum = 0;
        for (int i = 0; i < hand.size(); i++){
            handSum += hand.get(i).getValue();
        }
        return handSum;
    }

    public int getSumOfHand (ArrayList<Card> hand) {
        if(aceCountInHand(hand) > 0) {
            if (getSumWithHighAce(hand) <= 21) {return getSumWithHighAce(hand);}
            else{
                for (int i = 0; i < aceCountInHand(hand); i++) {
                    int sumOfHand = getSumWithHighAce(hand)-(i+1)*10;
                    if(sumOfHand <= 21) {return sumOfHand; }
                }
            }
        }
        else {
            int sumOfHand = 0;
            for (int i = 0; i < hand.size(); i++) {sumOfHand += hand.get(i).getValue();}
            return sumOfHand;
        }
        return 22; //we basically set it to the 'bust' case if the method returns nothing up to this point.
    }

    public static void rest() {
        //this method sleeps the program. It basically serves as a time duration between events.
        try {Thread.sleep(500);}
        catch (InterruptedException e) {}
    }
}