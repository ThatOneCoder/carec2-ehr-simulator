package carec2.service;


import carec2.domain.Patient;

public interface HL7MessageService {

    void saveMessage(String msg);
    void savePatient(Patient patient);
}
