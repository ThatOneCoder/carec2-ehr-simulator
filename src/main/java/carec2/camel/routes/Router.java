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
    RouterProcessor routerProcessor;

    @Override
    public void configure() throws Exception {
        onException(Exception.class)
                .log(LoggingLevel.ERROR, "carec2.camel.routes", "Unexpected exception ${exception}");

        String hl7Dir = routerProcessor.getPropValues("hl7-message-dir");
        String ehrServer = routerProcessor.getPropValues("ehr.server");
        String ehrHl7Port = routerProcessor.getPropValues("ehr.hl7.port");
        String ehrCsvPort = routerProcessor.getPropValues("ehr.csv.port");

        String mllp_hl7 = "netty4:tcp://" + ehrServer + ":" + ehrHl7Port + "?sync=true&keepAlive=true";
        String mllp_csv = "netty4:tcp://" + ehrServer + ":" + ehrCsvPort + "?sync=true&keepAlive=true";

//      // EHR Simulator
        from("file:" + hl7Dir + "?noop=true").routeId("EHR-Simulator")
                .choice()
                    .when()
                        .simple("${file:ext} == 'csv'")
                            .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'EHR-Simulator-Route'")
                            .log(LoggingLevel.INFO, "RAW CSV Message")
                            .unmarshal()
                            .csv()
                            .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
                            .to(mllp_csv)
                        .otherwise()
                            .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Starting 'EHR-Simulator-Route'")
                            .log(LoggingLevel.INFO, "RAW HL7 Message")
                            .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "${body}")
                            .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Unmarshalling Message")
                            .unmarshal()
                            .hl7(false)
                            .log(LoggingLevel.INFO, "carec2.camel.routes.Router", "Message Status: Message Unmarshalled")
                            .to(mllp_hl7)
                .end();
    }

}
