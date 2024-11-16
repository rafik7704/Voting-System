import javax.swing.*;
import java.awt.*;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends JFrame {

    Login() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x0E2A45));
        panel.setPreferredSize(new Dimension(50, 50));
        panel.setLayout(null);

        //ID Label
        JLabel idLabel = new JLabel("ID");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(new Font("Work Sans", Font.BOLD, 20));
        idLabel.setBounds(100, 120, 100, 30);
        panel.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(100, 160, 450, 30);
        idField.setBackground(Color.WHITE);
        idField.setForeground(Color.BLACK);
        idField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x0E2A45), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        idField.setCaretColor(Color.BLACK);
        panel.add(idField);

        //Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Work Sans", Font.BOLD, 20));
        passwordLabel.setBounds(100, 270, 100, 30);
        panel.add(passwordLabel);


        //Password Field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 310, 450, 30);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x0E2A45), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setCaretColor(Color.BLACK);
        panel.add(passwordField);

        JButton submitButton = new JButton("Submit");
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Work Sans", Font.BOLD, 20));
        submitButton.setBackground(new Color(0x0E2A45));
        submitButton.setBounds(250, 430, 150, 30);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> authenticateUser(idField.getText(), new String(passwordField.getPassword()), this));
        panel.add(submitButton);


        //Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Work Sans", Font.BOLD, 20));
        exitButton.setBackground(new Color(0x0E2A45));
        exitButton.setBounds(100, 430, 100, 30);
        exitButton.setFocusable(false);
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton);

        this.setTitle("Voting System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setResizable(false);
        this.setLayout(new BorderLayout(10, 10));

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon.png"));
        this.setIconImage(imageIcon.getImage());
        this.getContentPane().setBackground(new Color(0x123456));

        JLabel label = new JLabel();
        ImageIcon voteImageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/vote.png")).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        label.setIcon(voteImageIcon);
        label.setHorizontalAlignment(JLabel.CENTER);

        JPanel empty1 = new JPanel();
        empty1.setPreferredSize(new Dimension(50, 50));
        empty1.setBackground(new Color(0x123456));
        JPanel empty2 = new JPanel();
        empty2.setPreferredSize(new Dimension(50, 50));
        empty2.setBackground(new Color(0x123456));
        JPanel empty3 = new JPanel();
        empty3.setPreferredSize(new Dimension(50, 50));
        empty3.setBackground(new Color(0x123456));

        this.add(panel, BorderLayout.CENTER);
        this.add(label, BorderLayout.NORTH);
        this.add(empty1, BorderLayout.WEST);
        this.add(empty2, BorderLayout.EAST);
        this.add(empty3, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    //Authenticate User
    private void authenticateUser(String userId, String password, JFrame loginFrame) {
        try {
            String filePath = "resources/users.json";
            FileInputStream fileInputStream = new FileInputStream(filePath);

            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            }

            JSONObject jsonObject = new JSONObject(content.toString());
            JSONArray users = jsonObject.getJSONArray("users");

            boolean authenticated = false;
            String role = null;
            String fullName = null;

            for (int i = 0; i < users.length(); i++) {
                JSONObject userJson = users.getJSONObject(i);
                User user = new User(userJson);

                if (user.getId().equals(userId) && user.getPassword().equals(password)) {
                    authenticated = true;
                    role = user.getRoles();
                    fullName = user.getFullName();
                    break;
                }
            }

            if (authenticated) {
                if ("STAFF".equals(role)) {
                    new AdminDashboard(fullName);
                } else if ("STUDENT".equals(role)) {
                    new Vote();
                }
                loginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid ID or Password!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginFrame, "An error occurred while authenticating.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}