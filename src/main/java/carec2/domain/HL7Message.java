package carec2.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class HL7Message {
    @Id
    private String id;
    private String episode;
    private String message;
    private String status;
    private String statusDate;
    private String type;

    public HL7Message(String episode, String message, String status, String statusDate, String type) {
        this.episode = episode;
        this.message = message;
        this.status = status;
        this.statusDate = statusDate;
        this.type = type;
    }

    public HL7Message(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return "HL7Message{" +
                "id='" + id + '\'' +
                ", episode='" + episode + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", statusDate='" + statusDate + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
