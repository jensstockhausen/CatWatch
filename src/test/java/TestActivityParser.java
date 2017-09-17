import de.famst.activity.Activity;
import de.famst.activity.ActivityParser;
import de.famst.activity.Feed;
import de.famst.activity.FeedHandler;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class TestActivityParser
{
    public void process(String[] lines, int expectedLineOpen, int expectedLineClose)
    {
        FeedHandler feedHandler = mock(FeedHandler.class);
        ActivityParser ap = new ActivityParser(feedHandler, true);

        Arrays.stream(lines).forEach(line ->
            ap.handle(line)
        );

        Feed expectedFeed = new Feed(
            new Activity(lines[expectedLineOpen]),
            new Activity(lines[expectedLineClose])
        );

        Mockito.verify(feedHandler, Mockito.times(1)).handleFeed(
            Matchers.any(Feed.class));

        Mockito.verify(feedHandler, Mockito.times(1)).handleFeed(
            Matchers.refEq(expectedFeed));
    }


    @Test
    public void openCloseIsHandled() throws Exception
    {
        String[] lines = {
            "2017-08-05 20:24:33.336 opening 4",
            "2017-08-05 20:24:34.336 closing 4"
        };

        process(lines, 0, 1);
    }

    @Test
    public void multipleOpensAreIgnored() throws Exception
    {
        String[] lines = {
            "2017-08-05 18:24:33.336 opening 4",
            "2017-08-05 20:24:33.336 opening 4",
            "2017-08-05 20:24:34.336 closing 4"
        };

        process(lines, 1, 2);
    }

    @Test
    public void closeOpenCloseIsHandled() throws Exception
    {
        String[] lines = {
            "2017-08-05 18:24:33.336 closing 4",
            "2017-08-05 20:24:33.336 opening 4",
            "2017-08-05 20:24:34.336 closing 4"
        };

        process(lines, 1, 2);
    }

    @Test
    public void startedIsIgnored() throws Exception
    {
        String[] lines = {
            "2017-08-05 18:24:33.336 closing 4",
            "2017-08-05 20:24:33.336 started 0",
            "2017-08-05 20:24:34.336 opening 4",
            "2017-08-05 20:26:34.336 closing 4"
        };

        process(lines, 2, 3);
    }

    @Test
    public void multipleClosesAreIgnored() throws Exception
    {
        String[] lines = {
            "2017-08-05 20:24:33.336 opening 4",
            "2017-08-05 20:24:34.336 closing 4",
            "2017-08-05 20:24:36.336 closing 4"
        };

        process(lines, 0, 1);
    }


    @Test
    public void multipleOpeningsAreAccumulatedIfLessThan30SecondsApart() throws Exception
    {
        String[] lines = {
            "2017-08-05 20:24:33.336 opening 4",
            "2017-08-05 20:24:34.336 opening 4",
            "2017-08-05 20:24:36.336 closing 4"
        };

        process(lines, 0, 2);
    }

    @Test
    public void multipleOpeningsAreNotAccumulatedIfMoreThan30SecondsApart() throws Exception
    {
        String[] lines = {
            "2017-08-05 20:24:33.336 opening 4",
            "2017-08-05 20:25:34.336 opening 4",
            "2017-08-05 20:26:36.336 closing 4"
        };

        process(lines, 1, 2);
    }

    @Test
    public void closeAfterOpenIsIgnored() throws Exception
    {
        String[] lines = {
            "2017-09-04 04:45:30.306 opening 5",
            "2017-08-05 19:38:18.772 started 0",
            "2017-08-05 19:38:18.799 closing 2",
            "2017-08-05 20:04:05.591 closing 5"
        };

        FeedHandler feedHandler = mock(FeedHandler.class);
        ActivityParser ap = new ActivityParser(feedHandler, true);

        Arrays.stream(lines).forEach(line ->
            ap.handle(line)
        );

        Mockito.verify(feedHandler, never()).handleFeed(
            Matchers.any(Feed.class));
    }

    @Test
    public void oldCLoseAreIgnored() throws Exception
    {
        String[] lines = {
            "2017-08-04 04:45:30.306 opening 5",
            "2017-08-04 03:04:05.591 closing 5",
            "2017-08-04 19:38:18.799 closing 2",
            "2017-08-04 20:04:05.591 closing 5"
        };

        FeedHandler feedHandler = mock(FeedHandler.class);
        ActivityParser ap = new ActivityParser(feedHandler, true);

        Arrays.stream(lines).forEach(line ->
            ap.handle(line)
        );

        process(lines, 0, 3);
    }
}
