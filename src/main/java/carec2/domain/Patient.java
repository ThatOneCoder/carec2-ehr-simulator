package carec2.domain;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "facility_code")
    private String facilityCode;

    @Size(max = 20)
    @Column(name = "patient_mrn")
    private String patientMrn;

    @Size(max = 30)
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 30)
    @Column(name = "middle_name")
    private String middleName;

    @Size(max = 50)
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 9)
    @Column(name = "patient_ssn")
    private String patientSsn;

    private String gender;
    private Date dob;
    private Integer age;

    @Column(name = "death_indicator")
    private String deathIndicator;

    @Column(name = "death_date")
    private Date deathDate;

    protected Patient() {
    }

    public Patient(String facilityCode, String patientMrn, String firstName, String middleName, String lastName,
                   String patientSsn, String gender, Date dob, Integer age, String deathIndicator, Date deathDate) {
        this.facilityCode = facilityCode;
        this.patientMrn = patientMrn;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.patientSsn = patientSsn;
        this.gender = gender;
        this.dob = dob;
        this.age = age;
        this.deathIndicator = deathIndicator;
        this.deathDate = deathDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getPatientMrn() {
        return patientMrn;
    }

    public void setPatientMrn(String patientMrn) {
        this.patientMrn = patientMrn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatientSsn() {
        return patientSsn;
    }

    public void setPatientSsn(String patientSsn) {
        this.patientSsn = patientSsn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDeathIndicator() {
        return deathIndicator;
    }

    public void setDeathIndicator(String deathIndicator) {
        this.deathIndicator = deathIndicator;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Timestamp deathDate) {
        this.deathDate = deathDate;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", facilityCode='" + facilityCode + '\'' +
                ", patientMrn='" + patientMrn + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patientSsn='" + patientSsn + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", age='" + age + '\'' +
                ", deathIndicator='" + deathIndicator + '\'' +
                ", deathDate='" + deathDate + '\'' +
                '}';

    }
}