import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Enumeration;
import org.json.JSONArray;
import org.json.JSONObject;

public class Vote extends JFrame {
    private JSONArray candidates;
    private ButtonGroup group;
    private JButton submitButton;

    public Vote() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupFrame();
        loadCandidates();
        setupPanel();
        setupSubmitButton();

        this.setVisible(true);
    }

    private void setupFrame() {
        this.setTitle("Voting System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(0x123456));
        this.setLayout(null);

        ImageIcon iconImage = new ImageIcon(getClass().getResource("/icon.png"));
        this.setIconImage(iconImage.getImage());
    }

    private void loadCandidates() {
        try {
            File jsonFile = new File("resources/candidates.json");
            if (jsonFile.exists()) {
                String content = new String(java.nio.file.Files.readAllBytes(jsonFile.toPath()));
                JSONObject jsonObject = new JSONObject(content);
                candidates = jsonObject.getJSONArray("candidates");

                for (int i = 0; i < candidates.length(); i++) {
                    JSONObject candidate = candidates.getJSONObject(i);
                    if (!candidate.has("votes")) {
                        candidate.put("votes", 0);
                    }
                }
            } else {
                System.out.println("Error: candidates.json file not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupPanel() {
        group = new ButtonGroup();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x0E2A45));
        panel.setBounds(80, 210, 640, 260);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int gridY = 0;
        for (int i = 0; i < candidates.length(); i++) {
            JSONObject candidate = candidates.getJSONObject(i);
            String candidateName = candidate.getString("name");
            String imagePath = "resources/images/" + candidate.getString("image");

            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                ImageIcon originalImage = new ImageIcon(imageFile.getAbsolutePath());
                Image image = originalImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon resizedImage = new ImageIcon(image);

                JLabel candidateLabel = new JLabel(resizedImage);
                gbc.gridx = 0;
                gbc.gridy = gridY;
                panel.add(candidateLabel, gbc);

                JRadioButton radioButton = new JRadioButton(candidateName);
                radioButton.setForeground(Color.WHITE);
                radioButton.setOpaque(false);
                radioButton.setFont(new Font("Work Sans", Font.PLAIN, 20));
                radioButton.setFocusable(false);
                gbc.gridx = 1;
                panel.add(radioButton, gbc);

                group.add(radioButton);
            }

            gridY++;
        }

        this.add(panel);

        JLabel text = new JLabel("PLEASE CHOOSE YOUR CANDIDATE");
        text.setForeground(Color.WHITE);
        text.setFont(new Font("Work Sans", Font.BOLD, 20));
        text.setBounds(225, 120, 500, 30);
        this.add(text);
    }

    private void setupSubmitButton() {
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Work Sans", Font.PLAIN, 12));
        submitButton.setBackground(new Color(0x0E2A45));
        submitButton.setBounds(350, 580, 100, 40);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusable(false);
        submitButton.addActionListener(e -> submitVote());
        this.add(submitButton);
    }

    private void submitVote() {
        JRadioButton selectedButton = getSelectedButton();
        if (selectedButton != null) {
            String selectedCandidate = selectedButton.getText();
            for (int i = 0; i < candidates.length(); i++) {
                JSONObject candidate = candidates.getJSONObject(i);
                String candidateName = candidate.getString("name");

                if (candidateName.equals(selectedCandidate)) {
                    int currentVotes = candidate.optInt("votes", 0);
                    candidate.put("votes", currentVotes + 1);
                    saveUpdatedVotes();
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a candidate!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JRadioButton getSelectedButton() {
        Enumeration<AbstractButton> elements = group.getElements();
        while (elements.hasMoreElements()) {
            JRadioButton button = (JRadioButton) elements.nextElement();
            if (button.isSelected()) {
                return button;
            }
        }
        return null;
    }

    private void saveUpdatedVotes() {
        try {
            File jsonFile = new File("/Users/rafikazlan/IdeaProjects/Voting System/resources/candidates.json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("candidates", candidates);

            try (FileWriter writer = new FileWriter(jsonFile)) {
                writer.write(jsonObject.toString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Vote();
    }
}