package de.famst.activity;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Feed
{
    private final Activity open;
    private final Activity close;
    private final Double duration;

    public Feed(Activity open, Activity close)
    {
        this.open = open;
        this.close = close;

        long diff =  close.getTimestamp().getTime() - open.getTimestamp().getTime();
        duration = Double.valueOf(TimeUnit.MILLISECONDS.toMillis(diff))/1000.0;
    }

    public Date opened()
    {
        return open.getTimestamp();
    }

    public Double duration()
    {
        return duration;
    }

    public Integer channel()
    {
        return open.getChannel();
    }

    public Activity getOpen()
    {
        return open;
    }

    public Activity getClose()
    {
        return close;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feed feed = (Feed) o;

        if (open != null ? !open.equals(feed.open) : feed.open != null) return false;
        if (close != null ? !close.equals(feed.close) : feed.close != null) return false;
        return duration != null ? duration.equals(feed.duration) : feed.duration == null;
    }

    @Override
    public int hashCode()
    {
        int result = open != null ? open.hashCode() : 0;
        result = 31 * result + (close != null ? close.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Feed{" +
            "open=" + open +
            ", close=" + close +
            ", duration=" + duration +
            '}';
    }
}
