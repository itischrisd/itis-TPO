package client;

import javax.jms.JMSException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatView extends JFrame {

    private final ChatController controller;
    private JTextField inputField;
    private JTextArea chatArea;

    public ChatView(ChatController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Chat Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(500, 30));
        inputField.addActionListener(new SendButtonListener());

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        this.inputField = inputField;
    }

    private class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String text = inputField.getText();
                if (!text.trim().isEmpty()) {
                    controller.sendMessage(text);
                    inputField.setText("");
                }
            } catch (JMSException ex) {
                System.err.println("Failed to send message: " + ex.getMessage());
            }
        }
    }

    protected void displayMessage(String text) {
        chatArea.append(text + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
