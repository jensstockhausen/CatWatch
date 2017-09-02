package de.famst.persistance;

import de.famst.activity.Feed;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;


@Entity
@Table(uniqueConstraints= @UniqueConstraint(columnNames = {"signature"}))
public class FeedEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String signature;
    private Date date;
    private Time time;
    private Double duration;
    private Integer channel;

    public static FeedEntity fromFeed(Feed feed)
    {
        Date date = Date.valueOf(feed.getOpen().getLocalDate());
        Time time = Time.valueOf(feed.getOpen().getLocalTime());
        Double duration = feed.duration();
        Integer channel = feed.channel();

        return new FeedEntity(date, time, duration, channel);
    }

    public FeedEntity()
    {
    }

    public FeedEntity(Date date, Time time, Double duration, Integer channel)
    {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.channel = channel;

        StringBuilder sb = new StringBuilder();

        sb.append(date).append('|');
        sb.append(time).append('|');
        sb.append(channel).append('|');
        sb.append(duration);

        this.signature = sb.toString();
    }

    public Long getId()
    {
        return id;
    }

    public Date getDate()
    {
        return date;
    }

    public Time getTime()
    {
        return time;
    }

    public Double getDuration()
    {
        return duration;
    }

    public Integer getChannel()
    {
        return channel;
    }

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }
}
