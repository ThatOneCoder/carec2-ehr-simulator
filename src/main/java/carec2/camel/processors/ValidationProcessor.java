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
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.GenericModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.apache.http.util.TextUtils;
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

        CanonicalModelClassFactory mcf = new CanonicalModelClassFactory("2.5.1");


        HapiContext context = new DefaultHapiContext();
        context.setValidationContext(ValidationContextFactory.defaultValidation());
        context.setModelClassFactory(mcf);
        PipeParser pipeParser = context.getPipeParser();

        // start validation process

        // default HAPI validation (very light validation without conformance profiles
        try {
            pipeParser.parse(msg);
            response = true;
        } catch (HL7Exception e) {
            response = false;
            System.out.println("Invalid Message: \n" + e.getMessage());
        }

        //TODO: custom validation (per Sylvia)
        try {
            if (hasRequiredFields(msg) && hasPV1(msg) && hasPV1AfterPID(msg)) {
                response = true;
            } else {
                response = false;
            }
        } catch (HL7Exception e) {
            response = false;
            e.printStackTrace();
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

    private boolean hasRequiredFields(String msg) throws HL7Exception {
        boolean response = true;

        //TODO: if the message has a missing values for EVN-2, PID-3 or PV1-2, return false, else return true

        // create hapi context
        HapiContext context = new DefaultHapiContext();

        // for now we only have ADT messages
//        if (msg.contains("ADT")) {
////            System.out.println(msg.indexOf("A03"));
//
//        }

        // set up
        context.setModelClassFactory(new GenericModelClassFactory());
        context.setValidationContext(ValidationContextFactory.noValidation());
        GenericMessage genMessage = (GenericMessage) context.getPipeParser().parse(msg);
        Terser terser = new Terser(genMessage);

        // parse message for content
        String evn2 = String.valueOf(terser.get("/EVN-2"));
        String pid3 = String.valueOf(terser.get("/PID-3"));
        String pv12 = String.valueOf(terser.get("/PV1-2"));

        if (evn2.equals("null")) {
            System.out.println("Message Error: 'Missing Required Field' - [EVN-2] (Event Date)");
        }

        if(pid3.equals("null")) {
            System.out.println("Message Error: 'Missing Required Field' - [PID-3] (Patient ID)");
        }

        if(pv12.equals("null")) {
            System.out.println("Message Error: 'Missing Required Field' - [PV1-2] (Patient Class)");
        }

        if (evn2.equals("null") || pid3.equals("null") || pv12.equals("null")) {
            response = false;
        }

        return response;
    }

    private boolean hasPV1(String msg) throws HL7Exception {
        boolean response = false;

        //TODO: if the message has a missing PV1 segment, return false, else return true

        if (msg.contains("PV1")) {
            response = true;
        } else {
            System.out.println("Message Error: 'Invalid Structure | Missing Segment ' - Missing [PV-1] Segment");
        }

        return response;
    }

    private boolean hasPV1AfterPID(String msg) throws HL7Exception {
        boolean response = false;

        //TODO: if the message has the PV1 segment before the PID segment, return false, else return true

        String pv1 = "PV1";
        String pid = "PID";

        if (msg.contains("PV1") && msg.contains("PID")) {
            if(msg.indexOf(pid) < msg.indexOf(pv1)) {
                response = true;
            } else {
                System.out.println("Message Error: 'Invalid Structure | Incorrect Segment Order' - Segment [PV-1] Found Before Segment [PID-3]");
            }
        }

        return response;
    }
}

