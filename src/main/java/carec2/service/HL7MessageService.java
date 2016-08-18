package carec2.service;

import carec2.domain.HL7Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HL7MessageService {
    private static final Logger log = LoggerFactory.getLogger(HL7MessageService.class);
    private HL7MessageAssembler HL7MessageAssembler;

    private final String inMessage = "MSH|^~\\&|hl7Integration|hl7Integration|||||ADT^A01|||2.5|\r" +
            "EVN|A01|20130617154644\r" +
            "PID|1|465 306 5961||407623|Wood^Patrick^^^MR||19700101|1|||High Street^^Oxford^^Ox1 4DP~George St^^Oxford^^Ox1 5AP|||||||";

    @Autowired
    HL7MessageService(HL7MessageAssembler HL7MessageAssembler){
        this.HL7MessageAssembler = HL7MessageAssembler;
    }
/*
    public HL7Message createMessage() throws Exception{
        return messageAssembler.toRawMessage(inMessage);
    }
*/
    public HL7Message createHL7Message(){
        HL7Message persist = null;
        try{
            persist =  HL7MessageAssembler.toRawMessage(inMessage);
        }catch (Exception e){
            log.error("" + e.getMessage());
            e.printStackTrace();
        }
        return persist;
    }

    public static void messageFailed(){
        log.error("HL7Message failed");
    }
}
