package carec2.camel;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQProducer {

    private String brokerUrl;
    private String username;
    private String password;

    public ActiveMQProducer(final String brokerUrl, String username, String password) {
        this.brokerUrl = brokerUrl;
        this.username = username;
        this.password = password;
    }

    public void sendMessage(final String queueName, final String textMessage) throws Exception {
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

            // create producer

            MessageProducer producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage txtMessage = session.createTextMessage(textMessage);

            // send message now to the queue
            producer.send(txtMessage);

            System.out.println("Message sent to the queue: " + queueName);
        } catch (Exception e) {
            System.out.println("Exception while sending message to the queue" + e);
            throw e;
        } finally {

            // tidy up and clean connection and session
            if (connection != null) {
                connection.close();
                if (session != null) {
                    session.close();
                }
            }

        }

    }

}
