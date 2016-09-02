package carec2.camel.processors;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.GenericModelClassFactory;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class FilterProcessor {

    private String message;

    // getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
//        String message = in.toString();
        this.message = message;
    }

    // hl7 filtration
    public boolean process(String msg) throws Exception {

        if (isMaleOrFemale(msg)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isERMessage(String msg) throws HL7Exception {
        boolean response = false;

        //TODO: if the message is not an ER message, return false, else return true

        // create hapi context
        HapiContext context = new DefaultHapiContext();

        // set up
        context.setModelClassFactory(new GenericModelClassFactory());
        context.setValidationContext(ValidationContextFactory.noValidation());
        GenericMessage genMessage = (GenericMessage) context.getPipeParser().parse(msg);
        Terser terser = new Terser(genMessage);

        // parse message for content
        String hl7Type = String.valueOf(terser.get("/MSH-9-1"));

        if (hl7Type == "ADT") {
            String type = String.valueOf(terser.get("/PV1-2-1"));
            if (type == "E") {
                response = true;
            } else {
                response = false;
            }
        } else {
            // return true for ORU (HACK!!!)
            response = true;
        }

        return response;
    }

    private boolean isMaleOrFemale(String msg) throws HL7Exception {
        boolean response = false;

        //TODO: if the message is not an ER message, return false, else return true

        // create hapi context
        HapiContext context = new DefaultHapiContext();

        // set up
        context.setModelClassFactory(new GenericModelClassFactory());
        context.setValidationContext(ValidationContextFactory.noValidation());
        GenericMessage genMessage = (GenericMessage) context.getPipeParser().parse(msg);
        Terser terser = new Terser(genMessage);

        // parse message for content
        String sex = String.valueOf(terser.get("/PID-8"));

        if (! sex.equals("A")) {
            response = true;
        }

        return response;
    }


    public boolean isA03Message(String msg) throws HL7Exception {
        boolean response = false;

        //TODO: if the message is not an ER message, return false, else return true

        // create hapi context
        HapiContext context = new DefaultHapiContext();

        if (msg.contains("ADT")) {
//            System.out.println(msg.indexOf("A03"));

        }

        // set up
        context.setModelClassFactory(new GenericModelClassFactory());
        context.setValidationContext(ValidationContextFactory.noValidation());
        GenericMessage genMessage = (GenericMessage) context.getPipeParser().parse(msg);
        Terser terser = new Terser(genMessage);

        // parse message for content
        String hl7Type = String.valueOf(terser.get("/MSH-9-1"));
//        System.out.println("HL7 TYPE: " + hl7Type);

        if (msg.contains("ADT")) {
            if (msg.contains("A03")) {

                response = true;
            }
        } else {
            // return true for ORU (HACK!!!)
            response = true;
        }

        return response;
    }


    // csv filtration
    public boolean filterCsv(String msg) {
        boolean response = true;

        String[] fields = msg.split(",");

        // returns false if any required field is missing
        response = isSurgicalCaseRecord(fields);
        if (! response) {
            return response;
        }

        return response;
    }

    public boolean isSurgicalCaseRecord(String[] fields) {
        boolean response = true;

        // CCL_QUERY_TYPE column (column 1)
        String field = fields[0];

        if ( ! field.equals("Surgical_case_record")) {
            response = false;
            System.out.println("Non-Standard CSV Record:\t" + " [Incorrect Record Type | Non-Surgical-Case-Record] - Column #" + field);
        }

        return response;
    }
}
