package de.famst.persistance;

import de.famst.activity.ActivityParser;
import de.famst.logparser.SvgGraphHandler;
import org.apache.commons.io.input.Tailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
public class LogParseInitializer
{
    private static Logger LOG = LoggerFactory.getLogger(LogParseInitializer.class);

    @Autowired
    private StoreLogFeedHandler storeLogFeedHandler;

    private ActivityParser activityParser;

    public void startTailing(Path logFilePath)
    {
        activityParser = new ActivityParser(storeLogFeedHandler, false);

        Tailer tailer = new Tailer(logFilePath.toFile(), activityParser, 500);
        Thread thread = new Thread(tailer);
        thread.setName("ActivityParser");
        thread.start();
    }

}
