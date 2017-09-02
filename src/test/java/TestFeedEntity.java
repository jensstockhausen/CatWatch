import de.famst.persistance.FeedEntity;
import de.famst.activity.Activity;
import de.famst.activity.Feed;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestFeedEntity
{
    @Test
    public void canCreateFromFeed() throws Exception
    {
        Activity open  = new Activity("2017-08-05 20:24:33.000 opening 4");
        Activity close = new Activity("2017-08-05 20:24:40.500 opening 4");
        Feed feed = new Feed(open, close);

        FeedEntity feedEntity = FeedEntity.fromFeed(feed);

        assertThat(feedEntity.getDate(), is(Date.valueOf("2017-08-05")));
        assertThat(feedEntity.getTime(), is(Time.valueOf("20:24:33")));
        assertThat(feedEntity.getDuration(), is(7.5));
        assertThat(feedEntity.getChannel(), is(4));
        assertThat(feedEntity.getSignature(), is("2017-08-05|20:24:33|4|7.5"));
    }

}
