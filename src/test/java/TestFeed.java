import de.famst.activity.Activity;
import de.famst.activity.Feed;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class TestFeed
{
    @Test
    public void feedIsCreated() throws Exception
    {
        Activity open  = new Activity("2017-08-05 20:24:33.000 opening 4");
        Activity close = new Activity("2017-08-05 20:24:40.500 opening 4");
        Feed feed = new Feed(open, close);

        assertThat(feed.duration(), is(closeTo(7.5, 1e-5)));
        assertThat(feed.opened(), is(open.getTimestamp()));
    }

}
