package de.famst.persistance;

import de.famst.activity.Feed;
import de.famst.activity.FeedHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreLogFeedHandler implements FeedHandler
{
    private static Logger LOG = LoggerFactory.getLogger(StoreLogFeedHandler.class);

    @Autowired
    private FeedService feedService;

    @Override
    public void handleFeed(Feed feed)
    {
        LOG.info("handle {}", feed);

        FeedEntity feedEntity = FeedEntity.fromFeed(feed);
        feedService.appendFeed(feedEntity);
    }

    @Override
    public void finalize()
    {
    }

}
