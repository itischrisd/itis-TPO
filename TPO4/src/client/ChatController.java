package client;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.*;

public class ChatController {

    private ChatClient client;
    private ChatView view;

    public static void main(String[] args) {
        new ChatController();
    }

    public ChatController() {
        SwingUtilities.invokeLater(() -> {
            String username = JOptionPane.showInputDialog(null, "Enter your username:", "Username", JOptionPane.PLAIN_MESSAGE);
            if (username != null && !username.trim().isEmpty()) {
                try {
                    client = new ChatClient(this, username);
                    view = new ChatView(this);
                    view.setVisible(true);
                } catch (NamingException | JMSException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to initialize chat client: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    protected void sendMessage(String text) throws JMSException {
        client.sendMessage(text);
    }

    protected void displayMessage(String text) {
        view.displayMessage(text);
    }
}
