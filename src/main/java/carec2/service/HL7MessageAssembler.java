package carec2.service;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.parser.GenericModelClassFactory;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import carec2.domain.HL7Message;
import carec2.domain.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class HL7MessageAssembler {

    final private Logger log = LoggerFactory.getLogger(this.getClass());
    final private String status = "RAW";


    public HL7Message toRawMessage(String msg) throws Exception {
        HL7Message hl7Message = null;
        try {
            Date date = new Date();
            // create hapi context
            HapiContext context = new DefaultHapiContext();

            // set up
            context.setModelClassFactory(new GenericModelClassFactory());
            context.setValidationContext(ValidationContextFactory.noValidation());
            GenericMessage genMessage = (GenericMessage) context.getPipeParser().parse(msg);
            Terser terser = new Terser(genMessage);

            // parse message for content
            // "/MSH-9-3" unique identifier for the name of the file
            String assign_auth = String.valueOf(terser.get("/MSH-4-2"));
            String eoc_acc = String.valueOf(terser.get("/PID-3-1"));
            String episode = assign_auth + eoc_acc;

            String type = String.valueOf(terser.get("/MSH-9-1"));

            hl7Message = new HL7Message(episode, msg, status, date.toString(), type);

        } catch (HL7Exception e) {
            log.error("Invalid Message: " + e.getMessage());
        }
        return hl7Message;

    }


        public Patient parsePatient(String patient_mrn, Terser terser){
            Patient patient = null;
            try{
                String facility_code = String.valueOf(terser.get("/MSH-4-2"));
                String last_name = String.valueOf(terser.get("/PID-5-1"));
                String first_name = String.valueOf(terser.get("/PID-5-2"));
                String middle_name = String.valueOf(terser.get("/PID-5-3"));
                String gender = String.valueOf(terser.get("/PID-8"));
                Date dob = convertToDate(String.valueOf(terser.get("/PID-7")));
                String patient_ssn = String.valueOf(terser.get("/PID-3-5"));
                String deathDate = String.valueOf(terser.get("/PID-29"));

                Date death_date = null;
                if (!StringUtils.isEmpty(deathDate)){
                    death_date = convertToDate(deathDate);
                }
                String death_indicator = String.valueOf(terser.get("/PID-30"));

                patient = new Patient(facility_code, patient_mrn,first_name, middle_name, last_name, patient_ssn,
                        gender, dob, 0, death_indicator, death_date);

            } catch (HL7Exception e) {
                log.error("Invalid Patient information " + e.getMessage());
            }

            return patient;
        }

        private Date convertToDate(String dob){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmdd");
            Date date = null;
            try
            {
                date = simpleDateFormat.parse(dob);
                log.info("date : "+simpleDateFormat.format(date));
            } catch (ParseException ex){
                log.info("Exception in parsing the date: "+ ex.getMessage());
            }

            return date;
        }

}
