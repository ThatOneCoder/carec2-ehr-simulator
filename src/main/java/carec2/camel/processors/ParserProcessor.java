package carec2.camel.processors;


import carec2.service.HL7MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParserProcessor {

    private static Logger log = LoggerFactory.getLogger(ParserProcessor.class);

    @Autowired
    HL7MessageService hl7MessageService;


    public String parseAndSavePatient(String message) {
        return hl7MessageService.parseAndSavePatient(message);
    }

    public String parseAndSaveEncounter(String message) {
        return hl7MessageService.parseAndSaveEncounter(message);
    }

    public String parseAndSaveEpisode(String message) {
        return hl7MessageService.parseAndSaveEpisode(message);
    }

}
