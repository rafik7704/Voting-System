import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterCandidate extends JFrame {

    JTextField nameField;
    JTextField imageField;
    JButton registerButton;
    JButton selectImageButton;
    JButton homeButton;

    public RegisterCandidate(String fullName) {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setTitle("Register Candidate");
        this.setSize(500, 450);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(0x123456));
        this.setLayout(null);

        JLabel titleLabel = new JLabel("CANDIDATE REGISTRATION", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Work Sans", Font.BOLD, 18));
        titleLabel.setBounds(50, 10, 400, 40);
        this.add(titleLabel);

        JLabel nameLabel = new JLabel("Candidate Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Work Sans", Font.PLAIN, 14));
        nameLabel.setBounds(50, 70, 150, 30);
        this.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(200, 70, 230, 30);
        this.add(nameField);

        JLabel imageLabel = new JLabel("Image Path:");
        imageLabel.setForeground(Color.WHITE);
        imageLabel.setFont(new Font("Work Sans", Font.PLAIN, 14));
        imageLabel.setBounds(50, 120, 150, 30);
        this.add(imageLabel);

        imageField = new JTextField();
        imageField.setBounds(200, 120, 230, 30);
        imageField.setEditable(false);
        this.add(imageField);

        selectImageButton = new JButton("Choose Image");
        selectImageButton.setFont(new Font("Work Sans", Font.PLAIN, 14));
        selectImageButton.setBackground(new Color(0x0E2A45));
        selectImageButton.setBounds(200, 170, 150, 35);
        selectImageButton.setForeground(Color.WHITE);
        selectImageButton.setFocusable(false);
        this.add(selectImageButton);

        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooser();
            }
        });

        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Work Sans", Font.PLAIN, 14));
        homeButton.setBackground(new Color(0x0E2A45));
        homeButton.setBounds(50, 300, 150, 40);
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusable(false);
        this.add(homeButton);

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminDashboard(fullName);
                dispose();
            }
        });

        registerButton = new JButton("Register Candidate");
        registerButton.setFont(new Font("Work Sans", Font.PLAIN, 14));
        registerButton.setBackground(new Color(0x0E2A45));
        registerButton.setBounds(280, 300, 200, 40);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusable(false);
        this.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerCandidate(fullName);
            }
        });

        this.setVisible(true);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Candidate Image");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            imageField.setText(filePath);
        }
    }

    private void registerCandidate(String fullName) {
        String name = nameField.getText();
        String imagePath = imageField.getText();

        if (name.isEmpty() || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {

            String imageName = name.replaceAll("\\s+", "_").toLowerCase() + ".png";

            String resourcesDirectoryPath = "resources/images";
            File resourcesDirectory = new File(resourcesDirectoryPath);

            if (!resourcesDirectory.exists()) {
                resourcesDirectory.mkdirs();
            }

            Path targetPath = Paths.get(resourcesDirectoryPath, imageName);

            try {
                Files.copy(Paths.get(imagePath), targetPath, StandardCopyOption.REPLACE_EXISTING);

                saveCandidateInfo(name, imageName);

                JOptionPane.showMessageDialog(this, "Candidate Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                new AdminDashboard(fullName);
                dispose();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving the image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveCandidateInfo(String name, String imageName) {
        File jsonFile = new File("resources/candidates.json");

        JSONArray candidatesArray = new JSONArray();

        if (jsonFile.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(jsonFile))) {

                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                JSONObject existingData = new JSONObject(jsonBuilder.toString());
                if (existingData.has("candidates")) {
                    candidatesArray = existingData.getJSONArray("candidates");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JSONObject candidate = new JSONObject();
        candidate.put("name", name);
        candidate.put("image", imageName);

        candidatesArray.put(candidate);

        try (FileWriter writer = new FileWriter(jsonFile)) {
            JSONObject candidatesData = new JSONObject();
            candidatesData.put("candidates", candidatesArray);

            writer.write(candidatesData.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new RegisterCandidate("Admin");
    }
}