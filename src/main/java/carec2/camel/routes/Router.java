package carec2.camel.routes;

import carec2.camel.processors.*;
import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Router extends SpringRouteBuilder {
    private static Logger log = LoggerFactory.getLogger(Router.class);

    @Autowired
    ValidationProcessor validationProcessor;

    @Autowired
    FilterProcessor filterProcessor;

    @Autowired
    RouterProcessor routerProcessor;

    @Autowired
    AuditProcessor auditProcessor;

    @Autowired
    MyProcessor processor;

    @Autowired
    ParserProcessor parserProcessor;

    @Override
    public void configure() throws Exception {
          onException(Exception.class)
                  .log(LoggingLevel.ERROR, "carec2.camel.routes", "Unexpected exception ${exception}");

        RouterProcessor routerProcessor = new RouterProcessor();
        String hl7Dir = routerProcessor.getPropValues("hl7-message-dir");
        String ehrServer = routerProcessor.getPropValues("ehr.server");
        String ehrPort = routerProcessor.getPropValues("ehr.port");

        String mllp = "netty4:tcp://" + ehrServer + ":" + ehrPort + "?sync=true";



      // EHR Simulator
      from("file:" + hl7Dir + "?noop=true").routeId("EHR-Simulator")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'EHR-Simulator-Route'")
                .log(LoggingLevel.INFO, "RAW Message")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Unmarshalling Message")
                .unmarshal()
                .hl7(false)
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Unmarshalled")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'EHR-Simulator-Route'")
                .to(mllp);

        from(mllp).routeId("Parse-Camel-Route")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Parse-Camel-Route'")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Dequeuing Message")
//                .bean(routerProcessor, "dequeueMessage(validate)")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
                .bean(parserProcessor, "setMessage(${body})")
                .bean(parserProcessor, "parseAndSavePatient")
                .bean(parserProcessor, "getMessage")
                .bean(parserProcessor, "parseAndSaveEncounter")
                .bean(parserProcessor, "getMessage")
                .bean(parserProcessor, "parseAndSaveEpisode")
                .end();

//      // Audit Route
//          from(mllp).routeId("Audit-Camel-Route")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Audit-Camel-Route'")
//                .log(LoggingLevel.INFO, "RAW Message")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Recording Message in MongoDB")
//                .bean(auditProcessor, "processMessage")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Recorded in MongoDB")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueuing Message")
//                .bean(routerProcessor, "enqueueMessage(${body}, validate)")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Enqueued")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Audit-Camel-Route")
//                .end();



          // Validation Route
//        from(mllp).routeId("Validation-Camel-Route")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
//                .bean(validationProcessor, "process")

//        from("activemq:").routeId("Validation-Camel-Route")
//                .onException(Exception.class).handled(true)
//                .setExchangePattern(ExchangePattern.InOnly)
//                .to("bean:processValidation?method...")
//                .to("bean:processor?method=publishToQueue")
//                .to("bean:respondACK?method=process");

        /*from("activemq:")*/
      // Validation Route
//        from("timer://foo?fixedRate=true&period=2000").routeId("Validation-Camel-Route")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Validation-Camel-Route'")
//                .process(processor)
////                .setProperty("OriginalMessage", body())
////                .log(LoggingLevel.INFO, "RAWWWWW", body().toString())
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Dequeuing Message")
//                .bean(routerProcessor, "dequeueMessage(validate)")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
//                .log(LoggingLevel.INFO, "RAW Message")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Validating Message")
//                .bean(validationProcessor, "process")
//                .choice()
//                    .when()
//                        .simple("${body} == true")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Valid")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Publishing to 'store' Queue")
//                        .setExchangePattern(ExchangePattern.InOnly)
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${header.OriginalMessage}")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${property.OriginalMessage}")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", body().toString())
//                        .bean(routerProcessor, "enqueueMessage(${property.OriginalMessage}, filter)")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Published to 'filter' Queue")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Validation-Camel-Route")
//                    .otherwise()
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Not Valid")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Ignored")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Validation-Camel-Route")
//                .end();
//
//        // Filter Route
//        from("timer://foo?fixedRate=true&period=2000").routeId("Filter-Camel-Route")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Filter-Camel-Route'")
////                .setProperty("OriginalMessage", body())
////                .log(LoggingLevel.INFO, "RAWWWWW", body().toString())
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Dequeuing Message")
//                .bean(routerProcessor, "dequeueMessage(filter)")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
//                .log(LoggingLevel.INFO, "RAW Message")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Filtering Message")
//                .bean(filterProcessor, "process")
//                .choice()
//                    .when()
//                        .simple("${body} == true")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Valid")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Publishing to 'store' Queue")
//                        .setExchangePattern(ExchangePattern.InOnly)
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${header.OriginalMessage}")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${property.OriginalMessage}")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", body().toString())
//                        .bean(routerProcessor, "enqueueMessage(${property.OriginalMessage}, store)")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Published to 'store' Queue")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Filter-Camel-Route")
//                .otherwise()
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Not Valid")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Ignored")
//                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Filter-Camel-Route")
//                .end();

//        //StoreToFS Route
//        from("timer://foo?fixedRate=true&period=2000").routeId("StoreToFS-Camel-Route")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
//                .bean(routerProcessor, "dequeueMessage(validate)")
//                .convertBodyTo(byte[].class)
//                //     .to("file:C:/output/?fileName=${date:now:yyyyMMdd}/something.txt")
//                .to("file:C:/output/")
//                .end();

        //Parse Route
      //  from("timer://foo?fixedRate=true&period=2000").routeId("Parse-Camel-Route")
//        from(mllp).routeId("Parse-amel-Route")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Parse-Camel-Route'")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Dequeuing Message")
//                .bean(routerProcessor, "dequeueMessage(validate)")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
//                .bean(parserProcessor, "parseAndSaveMessage")
//                .end();
    }

}
