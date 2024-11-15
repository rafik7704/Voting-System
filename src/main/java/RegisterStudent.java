import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.*;

public class RegisterStudent extends JFrame {

    public RegisterStudent(String adminName) {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x0E2A45));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Register Student Account");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setForeground(Color.WHITE);
        studentIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(studentIdLabel, gbc);

        JTextField studentIdField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(studentIdField, gbc);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(nameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(confirmPasswordField, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(0x1D4B8A));
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        this.setTitle("Register Student");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(new Color(0x0E2A45));
        this.add(panel, BorderLayout.CENTER);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdField.getText();
                String fullName = nameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (studentId.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterStudent.this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterStudent.this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    User newUser = new User(createUserJson(studentId, fullName, password));

                    if (updateUsersJson(newUser)) {
                        JOptionPane.showMessageDialog(RegisterStudent.this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();

                        new AdminDashboard(adminName);
                    } else {
                        JOptionPane.showMessageDialog(RegisterStudent.this, "Error updating users file!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        this.setVisible(true);
    }

    private JSONObject createUserJson(String id, String fullName, String password) {
        JSONObject userJson = new JSONObject();
        userJson.put("id", id);
        userJson.put("fullName", fullName);
        userJson.put("password", password);
        userJson.put("roles", "STUDENT");
        return userJson;
    }

    private boolean updateUsersJson(User newUser) {
        try {

            Path usersFilePath = Paths.get("resources/users.json");
            File usersFile = usersFilePath.toFile();
            JSONObject rootObject;
            JSONArray usersArray;

            if (usersFile.exists() && usersFile.length() > 0) {

                String content = new String(Files.readAllBytes(usersFilePath));
                try {

                    rootObject = new JSONObject(content);

                    usersArray = rootObject.optJSONArray("users");
                    if (usersArray == null) {

                        usersArray = new JSONArray();
                        rootObject.put("users", usersArray);
                    }
                } catch (JSONException e) {

                    rootObject = new JSONObject();
                    usersArray = new JSONArray();
                    rootObject.put("users", usersArray);
                }
            } else {

                rootObject = new JSONObject();
                usersArray = new JSONArray();
                rootObject.put("users", usersArray);
            }

            JSONObject newUserJson = new JSONObject();
            newUserJson.put("id", newUser.getId());
            newUserJson.put("fullName", newUser.getFullName());
            newUserJson.put("password", newUser.getPassword());
            newUserJson.put("roles", newUser.getRoles());
            usersArray.put(newUserJson);

            Files.write(usersFilePath, rootObject.toString(4).getBytes());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}