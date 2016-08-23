package carec2.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class MyProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        String payload = exchange.getIn().getBody(String.class);
        // do something with the payload and/or exchange here
        exchange.setProperty("original-message", exchange.getIn().getBody());
//        exchange.getIn().setBody("Changed body");

    }
}