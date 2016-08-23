package carec2.camel.processors;

/*
*   --------------------
*   Validation Processor
*   --------------------
*   Purpose:
*       The validation processor will take in an HL7 message and validates that it is a proper HL7 message
*
*
* */

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.springframework.stereotype.Component;

@Component
public class ValidationProcessor {

    private String message;

    public Boolean process(Message in) throws Exception {

        // convert incoming message to a string
        String msg = in.toString();

        return validateHL7(msg);
    }

    private boolean validateHL7(String msg) {
        boolean response;

        HapiContext context = new DefaultHapiContext();
        context.setValidationContext(ValidationContextFactory.defaultValidation());
        PipeParser pipeParser = context.getPipeParser();

        try {
            pipeParser.parse(msg);
            response = true;
        } catch (HL7Exception e) {
            response = false;
            System.out.println("Invalid Message: \n" + e.getMessage());
        }

        return response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(Message in) {
        String message = in.toString();
        System.out.println("SET MESSAGE");
        System.out.println(message);
        this.message = message;
    }
}

