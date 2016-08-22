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

// java libs

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

// hapi libs

@Component
public class ValidationProcessor {

    public Boolean process(Message in) throws Exception {

        // convert incoming message to a string
        String msg = in.toString();

        // print message to console
//        System.out.println("---MESSAGE TO CONSOLE---");
//        printMultilineMessageToScreen(msg);

//        System.out.println("Is this HL7 message valid? |" + validateHL7(msg) + "|");
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

    private void printMultilineMessageToScreen(String msg) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(msg));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}

