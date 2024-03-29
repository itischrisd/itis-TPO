package admin;

import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

import javax.jms.JMSException;
import java.net.MalformedURLException;

public class Configuration {

    private static final String URL = "tcp://localhost:3035";

    public static void main(String[] args) throws MalformedURLException, JMSException {
        JmsAdminServerIfc admin = AdminConnectionFactory.create(URL);

        for (Object destination : admin.getAllDestinations()) {
            String name = destination.toString().split("-")[0];
            admin.removeDestination(name);
        }

        boolean isQueue = false;
        admin.addDestination("ChatTopic", isQueue);
    }
}
