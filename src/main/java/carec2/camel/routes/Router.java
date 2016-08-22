package carec2.camel.routes;

import carec2.camel.processors.AuditProcessor;
import carec2.camel.processors.FilterProcessor;
import carec2.camel.processors.Processor;
import carec2.camel.processors.ValidationProcessor;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.boot.FatJarRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Router extends FatJarRouter {
    private static Logger log = LoggerFactory.getLogger(Router.class);

    @Autowired
    ValidationProcessor validationProcessor;

    @Autowired
    FilterProcessor filterProcessor;

    @Autowired
    Processor processor;

    @Autowired
    AuditProcessor auditProcessor;


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

        String mllp = "netty4:tcp://" + ehrServer + ":" + ehrPort + "?sync=true";



      // EHR Simulator
      from("file:" + hl7Dir + "?noop=true").routeId("EHR-Simulator")
                .unmarshal()
                .hl7(false)
                .to(mllp);

      // Audit Route
        from(mllp).routeId("Audit-Camel-Route")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
                .bean(auditProcessor, "processMessage")
                .end();

//        from("activemq:").routeId("Validation-Camel-Route")
//                .onException(Exception.class).handled(true)
//                .setExchangePattern(ExchangePattern.InOnly)
//                .to("bean:processValidation?method...")
//                .to("bean:processor?method=publishToQueue")
//                .to("bean:respondACK?method=process");

        /*from("activemq:")*/
      // Validation Route
//        from(mllp).routeId("Validation-Camel-Route")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
//                .bean(validationProcessor, "process")
//                .choice()
//                    .when()
//                        .simple("${body} == true")
////                        .log("publish to QUEUE")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
//                        .log(LoggingLevel.INFO, "publish to QUEUE")
//                        .bean(processor, "publishToQueue(${body}, storeToFS)")
//                        .to("mock:filterQueue")
//                    .otherwise()
////                        .simple("${body} != true")
////                        .log("do NOT publish to QUEUE")
//                        .log(LoggingLevel.INFO, "do NOT publish to QUEUE")
////                        .to("log:carec2?level=INFO&showBody=true")
////                        .to("log:carec2?level=INFO&showAll=true")
//                        .to("mock:doNothing")
////                        .end()
////                .to("bean:respondACK?method=process")
//                .end();
//                .to("bean:validationProcessor?method=process")
//                .to("bean:processor?method=publishToQueue")

    }

}
