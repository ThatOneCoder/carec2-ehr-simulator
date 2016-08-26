package carec2.camel.processors;

import carec2.camel.ActiveMQConsumer;
import carec2.camel.ActiveMQProducer;
import carec2.service.HL7MessageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;


@Component
public class RouterProcessor {

    @Autowired
    private HL7MessageAssembler HL7MessageAssembler;

    public String getPropValues(String property) throws IOException {
        String result = "";
        InputStream inputStream = null;

        try {
            Properties prop = new Properties();
            String propFileName = "endpoint.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            result = prop.getProperty(property);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }

    public void enqueueMessage(String msg, String channel) throws IOException {

        String activemqHost = getPropValues("activemq-host");
        String activemqPort = getPropValues("activemq-port");

        String activemqUri = "tcp://" + activemqHost + ":" + activemqPort;

        // create sender
        ActiveMQProducer producer = new ActiveMQProducer(activemqUri, "admin", "admin");
        try {
            // attempt to add the message to an ActiveMQ Queue

//            printMultilineMessageToScreen(msg);
            producer.sendMessage(channel, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String dequeueMessage(String channel) throws Exception {

        ActiveMQConsumer activeMQConsumer = new ActiveMQConsumer("", "", "");

        String activemqHost = getPropValues("activemq-host");
        String activemqPort = getPropValues("activemq-port");
        String activemqUri = "tcp://" + activemqHost + ":" + activemqPort;

        ActiveMQConsumer consumer = new ActiveMQConsumer(activemqUri, "admin", "admin");
        String msg;
//            consumer.startReceiving("demo.queue");
        msg = consumer.receiveNextMessage(channel);
        return msg;
    }


    public void printMultilineMessageToScreen(String msg) throws IOException {
        File file = new File(msg);
        BufferedReader reader = new BufferedReader(new StringReader(msg));

        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public void print(String msg) {
        System.out.println("RAW MESSAGE");
        System.out.println("TESTING");
        System.out.println(msg);
    }


}
