package carec2.service;


import carec2.domain.Encounter;
import carec2.domain.Episode;
import carec2.domain.Patient;

public interface HL7MessageService {
    //save raw message in mongo
    void saveMessage(String message);

    //parse and save patient info in postgres
    String parseAndSavePatient(String message);

    //parse and save encounter info in postgres
    String parseAndSaveEncounter(String message);

    //parse and save episode info in postgres
    String parseAndSaveEpisode(String message);

    void savePatient(Patient patient);
    void saveEncounter(Encounter encounter);
    void saveEpisode(Episode episode);
}
