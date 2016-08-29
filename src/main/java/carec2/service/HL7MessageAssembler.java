package carec2.service;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.parser.GenericModelClassFactory;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import carec2.domain.Encounter;
import carec2.domain.Episode;
import carec2.domain.HL7Message;
import carec2.domain.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class HL7MessageAssembler {

    final private Logger log = LoggerFactory.getLogger(this.getClass());
    final private String status = "RAW";


    public HL7Message toRawMessage(String msg) throws Exception {
        HL7Message hl7Message = null;
        try {
            Date date = new Date();

            Terser terser = setUp(msg);

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

    public Patient parsePatient(String message){
        Patient patient = null;
        try{
            Terser terser = setUp(message);

            String corporate_mrn = String.valueOf(terser.get("/PID-3-1"));
            if(StringUtils.isEmpty(corporate_mrn)){
                log.error("ERROR: Patient Corporate MRN can not be null");
                return null;
            }
            String facility_code = String.valueOf(terser.get("/MSH-4-2"));
            String last_name = String.valueOf(terser.get("/PID-5-1"));
            String first_name = String.valueOf(terser.get("/PID-5-2"));
            String middle_name = String.valueOf(terser.get("/PID-5-3"));
            String gender = String.valueOf(terser.get("/PID-8"));
            String birthDate = String.valueOf(terser.get("/PID-7"));
            Timestamp dob = convertToTimestamp(birthDate);
            String patient_ssn = String.valueOf(terser.get("/PID-3-5"));
            String deathDate = String.valueOf(terser.get("/PID-29"));
            Timestamp death_date = convertToTimestamp(deathDate);
            String death_indicator = String.valueOf(terser.get("/PID-30"));

            patient = new Patient(facility_code, corporate_mrn, first_name, middle_name, last_name, patient_ssn,
                                    gender, dob, getAge(birthDate), death_indicator, death_date);

        } catch (HL7Exception e) {
            log.error("Invalid Patient information " + e.getMessage());
        }

        return patient;
    }

    public Encounter parseEncounter(String message){
        Encounter encounter = null;
        try{
            Terser terser = setUp(message);

            String patientClass = String.valueOf(terser.get("/PV1-2-1"));
            String admitType =  String.valueOf(terser.get("/PV1-4"));
            String preadmitNbr = String.valueOf(terser.get("/PV1-5-1"));
            String hospitalService = String.valueOf(terser.get("/PV1-10"));
            String admitSource = String.valueOf(terser.get("/PV1-14-2"));
            String patientType = String.valueOf(terser.get("/PV1-18"));
            String visitNbr = String.valueOf(terser.get("/PV1-19-1"));
            if(StringUtils.isEmpty(visitNbr)){
                log.error("ERROR: Encounter Visit Nbr can not be null");
                return null;
            }
            String dischargeDisposition = String.valueOf(terser.get("/PV1-36-2"));
            Timestamp facilityAdmitDate = convertToTimestamp(String.valueOf(terser.get("/PV1-44")));
            Timestamp dischargeDate = convertToTimestamp(String.valueOf(terser.get("/PV1-45")));
            String admitReason = String.valueOf(terser.get("/PV1-3-5"));
            String episodeNbr = String.valueOf(terser.get("/PID-18-1"));

            encounter = new Encounter(patientClass, admitType, preadmitNbr, hospitalService, admitSource, patientType,
                    visitNbr, dischargeDisposition, facilityAdmitDate, dischargeDate, admitReason, episodeNbr);
        } catch (HL7Exception e) {
            log.error("Invalid Encounter information " + e.getMessage());
        }
        return encounter;
    }

    public Episode parseEpisode(String message) {
        Episode episode = null;
        try {
            Terser terser = setUp(message);

            String episodeNbr = String.valueOf(terser.get("/PID-18-1"));
            if(StringUtils.isEmpty(episodeNbr)){
                log.error("ERROR: Episode Nbr can not be null");
                return null;
            }
           // Hardcode as "CJR"
            String episodeType = "CJR";
            Timestamp episodeDate = convertToTimestamp(String.valueOf(terser.get("/PV1-44")));
            String corporateMrn = String.valueOf(terser.get("/PID-3-1"));

            episode = new Episode(episodeNbr, episodeType, episodeDate, corporateMrn);
        } catch (HL7Exception e) {
            log.error("Invalid Episode information " + e.getMessage());
        }
        return episode;
    }


    private Terser setUp(String message) throws HL7Exception {
        // create hapi context
        HapiContext context = new DefaultHapiContext();

        // set up
        context.setModelClassFactory(new GenericModelClassFactory());
        context.setValidationContext(ValidationContextFactory.noValidation());
        GenericMessage genMessage = (GenericMessage) context.getPipeParser().parse(message);
        return new Terser(genMessage);
    }

    private Timestamp convertToTimestamp(String dob){
        Timestamp timestamp = null;
        if(!StringUtils.isEmpty(dob)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                Date parsedDate = simpleDateFormat.parse(dob);
                timestamp = new Timestamp(parsedDate.getTime());
            } catch (ParseException ex) {
                log.info("Exception in parsing the date: " + ex.getMessage());
            }
        }
        return timestamp;
    }

    private int getAge(String dob){
        int age = 0;
            if(!StringUtils.isEmpty(dob)) {
                int year = Integer.parseInt(dob.substring(0, 4));
                int month = Integer.parseInt(dob.substring(4, 6));
                int day = Integer.parseInt(dob.substring(6));

                long currentTime = System.currentTimeMillis();
                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(currentTime);

                int nowMonth = now.get(Calendar.MONTH) + 1;
                int nowYear = now.get(Calendar.YEAR);
                age = nowYear - year;

                if (month > nowMonth) {
                    age--;
                } else if (month == nowMonth) {
                    int nowDay = now.get(Calendar.DATE);

                    if (day > nowDay) {
                        age--;
                    }
                }
                return age;
            }
        return age;
    }


}
