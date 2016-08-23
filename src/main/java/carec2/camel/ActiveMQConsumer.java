package carec2.camel;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.parser.*;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.*;
import java.util.Properties;

public class ActiveMQConsumer {

    private String brokerUrl;
    private String username;
    private String password;

    public ActiveMQConsumer(final String brokerUrl, String username, String password) {
        this.brokerUrl = brokerUrl;
        this.username = username;
        this.password = password;
    }


    public String receiveNextMessage(final String queueName) throws Exception {
        Connection connection = null;
        Session session = null;
        try {
            // get the connection factory
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.username, this.password, this.brokerUrl);
            // create connection
            connection = connectionFactory.createConnection();

            // start
            connection.start();

            // create session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // create queue (it will create if queue doesn't exist)
            Destination queue = session.createQueue(queueName);


            MessageConsumer consumer = session.createConsumer(queue);

            TextMessage msg = (TextMessage) consumer.receive();

            consumer.close();
            session.close();
            connection.close();

            return msg.getText();

        } catch (Exception e) {
            System.out.println("Exception while sending message to the queue" + e);
            throw e;
        }
    }
}
