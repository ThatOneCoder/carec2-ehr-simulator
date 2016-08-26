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
    RestProcessor restProcessor;

    @Autowired
    ParserProcessor parserProcessor;

    @Override
    public void configure() throws Exception {
          onException(Exception.class)
                  .log(LoggingLevel.ERROR, "carec2.camel.routes", "Unexpected exception ${exception}");

        String hl7Dir = routerProcessor.getPropValues("hl7-message-dir");
        String ehrServer = routerProcessor.getPropValues("ehr.server");
        String ehrPort = routerProcessor.getPropValues("ehr.port");
        String storeFSDir = routerProcessor.getPropValues("storeFS-dir");

        String mllp = "netty4:tcp://" + ehrServer + ":" + ehrPort + "?sync=true";

//      // EHR Simulator
//      from("file:" + hl7Dir + "?noop=true").routeId("EHR-Simulator")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'EHR-Simulator-Route'")
//                .log(LoggingLevel.INFO, "RAW Message")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Unmarshalling Message")
//                .unmarshal()
//                .hl7(false)
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Unmarshalled")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'EHR-Simulator-Route'")
//                .to(mllp);

      // Audit Route
        from(mllp).routeId("Audit-Camel-Route")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Audit-Camel-Route'")
                .log(LoggingLevel.INFO, "RAW Message")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Recording Message in MongoDB")
                .bean(auditProcessor, "processMessage")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Recorded in MongoDB")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueuing Message")
                .bean(routerProcessor, "enqueueMessage(${body}, validate)")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Enqueued")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Audit-Camel-Route")
                .end();

      // Validation Route
        from("timer://foo?fixedRate=true&period=2000").routeId("Validation-Camel-Route")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Validation-Camel-Route'")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Dequeuing Message")
                .bean(routerProcessor, "dequeueMessage(validate)")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Saving Original Message to Bean")
                .bean(validationProcessor, "setMessage")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Saved Original Message to Bean")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
                .log(LoggingLevel.INFO, "RAW Message")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Validating Message")
                .bean(validationProcessor, "process")
                .choice()
                    .when()
                        .simple("${body} == true")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Valid")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Fetching Original Message from Bean")
                        .bean(validationProcessor, "getMessage")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Fetched Original Message from Bean")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueuing to 'filter' Queue")
                        .bean(routerProcessor, "enqueueMessage(${body}, filter)")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueued to 'filter' Queue")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Validation-Camel-Route")
                    .otherwise()
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Not Valid")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Fetching Original Message from Bean")
                        .bean(validationProcessor, "getMessage")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Fetched Original Message from Bean")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueuing to 'fail.validate' Queue")
                        .bean(routerProcessor, "enqueueMessage(${body}, fail.validate)")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueued to 'fail.validate' Queue")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Ignored")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Validation-Camel-Route")
                .end();

        // Filter Route
        from("timer://foo2?fixedRate=true&period=1000").routeId("Filter-Camel-Route")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Filter-Camel-Route'")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Dequeuing Message")
                .bean(routerProcessor, "dequeueMessage(filter)")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Saving Original Message to Bean")
                .bean(filterProcessor, "setMessage")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Saved Original Message to Bean")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
                .log(LoggingLevel.INFO, "RAW Message")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Validating Message")
                .bean(filterProcessor, "process")
                .choice()
                    .when()
                        .simple("${body} == true")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Valid")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Fetching Original Message from Bean")
                        .bean(filterProcessor, "getMessage")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Fetched Original Message from Bean")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueuing to 'store' Queue")
                        .bean(routerProcessor, "enqueueMessage(${body}, store)")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueued to 'store' Queue")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Validation-Camel-Route")
                .otherwise()
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Not Valid")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Fetching Original Message from Bean")
                        .bean(filterProcessor, "getMessage")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Fetched Original Message from Bean")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueuing to 'fail.filter' Queue")
                        .bean(routerProcessor, "enqueueMessage(${body}, fail.filter)")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Enqueuing to 'fail.filter' Queue")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Ignored")
                        .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Filter-Camel-Route")
                .end();

        //StoreToFS Route
        from("timer://foo3?fixedRate=true&period=2000").routeId("StoreToFS-Camel-Route")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
                .bean(routerProcessor, "dequeueMessage(store)")
                .convertBodyTo(byte[].class)
                //     .to("file:C:/output/?fileName=${date:now:yyyyMMdd}/something.txt")
                .to("file:"+storeFSDir)
                .bean(routerProcessor, "enqueueMessage(${body}, parser)")
                .end();

        // Parser Route
        from("timer://foo4?fixedRate=true&period=1000").routeId("Parser-Camel-Route")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Parser-Camel-Route'")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Dequeuing Message")
                .bean(routerProcessor, "dequeueMessage(parser)")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
                .bean(parserProcessor, "setMessage(${body})")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Parse/Record Patient Message")
                .bean(parserProcessor, "parseAndSavePatient")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Sending Notification Patient Update")
                .bean(restProcessor, "process(patient, ${body})")
                .bean(parserProcessor, "getMessage")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Parse/Record Episode Message")
                .bean(parserProcessor, "parseAndSaveEpisode")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Sending Notification Episode Update")
                .bean(restProcessor, "process(episode, ${body})")
                .bean(parserProcessor, "getMessage")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Parse/Record Encounter Message")
                .bean(parserProcessor, "parseAndSaveEncounter")
                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Sending Notification Encounter Update")
                .bean(restProcessor, "process(encounter, ${body})")
                .bean(parserProcessor, "getMessage")
                .bean(routerProcessor, "enqueueMessage(${body}, complete)")
                .end();

//        from("timer://foo3?fixedRate=true&period=3000").routeId("Notification-Camel-Route")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'Notification-Camel-Route'")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Sending Notification")
//                .bean(routerProcessor, "dequeueMessage(store)") //should be notify
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Dequeued")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Sending Notification")
//                .bean(restProcessor, "process(patient, 9011)")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Sent Notification")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Response: ${body}")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Sending Notification")
//                .bean(restProcessor, "process(encounter, 1234)")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Sent Notification")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Notification Status: Response: ${body}")
//                .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Ending 'Notification-Camel-Route'")
//                .end();
    }

}
