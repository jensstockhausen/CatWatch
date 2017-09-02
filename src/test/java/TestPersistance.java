import de.famst.BootApplication;
import de.famst.activity.Activity;
import de.famst.activity.Feed;
import de.famst.persistance.FeedEntity;
import de.famst.persistance.FeedRepository;
import de.famst.persistance.FeedService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= BootApplication.class)
@DataJpaTest
@ComponentScan("de.famst")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestPersistance
{
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedService feedService;

    @Test
    public void canFindFeedActions() throws Exception
    {
        FeedEntity feedEntity = new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("20:33:45"), 7.5, 4);

        feedRepository.save(feedEntity);

        List<FeedEntity> byDate = feedRepository.findByDate(Date.valueOf("2017-08-05"));

        assertThat(byDate.size(), is(1));
        assertThat(byDate.get(0).getDate(), is(Date.valueOf("2017-08-05")));
    }


    @Test
    public void cannotAddSameFeedTwice() throws Exception
    {
        FeedEntity feedEntity1 = new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("20:33:45"), 7.5, 4);

        FeedEntity feedEntity2 = new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("20:33:45"), 7.5, 4);

        FeedEntity stored1 = feedService.appendFeed(feedEntity1);
        FeedEntity stored2 = feedService.appendFeed(feedEntity2);

        List<FeedEntity> feedByDate = feedRepository.findByDate(Date.valueOf("2017-08-05"));

        assertThat(stored1.getSignature(), is(stored2.getSignature()));
        assertThat(feedByDate.size(), is(1));
    }


}
