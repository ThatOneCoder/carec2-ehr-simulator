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

    @Autowired
    HL7MessageService(HL7MessageAssembler HL7MessageAssembler){
        this.HL7MessageAssembler = HL7MessageAssembler;
    }
/*
    public HL7Message createMessage() throws Exception{
        return messageAssembler.toRawMessage(inMessage);
    }
*/
    public HL7Message createHL7Message(String inMessage){
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
