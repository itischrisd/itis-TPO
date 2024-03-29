/**
 * @author Dudek Krzysztof S25692
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatClient extends Thread {

    private final String host;
    private final int port;
    private final String id;
    private final List<String> chatHistory;
    private final Charset charset = StandardCharsets.UTF_8;
    private SocketChannel clientChannel;
    private ByteBuffer buffer;
    private CharBuffer charBuffer;
    private boolean connected;

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        connected = false;
        chatHistory = new ArrayList<>();
        chatHistory.add("\n=== " + id + " chat view");
    }

    public void login() {
        try {
            clientChannel = SocketChannel.open(new InetSocketAddress(host, port));
            clientChannel.configureBlocking(false);
            buffer = ByteBuffer.allocate(clientChannel.socket().getReceiveBufferSize());
            pushMessageToServer(id + ";login; \n");
            connected = true;
            start();
        } catch (IOException e) {
            log(e);
        }
    }

    @Override
    public void run() {
        while (connected) {
            receiveMessages();
        }
    }

    private void receiveMessages() {
        try {
            String message = fetchMessageFromServer();
            if (message.equals("logout")) {
                connected = false;
            } else {
                chatHistory.add(message);
            }
        } catch (IOException e) {
            log(e);
        }
    }

    private String fetchMessageFromServer() throws IOException {
        StringBuilder sb = new StringBuilder();

        while (true) {
            if (charBuffer == null || !charBuffer.hasRemaining()) {
                int read = clientChannel.read(buffer);
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

    public void send(String message) {
        pushMessageToServer(id + ";send;" + message + "\n");
    }

    private void pushMessageToServer(String request) {
        try {
            clientChannel.write(charset.encode(request));
        } catch (IOException e) {
            log(e);
        }
    }

    public void logout() {
        try {
            pushMessageToServer(id + ";logout; \n");
            this.join();
            clientChannel.close();
        } catch (InterruptedException | IOException e) {
            log(e);
        }
    }

    public String getChatView() {
        StringBuilder view = new StringBuilder();
        for (String message : chatHistory) {
            view.append(message).append("\n");
        }
        return view.toString();
    }

    private void log(Exception e) {
        chatHistory.add("*** " + e.toString());
    }
}