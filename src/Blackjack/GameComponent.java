package Blackjack;
import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GameComponent extends JComponent implements MouseListener {
    public BufferedImage logo, buttonbj, border1, border2, border3, border4;
    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerHand;
    private int playerScore, dealerScore;
    public boolean faceDown = true;
    public static boolean betMade = false;
    private int currentBalance;
    public static int currentBet;

    public GameComponent(ArrayList<Card> dH, ArrayList<Card> pH) {
        dealerHand = dH;
        playerHand = pH;
        dealerScore = 0;
        playerScore = 0;
        currentBalance = 1000;
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Color lightblue = new Color(70, 133, 123);
        Color darkblue = new Color(28, 76, 98);

        try {
            logo = ImageIO.read(new File("images/blackjackLogo.png"));
            buttonbj=ImageIO.read(new File("images/blackjackbutton.png"));
            border1=ImageIO.read(new File("images/border1.png"));
            border2=ImageIO.read(new File("images/border2.png"));
            border3=ImageIO.read(new File("images/border3.png"));
            border4=ImageIO.read(new File("images/border4.png"));
        }
        catch(IOException e) {}

        g2.drawImage(logo, 700, 280, null);
        Image newbuttonbj = buttonbj.getScaledInstance(160,120, Image.SCALE_SMOOTH);
        g2.drawImage(newbuttonbj, 517, 620, null);
        g2.drawImage(newbuttonbj, 680, 620, null);
        g2.drawImage(newbuttonbj, 845, 620, null);
        g2.drawImage(newbuttonbj, 888, 290, null);

        Image newborder1 = border1.getScaledInstance(125,125, Image.SCALE_SMOOTH);
        Image newborder2 = border2.getScaledInstance(125,125, Image.SCALE_SMOOTH);
        Image newborder3 = border3.getScaledInstance(125,125, Image.SCALE_SMOOTH);
        Image newborder4 = border4.getScaledInstance(125,125, Image.SCALE_SMOOTH);

        g2.drawImage(newborder1, 420, 10, null);
        g2.drawImage(newborder2, 967, 10, null);
        g2.drawImage(newborder3, 967, 620, null);
        g2.drawImage(newborder4, 420, 620, null);

        g2.setColor(lightblue);
        g2.fillRect(0, 0, 410, 800);
        g2.setColor(darkblue);
        g2.fillRect(40, 40, 330, 110);
        g2.fillRect(40, 640, 330, 110);
        g2.setColor(Color.WHITE); //we set the colors.
        g2.setFont(new Font("Courier", Font.BOLD, 30));
        g2.drawString("DEALER", 705, 60);
        g2.drawString("PLAYER", 705, 630);
        g2.drawString("Balance : " + currentBalance, 75, 680);
        g2.drawString("Dealer Wins : ", 70, 80);
        g2.drawString(Integer.toString(dealerScore), 310, 80);
        g2.drawString("Player Wins : ", 70, 130);
        g2.drawString(Integer.toString(playerScore), 310, 130);
        g2.setFont(new Font("Courier", Font.BOLD, 45));
        g2.setColor(darkblue);
        g2.drawString("-------------", 30, 200);
        g2.setColor(Color.WHITE);
        g2.drawString("RULES", 120, 240);
        g2.setColor(darkblue);
        g2.drawString("-------------", 30, 280);
        g2.drawString("-------------", 30, 630);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Courier", Font.BOLD, 15));
        g2.drawString("-> Click the BlackJack Icon to start.", 30, 305);
        g2.drawString("The goal of blackjack is to beat the ", 30, 330);
        g2.drawString("dealer's hand without going over 21.", 30, 355);
        g2.drawString("-> Face cards are worth 10.", 30, 380);
        g2.drawString("-> Aces are worth 1 or 11.", 30, 405);
        g2.drawString("Each player starts with two cards, one", 30, 430);
        g2.drawString("of the dealer's cards remains hidden.", 30, 455);
        g2.drawString("-> Hit : Ask for another card.", 30, 480);
        g2.drawString("-> Stand : End your turn.", 30, 505);
        g2.drawString("-> Double : Ask for two more cards.", 30, 530);
        g2.drawString("If you go over 21, the dealer wins", 30, 555);
        g2.drawString("If you're dealt 21, you got blackjack!", 30, 580);
        g2.setFont(new Font("Courier", Font.BOLD, 30));
        g2.drawString("Your Best : " + Main.HighScore, 60, 730);
        g2.setColor(Color.WHITE);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        g2.setFont(new Font("Courier", Font.ITALIC, 30));
        g2.drawString(sdf.format(cal.getTime()), 470, 360);

        try {
            for (int i = 0; i < dealerHand.size(); i++) {
                if (i == 0) { //if it is the first card,
                    //we check if it will be face down or not, we then draw each individual card.
                    if(faceDown) {dealerHand.get(i).printCard(g2, true, true, i); }
                    else {dealerHand.get(i).printCard(g2, true, false, i); }
                }
                else {dealerHand.get(i).printCard(g2, true, false, i); }
            }
        }
        catch (IOException e) {}

        try {
            for (int i = 0; i < playerHand.size(); i++) {
                playerHand.get(i).printCard(g2, false, false, i);
            }
        }
        catch (IOException e) {}
    }

    public void refresh(int cB, int uS, int dS, boolean fD) {
        currentBalance = cB;
        playerScore = uS;
        dealerScore = dS;
        faceDown = fD;
        this.repaint();
    }

    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if(mouseX>= 730 && mouseX<=810 && mouseY>=300 && mouseY<=380) {
            betMade = true;
            String[] options = new String[] {"1", "5", "10", "25", "100"};
            int response = JOptionPane.showOptionDialog(null, "Please enter your betting amount", "Betting",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch(response){
                case 0:
                    currentBet = 1;
                    currentBalance -= 1;
                    break;

                case 1:
                    currentBet = 5;
                    currentBalance -= 5;
                    break;

                case 2:
                    currentBet = 10;
                    currentBalance -= 10;
                    break;

                case 3:
                    currentBet = 25;
                    currentBalance -= 25;
                    break;

                case 4:
                    currentBet = 100;
                    currentBalance -= 100;
                    break;

                default :
                    currentBet = 1;
                    currentBalance -= 1;
                    break;
            }
            Main.newGame.startGame();
        }

    }
    //these methods will not be used but it is necessary to write them.
    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}