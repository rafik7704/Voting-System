import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    AdminDashboard(String fullName) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x0E2A45));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(600, 600));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Welcome, " + fullName);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(welcomeLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(0x0E2A45));

        JButton registerCandidateButton = createButton("Register Candidate");
        registerCandidateButton.addActionListener(e -> {
            new RegisterCandidate(fullName);
            dispose();
        });

        JButton registerStudentButton = createButton("Register Student");
        registerStudentButton.addActionListener(e -> {
            new RegisterStudent(fullName);
            dispose();
        });

        JButton voteStatisticsButton = createButton("Vote Statistics");
        voteStatisticsButton.addActionListener(e -> {
            new VoteStatistic(fullName);
            dispose();
        });

        buttonPanel.add(registerCandidateButton);
        buttonPanel.add(registerStudentButton);
        buttonPanel.add(voteStatisticsButton);

        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setBackground(new Color(0x0E2A45));

        JButton logoutButton = createButton("Logout");
        logoutButton.addActionListener(e -> {
            new Login();
            dispose();
        });

        logoutPanel.add(logoutButton);
        panel.add(logoutPanel);

        this.setTitle("Admin Dashboard");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(new Color(0x0E2A45));
        this.add(panel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(0x0E2A45));
        footerPanel.setPreferredSize(new Dimension(600, 30));
        this.add(footerPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0x1D4B8A));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));

        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setHorizontalAlignment(SwingConstants.CENTER);

        return button;
    }
}