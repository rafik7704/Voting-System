import javax.swing.*;
import java.awt.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class VoteStatistic extends JFrame {

    private JButton homeButton;
    VoteStatistic(String fullName) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("Voting System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(0x123456));
        this.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x0E2A45));
        panel.setPreferredSize(new Dimension(750, 2000));
        panel.setLayout(null);

        JSONArray candidates = loadCandidatesData();

        for (int i = 0; i < candidates.length(); i++) {
            JSONObject candidate = candidates.getJSONObject(i);

            String imagePath = candidate.getString("image");
            ImageIcon originalImage = new ImageIcon("resources/images/" + imagePath);
            Image scaledImage = originalImage.getImage().getScaledInstance(150, 160, Image.SCALE_SMOOTH);
            ImageIcon candidateImage = new ImageIcon(scaledImage);

            JLabel candidateLabel = new JLabel(candidateImage);
            candidateLabel.setBounds(30, 20 + (i * 170), 150, 160);
            panel.add(candidateLabel);

            String name = candidate.getString("name");
            int votes = candidate.getInt("votes");

            JLabel nameLabel = new JLabel(name);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Work Sans", Font.PLAIN, 20));
            nameLabel.setBounds(200, 20 + (i * 170), 150, 160);
            panel.add(nameLabel);

            JLabel resultLabel = new JLabel("Votes: " + votes);
            resultLabel.setForeground(Color.WHITE);
            resultLabel.setFont(new Font("Work Sans", Font.PLAIN, 20));
            resultLabel.setBounds(500, 20 + (i * 170), 150, 160);
            panel.add(resultLabel);

            if (i < candidates.length() - 1) {
                JSeparator separator = new JSeparator();
                separator.setOrientation(SwingConstants.HORIZONTAL);
                separator.setBackground(Color.WHITE);
                separator.setBounds(30, 183 + (i * 170), 700, 2);
                panel.add(separator);
            }
        }

        ImageIcon voteImage = new ImageIcon(new ImageIcon(getClass().getResource("/admin.png")).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        JLabel label = new JLabel(voteImage);
        label.setHorizontalAlignment(JLabel.CENTER);

        JSeparator separatorResult = new JSeparator();
        separatorResult.setOrientation(SwingConstants.VERTICAL);
        separatorResult.setBackground(Color.WHITE);
        separatorResult.setBounds(480, 20, 2, 500);
        panel.add(separatorResult);

        JPanel empty1 = new JPanel();
        empty1.setPreferredSize(new Dimension(50, 50));
        empty1.setBackground(new Color(0x123456));
        JPanel empty2 = new JPanel();
        empty2.setPreferredSize(new Dimension(50, 50));
        empty2.setBackground(new Color(0x123456));
        JPanel empty3 = new JPanel();
        empty3.setPreferredSize(new Dimension(50, 50));
        empty3.setBackground(new Color(0x123456));

        ImageIcon image = new ImageIcon(getClass().getResource("/icon.png"));
        this.setIconImage(image.getImage());

        this.add(label, BorderLayout.NORTH);
        this.add(empty1, BorderLayout.SOUTH);
        this.add(empty2, BorderLayout.EAST);
        this.add(empty3, BorderLayout.WEST);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane, BorderLayout.CENTER);

        homeButton(fullName);

        this.setVisible(true);
    }

    private void homeButton(String fullName) {

        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Work Sans", Font.BOLD, 16));
        homeButton.setBackground(new Color(0x0E2A45));
        homeButton.setForeground(Color.WHITE);
        homeButton.setPreferredSize(new Dimension(120, 40));
        homeButton.setFocusable(false);

        JPanel homePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        homePanel.setBackground(new Color(0x123456));
        homePanel.add(homeButton);

        homeButton.addActionListener(e -> goHome(fullName));

        this.add(homePanel, BorderLayout.SOUTH);
    }

    private void goHome(String fullName) {
        this.dispose();
        new AdminDashboard(fullName);
    }

    private JSONArray loadCandidatesData() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("resources/candidates.json")));
            JSONObject jsonObject = new JSONObject(content);
            return jsonObject.getJSONArray("candidates");
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static void main(String[] args) {
        new VoteStatistic("rafik");
    }
}