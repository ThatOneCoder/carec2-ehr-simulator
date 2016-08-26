package carec2.camel.processors;


import carec2.service.HL7MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditProcessor {

    @Autowired
    HL7MessageService hl7MessageService;

    public void processMessage(String msg) throws Exception {
        hl7MessageService.saveMessage(msg);
    }

}
