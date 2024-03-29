/**
 *
 *  @author Dudek Krzysztof S25692
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ChatClientTask extends FutureTask<ChatClient> {

    private ChatClientTask(ChatClient client, List<String> messages, int wait) {
        super(() -> {
            try {
                client.login();
                if (wait > 0) {
                    Thread.sleep(wait);
                }
                for (String message : messages) {
                    client.send(message);
                    if (wait > 0) {
                        Thread.sleep(wait);
                    }
                }
                client.logout();
                if (wait > 0) {
                    Thread.sleep(wait);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return client;
        });
    }

    public static ChatClientTask create(ChatClient client, List<String> messages, int wait) {
        return new ChatClientTask(client, messages, wait);
    }

    public ChatClient getClient() {
        try {
            return this.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
