package TicTacToe;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.io.*;
import java.util.Scanner;

//file and exception handling, inheritance, interfaces, method overriding

public class TicTacToe extends JPanel implements ActionListener {

    boolean playerX; // true if player X's turn, false if player O's turn
    boolean gameDone = false; // true if game is over
    int winner = -1; // 0 if X wins, 1 if O wins, -1 if no winner yet
    int player1wins = 0, player2wins = 0; // number of wins for each player
    int[][] board = new int[3][3]; // 0 if empty, 1 if X, 2 if O

    // paint variables
    int lineWidth = 5; 
    int lineLength =750 ; 
    int x = 30, y = 250; // location of first line
    int offset = 295; // square width
    int a = 0; // used for drawing the X's and O's
    int b = 5; // used for drawing the X's and O's
    int selX = 0; // selected square x
    int selY = 0; // selected square y

    // COLORS
    Color burgundy = new Color(184, 45, 70);
    Color lightyellow = new Color(237,206,94);
    Color lightorange = new Color(226,165,96);
    Color deeporange = new Color(223,128,62);
    Color purple = new Color(143,21,79);

    JButton jButton;

    public TicTacToe() {
        Dimension size = new Dimension(1120, 800); // size of the panel
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        addMouseListener(new XOListener()); // add mouse listener
        jButton = new JButton("New Game");
        jButton.addActionListener(this); // add action listener
        jButton.setBounds(315, 210, 100, 30); // set button location
        add(jButton); // add button to panel
        resetGame();
    }

    public void resetGame() {
        playerX = true;
        winner = -1;
        gameDone = false;
        //resetting the board as unselected 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0; // all spots are empty
            }
        }
        getJButton().setVisible(false); // hide the new game button
    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        drawBoard(page);
        drawUI(page);
        drawGame(page);
    }

    public void drawBoard(Graphics page) {
        setBackground(burgundy);
        page.setColor(lightorange);
        // drawing the grid for the game
        page.fillRoundRect(x, y, lineLength, lineWidth, 5, 30);
        page.fillRoundRect(x, y + offset, lineLength, lineWidth, 5, 30);
        page.fillRoundRect(y, x, lineWidth, lineLength, 30, 5);
        page.fillRoundRect(y + offset, x, lineWidth, lineLength, 30, 5);
    }

    public void drawUI(Graphics page) {
        page.setColor(purple);
        page.fillRect(820, 0, 450, 800);
        Font font = new Font("Courier", Font.BOLD, 40);
        page.setFont(font);

        // SET WIN COUNTER
        page.setColor(lightyellow);
        page.drawString("-----------", 840, 45);
        page.setColor(deeporange);
        page.drawString("Win Count", 870, 90);
        page.setColor(lightyellow);
        page.drawString("-----------", 840, 135);
        page.drawString(": " + player1wins, 950, 205);
        page.drawString(": " + player2wins, 950, 270);
        page.drawString("-----------", 840, 340);
        page.drawString("-----------", 840, 500);

        // DRAW score X
        ImageIcon xIcon = new ImageIcon("images/orangex.png");
        Image xImg = xIcon.getImage();
        //resizing
        Image newXImg = xImg.getScaledInstance(36, 36, java.awt.Image.SCALE_SMOOTH);
        ImageIcon newXIcon = new ImageIcon(newXImg);
        page.drawImage(newXIcon.getImage(),offset+ 600, 175, null);

        // DRAW score O
        page.setColor(lightyellow);
        page.fillOval(offset+ 600, 240, 36, 36);
        page.setColor(purple);
        page.fillOval(offset+ 605, 245, 27, 27);

        // DRAW WHOS TURN or WINNER
        page.setColor(lightyellow);
        Font font1 = new Font("Courier", Font.BOLD, 38);
        page.setFont(font1);

        if (gameDone) {
            if (winner == 1) { // x
                page.setColor(deeporange);
                page.fillRect(855, 380, 235,60);
                page.setColor(lightyellow);
                page.drawString("Winner: X", 870, 420);                
            } 
            else if (winner == 2) { // o
                page.setColor(deeporange);
                page.fillRect(855, 380, 235,60);
                page.setColor(lightyellow);
                page.drawString("Winner: O", 870, 420);
            } 
            else if (winner == 3) { // tie
                page.setColor(deeporange);
                page.fillRect(845, 380, 260,60);
                page.setColor(lightyellow);
                page.drawString("It's a tie", 860, 420);
            }
        } 
        else {
            Font font2 = new Font("Courier", Font.BOLD, 40);
            page.setFont(font2);
            page.drawString("", 350, 160);
            if (playerX) {
                page.setColor(deeporange);
                page.fillRect(855, 380, 230,60);
                page.setColor(lightyellow);
                page.drawString("X's Turn", 875, 420);
            } 
            else {
                page.setColor(deeporange);
                page.fillRect(855, 380, 230,60);
                page.setColor(lightyellow);
                page.drawString("O's Turn", 875, 420);
            }
        }
        // DRAW LOGO
        Image cookie = Toolkit.getDefaultToolkit().getImage("images/logo.png");
        page.drawImage(cookie, 860, 600, 220, 200, this);
        Font c = new Font("Courier", Font.BOLD, 35);
        page.setColor(lightyellow);
        page.setFont(c);
        page.drawString("Tic Tac Toe", 855, 580);
    }
 
    public void drawGame(Graphics page) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //1 means X icon, 2 means O
                if (board[i][j] == 0) {
                } else if (board[i][j] == 1) {
                    ImageIcon xIcon = new ImageIcon("images/orangex.png");
                    Image xImg = xIcon.getImage();
                    Image resizedXImg = xImg.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    ImageIcon resizedXIcon = new ImageIcon(resizedXImg);
                    page.drawImage(resizedXIcon.getImage(), 70 + offset * i, 70 + offset * j, null);
                } else if (board[i][j] == 2) {
                    page.setColor(lightyellow);
                    page.fillOval(70 + offset * i, 74 + offset * j, 80, 80);
                    page.setColor(burgundy);
                    page.fillOval(80 + offset * i, 84 + offset * j, 60, 60);
                }
            }
        }
        repaint();
    }

    public void checkWinner() {
        if (gameDone == true) {
            System.out.print("gameDone");
            return;
        }
        //the different kinds of ways 3 in a row can be made
        // vertical
        int temp = -1;
        if ((board[0][0] == board[0][1])
                && (board[0][1] == board[0][2])
                && (board[0][0] != 0)) {
            temp = board[0][0];
        } else if ((board[1][0] == board[1][1])
                && (board[1][1] == board[1][2])
                && (board[1][0] != 0)) {
            temp = board[1][1];
        } else if ((board[2][0] == board[2][1])
                && (board[2][1] == board[2][2])
                && (board[2][0] != 0)) {
            temp = board[2][1];

        // horizontal
        } else if ((board[0][0] == board[1][0])
                && (board[1][0] == board[2][0])
                && (board[0][0] != 0)) {
            temp = board[0][0];
        } else if ((board[0][1] == board[1][1])
                && (board[1][1] == board[2][1])
                && (board[0][1] != 0)) {
            temp = board[0][1];
        } else if ((board[0][2] == board[1][2])
                && (board[1][2] == board[2][2])
                && (board[0][2] != 0)) {
            temp = board[0][2];

        // diagonal
        } else if ((board[0][0] == board[1][1])
                && (board[1][1] == board[2][2])
                && (board[0][0] != 0)) {
            temp = board[0][0];
        } else if ((board[0][2] == board[1][1])
                && (board[1][1] == board[2][0])
                && (board[0][2] != 0)) {
            temp = board[0][2];
        } else {

            // CHECK FOR A TIE
            boolean notDone = false;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) { // checking if any of the slots are empty
                        notDone = true;
                        break;
                    }
                }
            }
            if (notDone == false) {temp = 3;} // this sets it as a tie
        }

        // to display whether someone has won or tie 
        if (temp > 0) {
            winner = temp;
            if (winner == 1) {
                player1wins++;
                System.out.println("winner is X");
            } 
            else if (winner == 2) {
                player2wins++;
                System.out.println("winner is O");
            } 
            else if (winner == 3) {System.out.println("It's a tie");}
            gameDone = true;
            getJButton().setVisible(true);//showing the new game button
        }
    }

    public JButton getJButton() {return jButton;}

    public void setPlayerXWins(int a) {player1wins = a;}

    public void setPlayerOWins(int a) {player2wins = a;}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.getContentPane();

        TicTacToe gamePanel = new TicTacToe();
        frame.add(gamePanel);

        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                try {
                    File file = new File("score.txt");
                    Scanner sc = new Scanner(file);
                    frame.setLocationRelativeTo(null);
                    gamePanel.setPlayerXWins(Integer.parseInt(sc.nextLine()));
                    gamePanel.setPlayerOWins(Integer.parseInt(sc.nextLine()));
                    sc.close();
                } catch (IOException io) {
                    // file doesnt exist
                    File file = new File("score.txt");
                }
            }

            public void windowClosing(WindowEvent e) {
                try {
                    PrintWriter pw = new PrintWriter("score.txt");
                    pw.write("");
                    pw.write(gamePanel.player1wins + "\n");
                    pw.write(gamePanel.player2wins + "\n");
                    pw.close();
                } 
                catch (FileNotFoundException e1) {}
            }
        });
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    private class XOListener implements MouseListener {
        //to track which box has been clicked by assigning a dimension space to each box on the grid
        public void mouseClicked(MouseEvent event) {
            selX = -1;
            selY = -1;
            if (gameDone == false) {
                a = event.getX();
                b = event.getY();
                int selX = 0, selY = 0;
                if (a > 12 && a < 273) {selX = 0;} 
                else if (a > 273 && a < 546) {selX = 1;} 
                else if (a > 546 && a < 830) {selX = 2;} 
                else {selX = -1;}

                if (b > 12 && b < 273) {selY = 0;} 
                else if (b > 273 && b < 546) {selY = 1;} 
                else if (b > 546 && b < 830) {selY = 2;} 
                else {selY = -1;}

                if (selX != -1 && selY != -1) {
                    //1 means x played, 2 means O played
                    if (board[selX][selY] == 0) {
                        if (playerX) {
                            board[selX][selY] = 1;
                            playerX = false;
                        } 
                        else {
                            board[selX][selY] = 2;
                            playerX = true;
                        }
                        checkWinner();
                        //to track which box has been clicked
                        System.out.println(" CLICK= x:" + a + ",y: " + b + "; selX,selY: " + selX + "," + selY);
                    }
                } 
                else {System.out.println("invalid click");}
            }
        }

        public void mouseReleased(MouseEvent event) {}

        public void mouseEntered(MouseEvent event) {}

        public void mouseExited(MouseEvent event) {}

        public void mousePressed(MouseEvent event) {}

    }

    @Override
    public void actionPerformed(ActionEvent e) {resetGame();}

}
