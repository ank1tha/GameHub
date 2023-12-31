package MemoryGame;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Timer;
import java.text.DecimalFormat;
class Game {
    public static String timeTaken;
    public static class Controller {
        final JFrame window;
        Model model;
        View view;
        Timer elapsedTimeTimer;
        public Controller(Model model) {
            this.window = new JFrame("Memory"); // Create the window
            //this.window.setSize(1120, 800);
            this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // Close the window when the user clicks on the close button
            this.window.setResizable(false); // Disable the resizing of the window
            this.reset(model); // Reset the game
        }
        // Reset the game
        public void reset(Model model) {
            this.model = model;
            this.model.resetElapsedTime();
            this.view = new View(model);
            this.window.setVisible(false);
            this.window.setContentPane(view);
            this.window.pack();
            this.window.setLocationRelativeTo(null);
            for (JButton button : this.model.getButtons()) {
                button.addActionListener(new ButtonActionListener(this));
            }
            Utilities.timer(200, (ignored) -> {
                this.window.setVisible(true);
                if (elapsedTimeTimer != null && elapsedTimeTimer.isRunning()) {
                    elapsedTimeTimer.stop();
                    elapsedTimeTimer = null;
                }
                // Update elapsed time every second
                Timer elapsedTimeTimer = new Timer(1000, e -> {
                    model.updateElapsedTime();
                    view.setElapsedTime(model.getElapsedTime());
                });
                if (elapsedTimeTimer.isRunning()) {
                    elapsedTimeTimer.stop();

                }
                elapsedTimeTimer.start();
            });
        }
        public JFrame getWindow() {
            return this.window;
        }
        public Model getModel() {
            return this.model;
        }
        public View getView() {
            return this.view;
        }
    }
    public static class Model {
        // Constants for the game
        long startTime; // Variable to store the start time of the game
        long elapsedTime;
        static final String[] AVAILABLE_IMAGES = new String[]{"0.png", "1.png", "3.png", "4.png", "5.png", "6.png", "7.png", "8.png"};
        static final Integer MAX_REGISTERED_SCORES = 10;
        final ArrayList<Float> scores;
        final ArrayList<JButton> buttons;
        final int columns; // Number of columns
        int tries; // Number of tries left
        boolean gameStarted; // Is the game started
        public Model(int columns) {
            this.columns = columns; // Number of columns
            this.buttons = new ArrayList<>(); // List of buttons in the game
            this.scores = new ArrayList<>(); // List of scores in the game
            this.tries =  10; // Number of tries initially
            this.gameStarted = false; // Game is not started initially
            int numberOfImage = columns * columns; // Number of images
            Vector<Integer> v = new Vector<>(); // Vector to store the images
            for (int i = 0; i < numberOfImage - numberOfImage % 2; i++) { // Add the images twice
                v.add(i % (numberOfImage / 2));
            }
            if (numberOfImage % 2 != 0) v.add(AVAILABLE_IMAGES.length - 1); // Add the last image if the number of images is odd
            // Add the images as a button to the game
            for (int i = 0; i < numberOfImage; i++) {
                int rand = (int) (Math.random() * v.size()); // Randomly select an image
                String reference = AVAILABLE_IMAGES[v.elementAt(rand)]; // Get the image
                this.buttons.add(new MemoryButton(reference)); // Add the image as a button

                v.removeElementAt(rand); // Remove the image from the vector
            }
        }
        public int getColumns() {
            return columns;
        }
        public ArrayList<JButton> getButtons() {
            return buttons;
        }
        // Get the number of tries left
        public int getTries() {
            return tries;
        }
        // Decrement the tries count by calling this method
        public void decrementTries() {
            this.tries--;
        }
        // return if the game has started
        public boolean isGameStarted() {
            return this.gameStarted;
        }
        // start the game
        public void startGame() {
            this.gameStarted = true;
            this.startTime = System.currentTimeMillis(); // Record the start time
        }

        public long getElapsedTime() {
            if (gameStarted) {
                long currentTime = System.currentTimeMillis();
                elapsedTime += (currentTime - startTime);
                startTime = currentTime;
                return elapsedTime;
            } else {
                return elapsedTime;
            }
        }

        public void updateElapsedTime() {
            if (gameStarted) {
                long currentTime = System.currentTimeMillis();
                elapsedTime += (currentTime - startTime);
                startTime = currentTime;
            }
        }

        public void resetElapsedTime() {
            this.elapsedTime = 0;
        }
    }

    public static class View extends JPanel {
        final JLabel tries;
        //final JLabel elapsedTimeLabel; // Label to display elapsed time

        public View(Model model) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.tries = new JLabel("", SwingConstants.CENTER);
            this.tries.setFont(new Font("Courier", Font.BOLD, 30));
            this.tries.setForeground(Color.WHITE);

            JPanel imagePanel = new JPanel();
            int columns = model.getColumns();
            imagePanel.setLayout(new GridLayout(columns, columns));
            for (JButton button : model.getButtons()) {
                imagePanel.add(button);
                button.setBackground(Color.BLACK);
                button.setForeground(Color.GRAY);
            }
            this.setTries(model.getTries());
            JPanel triesPanel = new JPanel();
            triesPanel.add(this.tries);
            triesPanel.setAlignmentX(CENTER_ALIGNMENT);
            triesPanel.setBackground(new Color(0X162c54));
            this.add(triesPanel);
            this.add(imagePanel);
            //this.elapsedTimeLabel = new JLabel("Elapsed Time: 0:00", SwingConstants.CENTER);
            //this.elapsedTimeLabel.setFont(new Font("Courier", Font.BOLD, 20));
            //this.elapsedTimeLabel.setForeground(Color.WHITE);
            //JPanel elapsedTimePanel = new JPanel();
            //elapsedTimePanel.add(this.elapsedTimeLabel);
            //elapsedTimePanel.setAlignmentX(CENTER_ALIGNMENT);
            //elapsedTimePanel.setBackground(new Color(0X162c54));
            //this.add(elapsedTimePanel);
        }
        public void setTries(int triesLeft) {
            this.tries.setText("Tries left : " + triesLeft);
        }
        public void setElapsedTime(long elapsedTime) {
            if (elapsedTime > 0) {
                DecimalFormat df = new DecimalFormat("0.00");
                double seconds = elapsedTime / 1000.0;
                //this.elapsedTimeLabel.setText("Elapsed Time: " + df.format(seconds) + "s");
            } else {
                //this.elapsedTimeLabel.setText("Elapsed Time: 0s");
            }
        }
    }
    // class to handle the button clicks
    public static class ReferencedIcon extends ImageIcon {
        final String reference;
        public ReferencedIcon(Image image, String reference) {
            super(image);
            this.reference = reference;
        }
        public String getReference() {
            return reference;
        }
    }
    // class to handle the button on the images
    public static class MemoryButton extends JButton {
        static final String IMAGE_PATH = "";
        static final Image NO_IMAGE = Utilities.loadImage("no_image.png");

        public MemoryButton(String reference) {
            Image image = Utilities.loadImage(IMAGE_PATH + reference);
            Dimension dimension = new Dimension(200,200);
            this.setPreferredSize(dimension);
            this.setIcon(new ImageIcon(NO_IMAGE));
            this.setDisabledIcon(new ReferencedIcon(image, reference));
        }
    }
    public static class Dialogs {
        public static void showLoseDialog(JFrame window,Model model) {
            UIManager.put("OptionPane.background", new Color(0Xedce5e));  //orange panel box
            UIManager.put("Panel.background", new Color(0Xedce5e));
            long elapsedTime = model.getElapsedTime();
            String timeTaken = formatElapsedTime(elapsedTime);

            String message = String.format("You lost. Try Again!\nTime taken: %s", timeTaken);
            JOptionPane.showMessageDialog(window.getContentPane(), message, "", JOptionPane.INFORMATION_MESSAGE);
        }

        // Helper method to format elapsed time as "mm:ss"
        private static String formatElapsedTime(long elapsedTime) {
            long seconds = elapsedTime / 1000;
            long minutes = seconds / 60;
            seconds %= 60;
            if(minutes==0){
                return String.format("%02d seconds", seconds);
            }
            else{
                return String.format("%02d:%02d minutes", minutes, seconds);
            }
        }

        public static void showWinDialog(JFrame window, Model model) {
            long elapsedTime = model.getElapsedTime();
            timeTaken = formatElapsedTime(elapsedTime);
            if ((double)elapsedTime/1000 < MemoryGame.Main.HighScore) {updateHighScore(((double)elapsedTime)/1000);}
            UIManager.put("OptionPane.background", new Color(0Xedce5e));
            UIManager.put("Panel.background", new Color(0Xedce5e));
            String message = String.format("You Won!\nTime taken: %s\nPersonal Best: %.2f seconds", timeTaken, MemoryGame.Main.HighScore);
            JOptionPane.showMessageDialog(window.getContentPane(), message, "", JOptionPane.INFORMATION_MESSAGE);
        }

        private static void updateHighScore(double newHighScore) {
            try {
                File file = new File("src/users.csv");
                Scanner scanner = new Scanner(file);

                List<String> updatedLines = new ArrayList<>();

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    String storedUsername = parts[0];

                    if (MemoryGame.Main.username.equals(storedUsername)) {
                        parts[3] = String.valueOf(newHighScore);
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
    }
    // class to handle the button clicks
    public static class ButtonActionListener implements ActionListener {
        final Controller controller;
        final Model model;
        final View view;
        final JFrame window;
        static int disabledButtonCount = 0;
        static JButton lastDisabledButton = null;
        static final Image TRAP_IMAGE = Utilities.loadImage("no_image.png");
        final ReferencedIcon trap;
        public ButtonActionListener(Controller controller) {
            this.controller = controller;
            this.model = controller.getModel();
            this.view = controller.getView();
            this.window = controller.getWindow();
            this.trap = new ReferencedIcon(TRAP_IMAGE, "no_image.png");
        }
        // Method to handle the button clicks and check if two images are same
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            button.setEnabled(false);
            ReferencedIcon thisIcon = (ReferencedIcon) button.getDisabledIcon();
            disabledButtonCount++;
            if (!model.isGameStarted()) { // If the game has not started
                model.startGame(); // Start the game
            }
            if (disabledButtonCount == 2) { // If two buttons are disabled
                ReferencedIcon thatIcon = (ReferencedIcon) lastDisabledButton.getDisabledIcon();
                boolean isPair = thisIcon.getReference().equals(thatIcon.getReference()); // Check if the two images are the same
                if (!isPair) { // If the two images are not the same
                    model.decrementTries();
                    view.setTries(model.getTries());
                    JButton lastButton = lastDisabledButton;
                    Utilities.timer(500, ((ignored) -> { // Wait 500ms before re-enabling the buttons
                        button.setEnabled(true); // Re-enable the button
                        lastButton.setEnabled(true); // Re-enable the last button
                    }));
                }
                disabledButtonCount = 0; // Reset the counter
            }
            ArrayList<JButton> enabledButtons = (ArrayList<JButton>) model.getButtons().stream().filter(Component::isEnabled).collect(Collectors.toList());
            if (enabledButtons.size() == 0) { // If all the buttons are disabled
                long elapsedTime = model.getElapsedTime();
                view.setElapsedTime(elapsedTime);
                Dialogs.showWinDialog(window, model); // Show the win dialog
                controller.reset(new Model(controller.getModel().getColumns())); // Reset the game
            }
            lastDisabledButton = button;    // Store the last button
            if (model.getTries() == 0) { // If the number of tries is 0
                Dialogs.showLoseDialog(window,model); // Show the lose dialog
                controller.reset(new Model(controller.getModel().getColumns())); // Reset the game

                Utilities.timer(1000, (ignored) -> model.getButtons().forEach(btn -> btn.setEnabled(false))); // Wait 1s before disabling all the buttons
            }
        }
    }
    public static class Utilities {
        // Method to create a timer
        public static void timer(int delay, ActionListener listener) {
            Timer t = new Timer(delay, listener);
            t.setRepeats(false);
            t.start();
        }

        // Method to load an image
        public static Image loadImage(String s) {
            Image image = null;
            try {
                InputStream resourceStream = Utilities.class.getResourceAsStream(s);
                if (resourceStream != null) {
                    image = ImageIO.read(resourceStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
    }
}

//  Main class to run the game
class Main{
    public static String username;
    public static double HighScore;
    static final int DEFAULT_SIZE = 4;
    public static void main(String[] args) {
        username = args[0];
        HighScore = Double.parseDouble(args[2]);
        Locale.setDefault(Locale.ENGLISH);
        SwingUtilities.invokeLater(() -> new Game.Controller(new Game.Model(DEFAULT_SIZE)));
    }
}