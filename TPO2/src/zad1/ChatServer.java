/**
 * @author Dudek Krzysztof S25692
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChatServer extends Thread {

    private final String host;
    private final int port;
    private final Charset charset = StandardCharsets.UTF_8;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer buffer;
    private CharBuffer charBuffer;
    private List<String> logs;
    private Map<String, SocketChannel> clients;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        buffer = ByteBuffer.allocate(1024);
        logs = new ArrayList<>();
        clients = new HashMap<>();
        start();
        System.out.println("\nServer started");
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                selector.select();

                if (this.isInterrupted()) break;

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        fetchAndHandleRequest(client);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    private void fetchAndHandleRequest(SocketChannel sender) throws IOException {
        do {
            String message = fetchRequestFromSocketChannel(sender);
            handleRequestLogic(sender, message);
        } while (charBuffer.hasRemaining());
    }

    private String fetchRequestFromSocketChannel(SocketChannel socketChannel) throws IOException {
        StringBuilder sb = new StringBuilder();

        while (true) {
            if (charBuffer == null || !charBuffer.hasRemaining()) {
                int read = socketChannel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    charBuffer = charset.decode(buffer);
                    buffer.clear();
                }
            } else {
                while (charBuffer.hasRemaining()) {
                    char c = charBuffer.get();
                    if (c == '\n') {
                        return sb.toString();
                    }
                    sb.append(c);
                }
            }
        }
    }

    private void handleRequestLogic(SocketChannel sender, String message) throws IOException {
        String[] messageParts = message.split(";");
        String senderName = messageParts[0];
        String command = messageParts[1];
        String messageContent = messageParts[2];
        String response = senderName;
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

        switch (command) {
            case "login":
                response += " logged in\n";
                clients.put(senderName, sender);
                break;
            case "logout":
                response += " logged out\n";
                break;
            case "send":
                response += ": " + messageContent + "\n";
                break;
        }

        logs.add(time + " " + response);
        sendMessageToAllClients(response);

        if (command.equals("logout")) {
            sender.write(charset.encode("logout\n"));
            clients.remove(senderName);
            sender.close();
        }
    }

    private void sendMessageToAllClients(String message) throws IOException {
        for (SocketChannel client : clients.values()) {
            client.write(charset.encode(message));
        }
    }

    public void stopServer() throws IOException {
        interrupt();
        serverSocketChannel.close();
        selector.close();
        System.out.println("\nServer stopped");
    }

    public String getServerLog() {
        StringBuilder log = new StringBuilder();
        for (String entry : logs) {
            log.append(entry);
        }
        return log.toString();
    }
}
