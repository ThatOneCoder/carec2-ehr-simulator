package carec2.camel.routes;

import carec2.camel.processors.Processor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Router extends SpringRouteBuilder {
    private static Logger log = LoggerFactory.getLogger(Router.class);

    @Override
    public void configure() throws Exception {
       // onException(Exception.class).handled(true)
           //     .bean(MessageService.class, "messageFailed")
          //      .transform().simple("Error processing this message.");

        onException(Exception.class)
                .log(LoggingLevel.ERROR, "carec2.camel.routes", "Unexpected exception ${exception}");

        Processor processor = new Processor();
        String hl7Dir = processor.getPropValues("hl7-message-dir");
        String ehrServer = processor.getPropValues("ehr.server");
        String ehrPort = processor.getPropValues("ehr.port");

//      from("file:" + hl7Dir + "?noop=true").routeId("EHR-Camel-Route")
//                .unmarshal()
//                .hl7(false)
//                .to("mllpport")
//                .to("bean:respondACK?method=process");

        from("netty4:tcp://" + ehrServer + ":" + ehrPort + "?sync=true").routeId("Audit-Camel-Route")
//                .onException(Exception.class).handled(true).process(new org.apache.camel.Processor() {
//                    @Override
//                    public void process(Exchange exchange) {
//                        exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class).printStackTrace();
//                    }
//                })
                .convertBodyTo(String.class)
                .to("bean:processorAudit?method=processMessage")
//                .to("bean:processor?method=publishToQueue")
                .to("bean:respondACK?method=process");

        from("file:" + hl7Dir + "?noop=true").routeId("EHR-Camel-Route")
                .unmarshal()
                .hl7(false)
                .to("netty4:tcp://" + ehrServer + ":" + ehrPort + "?sync=true")
                .end();


//        from("activemq:").routeId("Validation-Camel-Route")
//                .onException(Exception.class).handled(true)
//                .setExchangePattern(ExchangePattern.InOnly)
//                .to("bean:processValidation?method...")
//                .to("bean:processor?method=publishToQueue")
//                .to("bean:respondACK?method=process");
    }

}
