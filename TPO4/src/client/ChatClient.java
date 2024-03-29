package client;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatClient implements MessageListener {

    private final ChatController controller;
    private final String username;
    private TopicSession pubSession;
    private TopicPublisher publisher;

    public ChatClient(ChatController controller, String username) throws NamingException, JMSException {
        this.controller = controller;
        this.username = username;
        initializeJMS();
    }

    private void initializeJMS() throws NamingException, JMSException {
        InitialContext ctx = new InitialContext();
        TopicConnectionFactory conFactory = (TopicConnectionFactory) ctx.lookup("ConnectionFactory");
        TopicConnection connection = conFactory.createTopicConnection();
        pubSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicSession subSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic chatTopic = (Topic) ctx.lookup("ChatTopic");
        publisher = pubSession != null ? pubSession.createPublisher(chatTopic) : null;
        TopicSubscriber subscriber = subSession.createSubscriber(chatTopic);
        subscriber.setMessageListener(this);
        connection.start();
    }

    protected void sendMessage(String text) throws JMSException {
        LocalTime localTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        text = "[" + localTime.format(formatter) + "] " + username + ": " + text;
        TextMessage message = pubSession.createTextMessage();
        message.setText(text);
        publisher.publish(message);
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                TextMessage textMessage = (TextMessage) message;
                controller.displayMessage(textMessage.getText());
            } catch (JMSException e) {
                System.err.println("Failed to process message: " + e.getMessage());
            }
        }
    }
}
