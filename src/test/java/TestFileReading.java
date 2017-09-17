import de.famst.BootApplication;
import de.famst.persistance.FeedEntity;
import de.famst.persistance.FeedRepository;
import de.famst.persistance.LogParseInitializer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileWriter;
import java.sql.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class)
@DataJpaTest
@ComponentScan("de.famst")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestFileReading
{
    private static Logger LOG = LoggerFactory.getLogger(TestFileReading.class);

    @Autowired
    private LogParseInitializer logParseInitializer;

    @Autowired
    private FeedRepository feedRepository;

    @Rule
    public TemporaryFolder logFolder = new TemporaryFolder();
    private File logFile;

    @Before
    public void setUp() throws Exception
    {
        logFile = logFolder.newFile();

        FileWriter fw = new FileWriter(logFile);
        fw.append("2017-08-05 20:24:33.336 opening 4\n");
        fw.append("2017-08-05 20:30:33.336 closing 4\n");
        fw.close();
    }

    @Test
    public void logFileIsRead() throws Exception
    {
        logParseInitializer.startTailing(logFile.toPath());
        Thread.sleep(1000);

        List<FeedEntity> feedRepositoryByDate = feedRepository.findByDateOrderByTimeAsc(Date.valueOf("2017-08-05"));
        assertThat(feedRepositoryByDate.size(), is(1));
    }


    @Test
    public void additionalLinesAreTailed() throws Exception
    {
        logParseInitializer.startTailing(logFile.toPath());
        Thread.sleep(100);

        {
            FileWriter fw = new FileWriter(logFile, true);
            fw.append("2017-08-05 21:24:33.336 opening 4\n");
            fw.append("2017-08-05 21:30:33.336 closing 4\n");
            fw.close();
        }

        Thread.sleep(100);
        {
            FileWriter fw = new FileWriter(logFile, true);
            fw.append("2017-08-05 22:24:33.336 opening 4\n");
            fw.append("2017-08-05 22:30:33.336 closing 4\n");
            fw.close();
        }

        Thread.sleep(500);

        List<FeedEntity> feedRepositoryByDate = feedRepository.findByDateOrderByTimeAsc(Date.valueOf("2017-08-05"));

        feedRepositoryByDate.forEach( fe ->
            LOG.info("found: {}", fe.getSignature())
        );

        assertThat(feedRepositoryByDate.size(), is(3));
    }

}
