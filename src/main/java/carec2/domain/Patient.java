package carec2.domain;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;


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
    @Column(name = "corporate_mrn")
    private String corporateMrn;

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
    private Timestamp dob;
    private Integer age;

    @Column(name = "death_indicator")
    private String deathIndicator;

    @Column(name = "death_date")
    private Timestamp deathDate;

    protected Patient() {
    }

    public Patient(String facilityCode, String corporateMrn, String firstName, String middleName, String lastName,
                   String patientSsn, String gender, Timestamp dob, Integer age, String deathIndicator, Timestamp deathDate) {
        this.facilityCode = facilityCode;
        this.corporateMrn = corporateMrn;
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

    public String getcorporateMrn() {
        return corporateMrn;
    }

    public void setcorporateMrn(String corporateMrn) {
        this.corporateMrn = corporateMrn;
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

    public Timestamp getDob() {
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

    public Timestamp getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Timestamp deathDate) {
        this.deathDate = deathDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (id == null || obj == null || getClass() != obj.getClass())
            return false;
        Patient that = (Patient) obj;
        return id.equals(that.id)
                && (firstName == that.firstName || (firstName != null && firstName.equals(that.getFirstName())))
                && (lastName == that.lastName || (lastName != null && lastName .equals(that.getLastName())))
                && (corporateMrn == that.corporateMrn || (corporateMrn != null && corporateMrn.equals(that.getLastName())));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + (int)(long)id;
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((corporateMrn == null) ? 0 : corporateMrn.hashCode());
        return result;

    }
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", facilityCode=" + facilityCode +
                ", corporateMrn=" + corporateMrn +
                ", firstName=" + firstName +
                ", middleName=" + middleName +
                ", lastName=" + lastName +
                ", patientSsn=" + patientSsn +
                ", gender=" + gender +
                ", dob=" + dob +
                ", age=" + age +
                ", deathIndicator=" + deathIndicator +
                ", deathDate=" + deathDate +
                "}";

    }
}