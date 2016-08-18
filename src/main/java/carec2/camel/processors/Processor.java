package carec2.camel.processors;

import carec2.camel.ActiveMQProducer;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;


@Component
public class Processor {

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

    public void publishToQueue(String msg) throws IOException {

        String activemqHost = (System.getenv("ACTIVEMQ_SERVICE_HOST")== null) ? getPropValues("activemq-host") : System.getenv("ACTIVEMQ_SERVICE_HOST");
        String activemqPort = (System.getenv("ACTIVEMQ_SERVICE_PORT")== null) ? getPropValues("activemq-port") : System.getenv("ACTIVEMQ_SERVICE_PORT");

        String activemqUri = "tcp://" + activemqHost + ":" + activemqPort;

        // create sender
        ActiveMQProducer producer = new ActiveMQProducer(activemqUri, "admin", "admin");
        try {
            // attempt to add the message to an ActiveMQ Queue

            printMultilineMessageToScreen(msg);
            producer.sendMessage("demo.queue", msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void printMultilineMessageToScreen(String msg) throws IOException {
        File file = new File(msg);
        BufferedReader reader = new BufferedReader(new StringReader(msg));

        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }


}

