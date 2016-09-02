package carec2.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class Episode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "episode_nbr")
    private String episodeNbr;

    @Column(name = "episode_type")
    private String episodeType;

    @Column(name = "episode_date")
    private Timestamp episodeDate;

    @Column(name = "corporate_mrn")
    private String corporateMrn;

    protected Episode() {
    }

    public Episode(String episodeNbr, String episodeType, Timestamp episodeDate, String corporateMrn) {
        this.episodeNbr = episodeNbr;
        this.episodeType = episodeType;
        this.episodeDate = episodeDate;
        this.corporateMrn = corporateMrn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEpisodeNbr() {
        return episodeNbr;
    }

    public void setEpisodeNbr(String episodeNbr) {
        this.episodeNbr = episodeNbr;
    }

    public String getEpisodeType() {
        return episodeType;
    }

    public void setEpisodeType(String episodeType) {
        this.episodeType = episodeType;
    }

    public Timestamp getEpisodeDate() {
        return episodeDate;
    }

    public void setEpisodeDate(Timestamp episodeDate) {
        this.episodeDate = episodeDate;
    }

    public String getcorporateMrn() {
        return corporateMrn;
    }

    public void setcorporateMrn(String corporateMrn) {
        this.corporateMrn = corporateMrn;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (id == null || obj == null || getClass() != obj.getClass())
            return false;
        Episode that = (Episode) obj;
        return id.equals(that.id)
                && (episodeNbr == that.episodeNbr || (episodeNbr != null && episodeNbr.equals(that.getEpisodeNbr()))
                && (corporateMrn == that.corporateMrn || (corporateMrn != null && corporateMrn.equals(that.getcorporateMrn()))));
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((episodeNbr == null) ? 0 : episodeNbr.hashCode());
        result = prime * result + (int)(long)id;
        result = prime * result + ((corporateMrn == null) ? 0 : corporateMrn.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", episodeNbr=" + episodeNbr +
                ", episodeType=" + episodeType +
                ", episodeDate=" + episodeDate +
                ", corporateMrn=" + corporateMrn +
                "}";
    }
}
