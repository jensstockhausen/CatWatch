package de.famst.activity;

import de.famst.logparser.ActivityLogParser;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.SECONDS;

public class ActivityParser extends TailerListenerAdapter
{
    private static Logger LOG = LoggerFactory.getLogger(ActivityParser.class);

    private FeedHandler feedHandler;
    private GraphHandler graphHandler;
    private Boolean stopAtEnd;

    private Map<Integer, Activity> currentState;
    private Tailer tailer;

    public ActivityParser(FeedHandler feedHandler, Boolean stopAtEnd)
    {
        this.feedHandler = feedHandler;
        this.stopAtEnd = stopAtEnd;
        this.currentState = new HashMap<>();
        this.graphHandler = null;
    }

    public ActivityParser(FeedHandler feedHandler, GraphHandler graphHandler, Boolean stopAtEnd)
    {
        this.feedHandler = feedHandler;
        this.graphHandler = graphHandler;
        this.stopAtEnd = stopAtEnd;
        this.currentState = new HashMap<>();
    }

    @Override
    public void init(Tailer tailer)
    {
        this.tailer = tailer;
    }

    @Override
    public void endOfFileReached()
    {
        if (stopAtEnd)
        {
            feedHandler.finalize();
            tailer.stop();

            if (graphHandler != null)
            {
                graphHandler.finish();
            }
        }
    }

    @Override
    public void handle(String line)
    {
        StringBuilder msg = new StringBuilder();

        msg.append("handling line: ");
        msg.append(line);

        Activity activity = new Activity(line);

        if (!activity.isValid())
        {
            msg.append(" INVALID");
            LOG.info(msg.toString());

            return;
        }

        if (activity.getOperation().equals(Activity.OPERATION.STARTING))
        {
            msg.append(" [was STARTING] ");
            LOG.info(msg.toString());
            // reset states
            this.currentState = new HashMap<>();

            return;
        }


        if (graphHandler != null)
        {
            graphHandler.drawActivity(activity);
        }


        if (activity.getOperation() == Activity.OPERATION.OPENING)
        {
            if (currentState.containsKey(activity.getChannel()))
            {
                Activity currentActivity = currentState.get(activity.getChannel());

                if (currentActivity.getOperation() == Activity.OPERATION.OPENING)
                {
                    msg.append(" [was OPENING] ");

                    if (activity.getInstant().minus(30, SECONDS)
                        .isBefore(currentActivity.getInstant()))
                    {
                        msg.append("keep prior => O");
                    }
                    else
                    {
                        currentState.put(activity.getChannel(), activity);
                        msg.append("overwrite prior => O");
                    }
                }
            }
            else
            {
                currentState.put(activity.getChannel(), activity);

                msg.append("=> O");
            }
        }

        if (activity.getOperation() == Activity.OPERATION.CLOSING)
        {
            msg.append("=> C");

            if (currentState.containsKey(activity.getChannel()))
            {
                Integer channel = activity.getChannel();

                Feed feed = new Feed(currentState.get(channel), activity);

                msg.append(" duration ");
                msg.append(feed.duration());

                feedHandler.handleFeed(feed);

                if (graphHandler != null)
                {
                    graphHandler.drawFeed(feed);
                }

                currentState.remove(channel);
            }
        }

        LOG.info(msg.toString());
    }


}
