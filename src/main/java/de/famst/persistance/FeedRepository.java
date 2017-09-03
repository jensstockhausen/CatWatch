package de.famst.persistance;

import de.famst.activity.Feed;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface FeedRepository extends CrudRepository<FeedEntity, Long>
{
    List<FeedEntity> findByDateOrderByTimeAsc(Date date);

    List<FeedEntity> findByDateAndChannelOrderByTimeAsc(Date date, Integer channel);

    List<FeedEntity> findBySignature(String signature);
}