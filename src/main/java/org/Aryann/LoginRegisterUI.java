package org.Atharv1;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
// import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginRegisterUI {
    private static String loggedInUsername;
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginRegisterUI() {
        frame = new JFrame("Login / Register");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  //centre the Login page.
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username :--");   
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);   // Get Username

        usernameField = new JTextField(20);
        usernameField.setBounds(90, 20, 180, 25);
        panel.add(usernameField);  

        JLabel passwordLabel = new JLabel("Password :--");
        passwordLabel.setBounds(10, 55, 80, 25);
        panel.add(passwordLabel);   // Get Password

        passwordField = new JPasswordField(20);
        passwordField.setBounds(90, 55, 180, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(45, 90, 200, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();  // check for authentication
            }
        });
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(45, 120, 200, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();   // Register new user
            }
        });
        panel.add(registerButton);
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        int userId = getUserId(username, password);
        if (userId != -1) {
            loggedInUsername = username;
            SwingUtilities.invokeLater(() -> {
                try {
                    new HomeWindow(userId).setVisible(true);  // GOto HomeWindow page if all okay
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            SwingUtilities.invokeLater(() -> frame.dispose());
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        UserDB.insertUser(username, password);
        JOptionPane.showMessageDialog(frame, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public int getUserId(String username, String password) {
        return UserDB.getUserId(username, password);
    }
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginRegisterUI();
            }
        });
    }
}
