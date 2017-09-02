package de.famst.logparser;

import de.famst.activity.Activity;
import de.famst.activity.Feed;
import de.famst.activity.FeedHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LogFeedHandler implements FeedHandler
{
    private static Logger LOG = LoggerFactory.getLogger(ActivityLogParser.class);

    private FileWriter fileWriter = null;
    private CSVPrinter csvFilePrinter = null;
    private CSVFormat csvFileFormat = null;

    private static final Object[] FILE_HEADER = {"timestamp", "duration", "channel"};

    public LogFeedHandler()
    {
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");

        try
        {
            fileWriter = new FileWriter("./out.csv");
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(FILE_HEADER);
        } catch (Exception e)
        {
            LOG.error("exception:", e);
        }
    }

    @Override
    public void handleFeed(Feed feed)
    {
        List feedRow = new ArrayList();

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        feedRow.add(dt.format(feed.opened()));
        feedRow.add(feed.duration());
        feedRow.add(feed.channel());

        try
        {
            csvFilePrinter.printRecord(feedRow);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void finalize()
    {
        try
        {
            fileWriter.flush();
            fileWriter.close();
            csvFilePrinter.close();
        }
        catch (IOException e)
        {
            System.out.println("Error while closing fileReader/csvFileParser !!!");
            e.printStackTrace();
        }

    }
}
