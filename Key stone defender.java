import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class GUI {
    private JFrame frame;
    private JTextField keyField;
    private JTextArea logArea;
    private JButton encryptButton;
    private JButton decryptButton;

    public GUI() {
        frame = new JFrame("KEYSTONE DEFENDER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JLabel keyLabel = new JLabel("Enter Encryption Key:");
        keyField = new JTextField(20);
        topPanel.add(keyLabel);
        topPanel.add(keyField);

        JPanel buttonPanel = new JPanel();
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performEncryption();
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performDecryption();
            }
        });

        frame.setVisible(true);
    }

    private void performEncryption() {
        String keyString = keyField.getText();
        if (keyString.isEmpty()) {
            log("Please enter a valid key.");
            return;
        }

        try {
            Key secretKey = new SecretKeySpec(keyString.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File inputFile = fileChooser.getSelectedFile();
                byte[] inputBytes = new byte[(int) inputFile.length()];
                FileInputStream inputStream = new FileInputStream(inputFile);
                inputStream.read(inputBytes);
                inputStream.close();

                byte[] encryptedBytes = cipher.doFinal(inputBytes);

                JFileChooser saveFileChooser = new JFileChooser();
                int saveResult = saveFileChooser.showSaveDialog(frame);

                if (saveResult == JFileChooser.APPROVE_OPTION) {
                    File outputFile = saveFileChooser.getSelectedFile();
                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    outputStream.write(encryptedBytes);
                    outputStream.close();

                    log("Encryption successful. Encrypted file saved.");
                }
            }
        } catch (Exception ex) {
            log("Encryption error: " + ex.getMessage());
        }
    }

    private void performDecryption() {
        String keyString = keyField.getText();
        if (keyString.isEmpty()) {
            log("Please enter a valid key.");
            return;
        }

        try {
            Key secretKey = new SecretKeySpec(keyString.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File inputFile = fileChooser.getSelectedFile();
                byte[] inputBytes = new byte[(int) inputFile.length()];
                FileInputStream inputStream = new FileInputStream(inputFile);
                inputStream.read(inputBytes);
                inputStream.close();

                byte[] decryptedBytes = cipher.doFinal(inputBytes);

                JFileChooser saveFileChooser = new JFileChooser();
                int saveResult = saveFileChooser.showSaveDialog(frame);

                if (saveResult == JFileChooser.APPROVE_OPTION) {
                    File outputFile = saveFileChooser.getSelectedFile();
                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    outputStream.write(decryptedBytes);
                    outputStream.close();

                    log("Decryption successful. Decrypted file saved.");
                }
            }
        } catch (Exception ex) {
            log("Decryption error: " + ex.getMessage());
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }
}
