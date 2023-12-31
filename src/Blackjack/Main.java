package Blackjack;
import javax.swing.JFrame;

public class Main {
    public static JFrame gameFrame = new JFrame(); //This is the frame in which the real blackjack game will be played.
    private static int playerScore = 0; //we have the player score, which starts as 0.
    private static int dealerScore = 0; //we have the dealer score, which starts as 0.
    public static int currentBalance = 1000; //we have the balance, which starts with 1000.
    public static String username;
    public static int HighScore;
    public static Game newGame; //we initialize a 'Game' in order to control, start, and calculate the blackjack game.
    private static boolean isFirstTime = true; //this boolean value will check if the game is newly started for the first time.

    public static void main(String[] args) throws InterruptedException {
        username = args[0];
        HighScore = Integer.parseInt(args[1]);
        newGame = new Game(gameFrame);
        Main.gameRefreshThread.start(); // simultaneously, we start our two threads that run at the same time.
        Main.gameCheckThread.start();
    }

    public static Thread gameRefreshThread = new Thread () {
        public void run () {
            while(true){
                newGame.atmosphereComponent.refresh(currentBalance, playerScore, dealerScore-1, newGame.faceDown);
                // Calls the refresh method of the GameComponent atmosphere component which is declared inside the Game class. This method updates the scores and balance values.
            }
        }
    };

    public static Thread gameCheckThread = new Thread () { //the second thread continually checks the game for a round over situation.
        public void run () {
            while(true) {
                if (isFirstTime||newGame.roundOver) {
                    if (newGame.dealerWon){
                        dealerScore++;
                        currentBalance-= GameComponent.currentBet;
                    }
                    else {
                        playerScore++;
                        currentBalance+= GameComponent.currentBet*2;
                    }
                    gameFrame.getContentPane().removeAll(); //we remove everything from the frame.
                    newGame = new Game(gameFrame); //we initialize a new game on the same frame.
                    newGame.formGame(); //we set the atmosphere of the game (which is everything except the cards)
                    isFirstTime = false; //once this thread worked, the game can't be opening for the first time
                }
            }
        }
    };
}
