package carec2.service;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.parser.GenericModelClassFactory;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import carec2.domain.HL7Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HL7MessageAssembler {

    final private Logger logger = LoggerFactory.getLogger(this.getClass());
    final private String status = "RAW";
    /*
    public HL7Message processRawMessage(HL7Message in) throws Exception {
        HL7Message rawMessage = null;
        if (in != null) {
            // convert incoming message to a string
            String msg = in.toString();
            // immediately store message
            rawMessage = toRawMessage(msg);
        }
        return  rawMessage;
    }
*/
    public HL7Message toRawMessage(String msg) throws Exception{

            java.util.Date date = new java.util.Date();
            // create hapi context
            HapiContext context = new DefaultHapiContext();

            // set up
            context.setModelClassFactory(new GenericModelClassFactory());
            context.setValidationContext(ValidationContextFactory.noValidation());
            GenericMessage genMessage = (GenericMessage) context.getPipeParser().parse(msg);
            Terser terser = new Terser(genMessage);

            // parse message for content
            String assign_auth = String.valueOf(terser.get("/MSH-4-2"));
            String eoc_acc = String.valueOf(terser.get("/PID-3-1"));
            String episode = assign_auth + eoc_acc;
    //        System.out.println("episode: " + episode);
    //        System.out.print(msg);

            String dt = "dateof( now() )";
            String type = String.valueOf(terser.get("/MSH-9-1"));

            return new HL7Message(episode, msg, status, dt, type );
    }
}
