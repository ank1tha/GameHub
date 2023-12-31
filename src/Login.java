import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;

//inheritance, exception handling, reflections

class Login extends JFrame {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    ImageIcon backgroundImage = new ImageIcon("images/miniprojfront.png");

    public void initialize() throws IOException {
    frame = new JFrame("Authentication");
    frame.setSize(1120, 800);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setLayout(new BorderLayout());

    // Create a JLabel with the background image
    JLabel backgroundLabel = new JLabel(backgroundImage);
    frame.add(backgroundLabel, BorderLayout.CENTER);

    JButton buttonlogin = createGameButton("LogIn","images/login.png",1);
    JButton buttonsignup = createGameButton("SignUp","images/signup.png",2);

    //JLayeredPane layeredPane = getLayeredPane();
    buttonlogin.setBounds(410, 400, 280, 120);
    buttonsignup.setBounds(410, 540, 280, 120);
    backgroundLabel.add(buttonlogin, JLayeredPane.PALETTE_LAYER);
    backgroundLabel.add(buttonsignup, JLayeredPane.PALETTE_LAYER);

    JLabel welcomeLabel = new JLabel("GameHub");
    welcomeLabel.setFont(new Font("Courier", Font.BOLD, 50));
    welcomeLabel.setForeground(Color.WHITE);
    welcomeLabel.setBounds(450, 125, 800, 50); // Set bounds for welcome label
    backgroundLabel.add(welcomeLabel);

    JLabel userLabel = new JLabel("Username:");
    userLabel.setFont(new Font("Courier", Font.BOLD, 20));
    userLabel.setForeground(Color.WHITE);
    userLabel.setBounds(500, 190, 180, 50);
    backgroundLabel.add(userLabel);

    usernameField = new JTextField(20);
    usernameField.setBounds(460, 240, 190, 50);
    backgroundLabel.add(usernameField);

    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setFont(new Font("Courier", Font.BOLD, 20));
    passwordLabel.setForeground(Color.WHITE);
    passwordLabel.setBounds(500, 300, 180, 50);
    backgroundLabel.add(passwordLabel);

    passwordField = new JPasswordField(20);
    passwordField.setBounds(460, 350, 190, 50);
    backgroundLabel.add(passwordField);
    // Make the frame visible
    frame.setVisible(true);
}


private JButton createGameButton(String gameName, String iconPath, int choice) {
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
    if(choice==1){
    button.setBounds(450,460, 180, 10);
    button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
                        JOptionPane.showMessageDialog(frame, "Login Successful!");
                        frame.setVisible(false);
                        SwingUtilities.invokeLater(() -> new MainMenu(usernameField.getText(), getBJHighScore(usernameField.getText()), getMGHighScore(usernameField.getText())));
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid Credentials!");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });}
    if(choice==2){
        button.setBounds(450, 600, 180, 10);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!isUsernameTaken(usernameField.getText())) {
                        signUp(usernameField.getText(), new String(passwordField.getPassword()));
                        JOptionPane.showMessageDialog(frame, "Sign Up Successful!");
                        frame.setVisible(false);
                        SwingUtilities.invokeLater(() -> new MainMenu(usernameField.getText(), 0, 0));
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username already taken. Choose a different username.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
    return button;
}
public boolean authenticate(String username, String password) throws IOException {
    File file = new File("src/users.csv");
    Scanner scanner = new Scanner(file);

    while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] parts = line.split(",");
        String storedUsername = parts[0];
        String storedPassword = parts[1];

            if (username.equals(storedUsername) && password.equals(storedPassword)) {
                scanner.close();
                return true;
            }
        }

        scanner.close();
        return false;
    }

    public int getBJHighScore(String username) {
        try {
            File file = new File("src/users.csv");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String storedUsername = parts[0];

                if (username.equals(storedUsername)) {
                    scanner.close();
                    return Integer.parseInt(parts[2]); // BlackJack
                }
            }
            scanner.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public double getMGHighScore(String username) {
        try {
            File file = new File("src/users.csv");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String storedUsername = parts[0];

                if (username.equals(storedUsername)) {
                    scanner.close();
                    return Double.parseDouble(parts[3]);
                }
            }
            scanner.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public boolean isUsernameTaken(String username) throws IOException {
        File file = new File("src/users.csv");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            String storedUsername = parts[0];

            if (username.equals(storedUsername)) {
                scanner.close();
                return true;
            }
        }

        scanner.close();
        return false;
    }

    public void signUp(String username, String password) throws IOException {
        FileWriter writer = new FileWriter("src/users.csv", true);
        writer.append(username + "," + password + ",0,1800\n");
        writer.close();
    }
}
