package de.famst;

import de.famst.persistance.LogParseInitializer;
import de.famst.persistance.StoreLogFeedHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;

import java.nio.file.Paths;


@SpringBootApplication
public class BootApplication implements ApplicationListener<ApplicationReadyEvent>
{
    private static Logger LOG = LoggerFactory.getLogger(BootApplication.class);

    @Autowired
    private LogParseInitializer logParseInitializer;


    public static void main(String[] args)
    {
        SpringApplication.run(BootApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event)
    {
        String logFilePath = System.getProperty("logfilepath");

        if ((logFilePath != null) && (Paths.get(logFilePath).toFile().exists()))
        {
            LOG.info("start tailing [{}]", logFilePath);

            logParseInitializer.startTailing(Paths.get(logFilePath));
        }

    }
}
