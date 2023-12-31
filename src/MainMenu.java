import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Method;
import java.io.File;

import javax.imageio.ImageIO;

//inheritance, exception handling, reflections

public class MainMenu extends JFrame {
    public String username;
    public int BlackJackHighScore;
    public double MemoryGameHighScore;
    public MainMenu(String username, int BlackJackHighScore, double MemoryGameHighScore) {

        this.username = username;
        this.BlackJackHighScore = BlackJackHighScore;
        this.MemoryGameHighScore = MemoryGameHighScore;

        ImageIcon backgroundImage = new ImageIcon("images/miniprojfront.png");

        // Set the window properties
        setTitle("GameHub");
        setSize(1120, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //layout manager to display the background image
        setLayout(new BorderLayout());
        JLabel backgroundLabel = new JLabel(backgroundImage);
        add(backgroundLabel, BorderLayout.CENTER);

        // Create buttons for each game
        JButton blackjackButton = createGameButton("Blackjack", "images/blackjack.png", "Blackjack.Main");
        JButton memoryTilesButton = createGameButton("PokeTiles", "images/poketiles.png", "MemoryGame.Main");
        JButton ticTacToeButton = createGameButton("Tic-Tac-Toe", "images/tictactoe.png", "TicTacToe.TicTacToe");

        // Add buttons to the JFrame with a layered pane
        JLayeredPane layeredPane = getLayeredPane();
        blackjackButton.setBounds(382, 150, 336, 240);
        memoryTilesButton.setBounds(382, 300, 336, 240);
        ticTacToeButton.setBounds(382, 450, 336, 240);
        layeredPane.add(blackjackButton, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(memoryTilesButton, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(ticTacToeButton, JLayeredPane.PALETTE_LAYER);

        JLabel welcomeLabel = new JLabel("GameHub");
        welcomeLabel.setFont(new Font("Courier", Font.BOLD, 50));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(450, 125, 800, 50); // Set bounds for welcome label
        backgroundLabel.add(welcomeLabel);

        JLabel blackjackLabel = new JLabel("BlackJack");
        blackjackLabel.setFont(new Font("Courier", Font.BOLD, 30));
        blackjackLabel.setForeground(Color.BLACK);
        blackjackLabel.setBounds(475, 263, 800, 50);
        layeredPane.add(blackjackLabel, JLayeredPane.MODAL_LAYER);
        
        JLabel poketileLabel = new JLabel("PokeTiles");
        poketileLabel.setFont(new Font("Courier", Font.BOLD, 30));
        poketileLabel.setForeground(Color.BLACK);
        poketileLabel.setBounds(475, 413, 800, 50);
        layeredPane.add(poketileLabel, JLayeredPane.MODAL_LAYER);

        JLabel tictactoLabel = new JLabel("TicTacToe");
        tictactoLabel.setFont(new Font("Courier", Font.BOLD, 30));
        tictactoLabel.setForeground(Color.BLACK);
        tictactoLabel.setBounds(475, 565, 800, 50);
        layeredPane.add(tictactoLabel, JLayeredPane.MODAL_LAYER);

        // Make the window visible
        setVisible(true);
    }

    private JButton createGameButton(String gameName, String iconPath, String mainClass) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        try {
            Image img = ImageIO.read(new File(iconPath));
            button.setIcon(new ImageIcon(img));
        } 
        catch (IOException e) {e.printStackTrace();}
    
        // Make the button transparent
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to execute when the button is clicked
                try {
                    Class<?> findclass = Class.forName(mainClass);
                    Method method = findclass.getMethod("main", String[].class);
                    method.setAccessible(true);
                    String[] args = {username, String.valueOf(BlackJackHighScore), String.valueOf(MemoryGameHighScore)}; // Replace with your values
                    method.invoke(null, (Object) args);
                }
                catch (Exception ex) {ex.printStackTrace(); }
            }
        });
    
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Login().initialize();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
