package carec2.camel.processors;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.parser.GenericModelClassFactory;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import carec2.domain.HL7Message;
import carec2.repository.HL7MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessorAudit {

    @Autowired
    HL7MessageRepository hl7MessageRepository;

    public void processMessage(String msg) throws Exception {
        //store hl7 messages in mongo
        recordMessage(msg, "RAW");
    }

    public void recordMessage(String msg, String status) throws Exception {
        java.util.Date date = new java.util.Date();

//        // create mongo client
//        MongoClient mongo = new MongoClient( "localhost" , 27017 );
//
//        // get mongo db
//        DB db = mongo.getDB("microservices");
//
//        // get mongo collection (aka table)
//        DBCollection table = db.getCollection("message");
//
//        // create document(data)
//        BasicDBObject document = new BasicDBObject();

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

        String dt = date.toString();
        String type = String.valueOf(terser.get("/MSH-9-1"));

        HL7Message hl7Message = new HL7Message(episode, msg, status, dt, type);
        hl7MessageRepository.save(hl7Message);

//        String query = ("INSERT INTO message (id, episode, message, status, statusdate, type) VALUES (now(),'" + episode + "','" + msg + "','" + status + "'," + dt + ",'" + type + "')");
//
//        // add data to a mongo document
//        document.put("episode", episode);
//        document.put("message", msg);
//        document.put("status", status);
//        document.put("statusdate", date);
//        document.put("type", type);
//        table.insert(document);

    }

}
