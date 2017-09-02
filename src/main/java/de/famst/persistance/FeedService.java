package de.famst.persistance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService
{
    @Autowired
    FeedRepository feedRepository;

    public FeedService()
    {
    }

    public FeedEntity appendFeed(FeedEntity feedEntity)
    {
        List<FeedEntity> feedsBySignature = feedRepository.findBySignature(feedEntity.getSignature());

        if (feedsBySignature.size() != 0)
        {
            return feedsBySignature.get(0);
        }

        return feedRepository.save(feedEntity);
    }


}
