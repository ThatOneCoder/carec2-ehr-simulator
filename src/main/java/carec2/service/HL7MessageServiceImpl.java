package carec2.service;

import carec2.domain.Encounter;
import carec2.domain.Episode;
import carec2.domain.HL7Message;
import carec2.domain.Patient;
import carec2.repository.EncounterRepository;
import carec2.repository.EpisodeRepository;
import carec2.repository.HL7MessageRepository;
import carec2.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HL7MessageServiceImpl implements HL7MessageService{

    private static final Logger log = LoggerFactory.getLogger(HL7MessageServiceImpl.class);

    private final HL7MessageRepository hl7MessageRepository;

    private final HL7MessageAssembler hl7MessageAssembler;

    private final PatientRepository patientRepository;

    private final EncounterRepository encounterRepository;

    private final EpisodeRepository episodeRepository;

    @Autowired
    public HL7MessageServiceImpl(HL7MessageRepository hl7MessageRepository,
                                 HL7MessageAssembler hl7MessageAssembler,
                                 PatientRepository patientRepository,
                                 EncounterRepository encounterRepository,
                                 EpisodeRepository episodeRepository) {
        this.hl7MessageRepository = hl7MessageRepository;
        this.hl7MessageAssembler = hl7MessageAssembler;
        this.patientRepository = patientRepository;
        this.encounterRepository = encounterRepository;
        this.episodeRepository = episodeRepository;
    }

    @Override
    public void saveMessage(String msg){
        //store hl7 messages in mongo
        try{
            HL7Message hl7Message = hl7MessageAssembler.toRawMessage(msg);
            if (hl7Message != null) {
                hl7MessageRepository.save(hl7Message);
            }
        }catch (Exception e){
            log.error("Error in save raw message in mongo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String parseAndSavePatient(String message){
        try{
            Patient patient = hl7MessageAssembler.parsePatient(message);
            if (patient != null){
                savePatient(patient);
                return patient.getcorporateMrn();
            }
        }catch (Exception e){
            log.error("Error in parse and save patient in postgres: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String parseAndSaveEncounter(String message){
        try{
            Encounter encounter = hl7MessageAssembler.parseEncounter(message);
            if (encounter != null){
                saveEncounter(encounter);
                return encounter.getVisitNbr();
            }
        }catch (Exception e){
            log.error("Error in parse and save encounter in postgres: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String parseAndSaveEpisode(String message){
        try{
            Episode episode = hl7MessageAssembler.parseEpisode(message);
            if (episode != null){
                saveEpisode(episode);
                return episode.getEpisodeNbr();
            }
        }catch (Exception e){
            log.error("Error in parse and save episode in postgres: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    @Transactional
    @Override
    public void savePatient(Patient patientPersist){
        Long id = patientRepository.findByCorporateMrn(patientPersist.getcorporateMrn());
        if (id != null){
            //          patientRepository.update(patientPersist.getFirstName(), id);
            log.info("Patient exists, it will update...");
        }else {
            patientRepository.save(patientPersist);
        }
    }

    @Transactional
    @Override
    public void saveEncounter(Encounter encounterPersist){
        List<Long> ids = encounterRepository.findByVisitNbr(encounterPersist.getVisitNbr());
        if (ids != null && ids.size() > 0){
            log.info("Encounter exists, it will update...");
        }else {
            encounterRepository.save(encounterPersist);
        }
    }

    @Transactional
    @Override
    public void saveEpisode(Episode episodePersist){
        List<Long> ids  = episodeRepository.findByEpisodeNbr(episodePersist.getEpisodeNbr(), episodePersist.getcorporateMrn());
        if (ids != null && ids.size() > 0){
            log.info("Episode exists, it will update...");
        } else {
            episodeRepository.save(episodePersist);
        }

    }
}
