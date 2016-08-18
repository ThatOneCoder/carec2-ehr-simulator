package carec2.camel.routes;

import carec2.camel.processors.Processor;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.boot.FatJarRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Router extends FatJarRouter {
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


        from("netty4:tcp://" + ehrServer + ":" + ehrPort + "?sync=true").routeId("Audit-Camel-Route")
//                .onException(Exception.class).handled(true)
//                .setExchangePattern(ExchangePattern.InOnly)
                .to("bean:processor?method=print")
                .transform().simple("bean:hl7MessageService?method=createHL7Message")
                .to("mongodb:mongo?database=microservices&collection=messages&operation=insert")
                .to("bean:processor?method=publishToQueue")
                .to("bean:respondACK?method=process");

      from("file:" + hl7Dir + "?noop=true").routeId("EHR-Camel-Route")
                .unmarshal()
                .hl7(false)
                .to("netty4:tcp://" + ehrServer + ":" + ehrPort + "?sync=true")
                .to("bean:respondACK?method=process");

//        from("mllpport").routeId("Audit-Camel-Route")
//                .onException(Exception.class).handled(true)
//                .setExchangePattern(ExchangePattern.InOnly)
//                .to("mongodb:mongo?database=microservices&collection=messages&operation=insert")
//                .to("bean:processor?method=publishToQueue")
//                .to("bean:respondACK?method=process");

//        from("activemq:").routeId("Validation-Camel-Route")
//                .onException(Exception.class).handled(true)
//                .setExchangePattern(ExchangePattern.InOnly)
//                .to("bean:processValidation?method...")
//                .to("bean:processor?method=publishToQueue")
//                .to("bean:respondACK?method=process");
    }

}
