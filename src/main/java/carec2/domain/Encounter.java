package carec2.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class Encounter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "patient_class")
    private String patientClass;

    @Column(name = "admit_type")
    private String admitType;

    @Column(name = "preadmit_nbr")
    private String preadmitNbr;

    @Column(name = "hospital_service")
    private String hospitalService;

    @Column(name = "admit_source")
    private String admitSource;

    @Column(name = "patient_type")
    private String patientType;

    @Column(name = "visit_nbr")
    private String visitNbr;

    @Column(name = "discharge_disposition")
    private String dischargeDisposition;

    @Column(name = "facility_admit_date")
    private Timestamp facilityAdmitDate;

    @Column(name = "discharge_date")
    private Timestamp dischargeDate;

    @Column(name = "admitReason")
    private String admitReason;

    @Column(name = "episode_nbr")
    private String episodeNbr;


    protected Encounter() {

    }

    public Encounter(String patientClass, String admitType, String preadmitNbr, String hospitalService, String admitSource, String patientType, String visitNbr,
                     String dischargeDisposition, Timestamp facilityAdmitDate, Timestamp dischargeDate, String admitReason, String episodeNbr) {
        this.patientClass = patientClass;
        this.admitType = admitType;
        this.preadmitNbr = preadmitNbr;
        this.hospitalService = hospitalService;
        this.admitSource = admitSource;
        this.patientType = patientType;
        this.visitNbr = visitNbr;
        this.dischargeDisposition = dischargeDisposition;
        this.facilityAdmitDate = facilityAdmitDate;
        this.dischargeDate = dischargeDate;
        this.admitReason = admitReason;
        this.episodeNbr = episodeNbr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientClass() {
        return patientClass;
    }

    public void setPatientClass(String patientClass) {
        this.patientClass = patientClass;
    }

    public String getAdmitType() {
        return admitType;
    }

    public void setAdmitType(String admitType) {
        this.admitType = admitType;
    }

    public String getPreadmitNbr() {
        return preadmitNbr;
    }

    public void setPreadmitNbr(String preadmitNbr) {
        this.preadmitNbr = preadmitNbr;
    }

    public String getHospitalService() {
        return hospitalService;
    }

    public void setHospitalService(String hospitalService) {
        this.hospitalService = hospitalService;
    }

    public String getAdmitSource() {
        return admitSource;
    }

    public void setAdmitSource(String admitSource) {
        this.admitSource = admitSource;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getVisitNbr() {
        return visitNbr;
    }

    public void setVisitNbr(String visitNbr) {
        this.visitNbr = visitNbr;
    }

    public String getDischargeDisposition() {
        return dischargeDisposition;
    }

    public void setDischargeDisposition(String dischargeDisposition) {
        this.dischargeDisposition = dischargeDisposition;
    }

    public Timestamp getFacilityAdmitDate() {
        return facilityAdmitDate;
    }

    public void setFacilityAdmitDate(Timestamp facilityAdmitDate) {
        this.facilityAdmitDate = facilityAdmitDate;
    }

    public Timestamp getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Timestamp dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getAdmitReason() {
        return admitReason;
    }

    public void setAdmitReason(String admitReason) {
        this.admitReason = admitReason;
    }

    public String getEpisodeNbr() {
        return episodeNbr;
    }

    public void setEpisodeNbr(String episodeNbr) {
        this.episodeNbr = episodeNbr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (id == null || obj == null || getClass() != obj.getClass())
            return false;
        Encounter that = (Encounter) obj;
        return id.equals(that.id);
    }
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "Encounter{" +
                "id=" + id +
                ", patientClass=" + patientClass +
                ", admitType=" + admitType +
                ", preadmitNbr=" + preadmitNbr +
                ", hospitalService=" +  hospitalService +
                ", admitSource=" + admitSource +
                ", patientType=" + patientType +
                ", visitNbr=" + visitNbr +
                ", dischargeDisposition=" + dischargeDisposition +
                ", facilityAdmitDate=" + facilityAdmitDate +
                ", dischargeDate=" + dischargeDate +
                ", admitReason=" + admitReason +
                ", episodeNbr=" + episodeNbr +
                "}";

    }
}
