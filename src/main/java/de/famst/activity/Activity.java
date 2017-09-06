package de.famst.activity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Activity
{

    public static enum OPERATION
    {
        OPENING,
        CLOSING,
        STARTING,
        OTHER;
    }

    private static SimpleDateFormat simpleDateFormat
        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private Date timestamp = null;
    private OPERATION operation = null;
    private Integer channel = null;

    public Activity(String line)
    {
        String[] parts = line.split(" ");

        if (parts.length == 4)
        {
            try
            {
                String timeString = parts[0] + " " + parts[1];
                timestamp = simpleDateFormat.parse(timeString);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }

            if (parts[2].equals("opening"))
            {
                operation = OPERATION.OPENING;
            } else if (parts[2].equals("closing"))
            {
                operation = OPERATION.CLOSING;
            } else if (parts[2].equals("started"))
            {
                operation = OPERATION.STARTING;
            }
            else
            {
                operation = OPERATION.OTHER;
            }

            try
            {
                channel = Integer.parseInt(parts[3]);
            } catch (NumberFormatException e)
            {
                channel = null;
            }
        }
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public Instant getInstant()
    {
        return timestamp.toInstant();
    }

    public LocalDate getLocalDate()
    {
        return LocalDate.from(ZonedDateTime.ofInstant(getInstant(), ZoneId.systemDefault()));
    }

    public LocalTime getLocalTime()
    {
        return LocalTime.from(ZonedDateTime.ofInstant(getInstant(), ZoneId.systemDefault()));
    }

    public OPERATION getOperation()
    {
        return operation;
    }

    public Integer getChannel()
    {
        return channel;
    }

    public Boolean isValid()
    {
        return (getTimestamp() != null) && (getOperation() != null) && (getChannel() != null);
    }

    @Override
    public String toString()
    {
        return "Activity{" +
            "timestamp=" + timestamp +
            ", operation=" + operation +
            ", channel=" + channel +
            '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (timestamp != null ? !timestamp.equals(activity.timestamp) : activity.timestamp != null) return false;
        if (operation != activity.operation) return false;
        return channel != null ? channel.equals(activity.channel) : activity.channel == null;
    }

    @Override
    public int hashCode()
    {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        return result;
    }


}
