import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileEncryptorDecryptor extends JFrame {
    private JTextField filePathField;
    private JButton encryptButton;
    private JButton decryptButton;
    private JComboBox<String> operationSelector;

    public FileEncryptorDecryptor() {
        // Set up the frame
        setTitle("File Encryptor/Decryptor");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        filePathField = new JTextField(20);
        encryptButton = new JButton("Encrypt File");
        decryptButton = new JButton("Decrypt File");
        operationSelector = new JComboBox<>(new String[]{"Encrypt", "Decrypt"});

        // Set up the layout
        JPanel panel = new JPanel();
        panel.add(new JLabel("File Path:"));
        panel.add(filePathField);
        panel.add(operationSelector);
        panel.add(encryptButton);
        panel.add(decryptButton);

        add(panel);

        // Add action listeners
        encryptButton.addActionListener(e -> processFile(true));
        decryptButton.addActionListener(e -> processFile(false));
    }

    private void processFile(boolean isEncryption) {
        String filePath = filePathField.getText().trim();
        String outputFilePath = isEncryption ? "encrypted_" + new File(filePath).getName() : "decrypted_" + new File(filePath).getName();
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String processedContent = isEncryption ? encrypt(content) : decrypt(content);
            writeToFile(outputFilePath, processedContent);
            JOptionPane.showMessageDialog(this, "File processed successfully: " + outputFilePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error processing file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String encrypt(String data) {
        return caesarCipher(data, 3); // Shift of 3 for encryption
    }

    private String decrypt(String data) {
        return caesarCipher(data, -3); // Shift of -3 for decryption
    }

    private String caesarCipher(String data, int shift) {
        StringBuilder result = new StringBuilder();

        for (char ch : data.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                result.append((char) ((ch - base + shift + 26) % 26 + base));
            } else {
                result.append(ch); // Non-letter characters remain the same
            }
        }
        return result.toString();
    }

    private void writeToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileEncryptorDecryptor app = new FileEncryptorDecryptor();
            app.setVisible(true);
        });
    }
}
