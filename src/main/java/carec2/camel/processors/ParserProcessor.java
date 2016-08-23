package carec2.camel.processors;


import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.parser.GenericModelClassFactory;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import carec2.domain.Patient;
import carec2.service.HL7MessageAssembler;
import carec2.service.HL7MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ParserProcessor {

    private static Logger log = LoggerFactory.getLogger(ParserProcessor.class);

    @Autowired
    HL7MessageService hl7MessageService;

    @Autowired
    HL7MessageAssembler hL7MessageAssembler;

    public void parseMessage(String message) {
        try {
            HapiContext context = new DefaultHapiContext();

            // set up
            context.setModelClassFactory(new GenericModelClassFactory());
            context.setValidationContext(ValidationContextFactory.noValidation());
            GenericMessage genMessage = (GenericMessage) context.getPipeParser().parse(message);
            Terser terser = new Terser(genMessage);

            String patientInfo = String.valueOf(terser.get("/PID-3-1"));
            if (!StringUtils.isEmpty(patientInfo)) {
                parsePatient(patientInfo, terser);
            }

        } catch (HL7Exception e) {
            log.error("Invalid Message: " + e.getMessage());
        }
    }

    private void parsePatient(String pInfo, Terser terser){
        Patient patient = hL7MessageAssembler.parsePatient(pInfo, terser);
        if (patient != null){
            hl7MessageService.savePatient(patient);
        }
    }
}
