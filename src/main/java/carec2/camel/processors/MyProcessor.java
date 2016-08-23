package carec2.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        String payload = exchange.getIn().getBody(String.class);
        // do something with the payload and/or exchange here
//        exchange.setProperty("original-message", exchange.getIn().getBody());
//        exchange.getIn().setHeader("originalmessage", exchange.getIn());
//        Map<String, Object> headers = exchange.getIn().getHeaders();
////        exchange.getIn().setBody("Changed body");
//        exchange.getOut().setHeaders(headers);
//        exchange.getOut().setBody("Changed Body", String.class);
        System.out.println("ORIGINAL MESSAGE");
        System.out.println(payload);
        System.out.println(exchange.getIn().getBody());
        System.out.println(exchange.getProperty("orig"));

    }
}