package carec2.repository;


import carec2.domain.Episode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
@Repository
public interface EpisodeRepository extends CrudRepository<Episode, Long> {
    @Query(value = "SELECT ep.id FROM Episode ep where ep.episode_nbr=?1", nativeQuery = true)
    Long findByEpisodeNbr(String episodeNbr);
    void flush();
}
