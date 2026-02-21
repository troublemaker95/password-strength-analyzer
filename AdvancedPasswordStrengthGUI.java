import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class AdvancedPasswordStrengthGUI {

    public static double calculateEntropy(String password) {
        int charsetSize = 0;

        if (password.matches(".*[a-z].*")) charsetSize += 26;
        if (password.matches(".*[A-Z].*")) charsetSize += 26;
        if (password.matches(".*[0-9].*")) charsetSize += 10;
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) charsetSize += 32;

        if (charsetSize == 0) return 0;

        return password.length() * (Math.log(charsetSize) / Math.log(2));
    }

    public static int calculateScore(String password) {
        int score = 0;

        if (password.length() >= 8) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) score++;

        return score;
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("🔐 Password Strength Analyzer");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Dark theme background
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Password Strength Checker");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel strengthLabel = new JLabel("Strength: ");
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        strengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel entropyLabel = new JLabel("Entropy: ");
        entropyLabel.setForeground(Color.LIGHT_GRAY);
        entropyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar strengthBar = new JProgressBar(0, 5);
        strengthBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        strengthBar.setStringPainted(false);

        JTextArea recommendations = new JTextArea(5, 20);
        recommendations.setEditable(false);
        recommendations.setBackground(new Color(40, 40, 40));
        recommendations.setForeground(Color.WHITE);
        recommendations.setFont(new Font("Monospaced", Font.PLAIN, 13));

        // Live update listener
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }

            private void update() {
                String password = new String(passwordField.getPassword());

                int score = calculateScore(password);
                double entropy = calculateEntropy(password);

                strengthBar.setValue(score);

                String strength;
                Color color;

                if (score <= 2) {
                    strength = "Weak";
                    color = Color.RED;
                } else if (score == 3 || score == 4) {
                    strength = "Medium";
                    color = Color.ORANGE;
                } else {
                    strength = "Strong";
                    color = Color.GREEN;
                }

                strengthLabel.setText("Strength: " + strength);
                strengthLabel.setForeground(color);
                strengthBar.setForeground(color);

                entropyLabel.setText(String.format("Entropy: %.2f bits", entropy));

                // Recommendations
                StringBuilder tips = new StringBuilder("Recommendations:\n");

                if (password.length() < 8)
                    tips.append("- Use at least 8 characters\n");
                if (!password.matches(".*[a-z].*"))
                    tips.append("- Add lowercase letters\n");
                if (!password.matches(".*[A-Z].*"))
                    tips.append("- Add uppercase letters\n");
                if (!password.matches(".*[0-9].*"))
                    tips.append("- Add numbers\n");
                if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"))
                    tips.append("- Add special characters\n");

                if (score == 5)
                    tips = new StringBuilder("Excellent password! ✅");

                recommendations.setText(tips.toString());
            }
        });

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(strengthBar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(strengthLabel);
        panel.add(entropyLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(recommendations);

        frame.add(panel);
        frame.setVisible(true);
    }
}