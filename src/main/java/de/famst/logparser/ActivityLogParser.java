package de.famst.logparser;

import de.famst.activity.ActivityParser;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ActivityLogParser
{
    private static Logger LOG = LoggerFactory.getLogger(ActivityLogParser.class);

    public static void main(String[] args)
    {
        LOG.info("parsing {}", args[0]);

        TailerListener listener = new ActivityParser(new LogFeedHandler(), new SvgGraphHandler(), true);

        Tailer tailer = new Tailer(new File(args[0]), listener, 500);

        Thread thread = new Thread(tailer);
        thread.start();
    }

}
