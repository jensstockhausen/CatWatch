import de.famst.BootApplication;
import de.famst.persistance.FeedEntity;
import de.famst.persistance.FeedRepository;
import de.famst.persistance.FeedService;
import de.famst.persistance.LogParseInitializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = BootApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestMVC
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeedRepository feedRepository;

    @Test
    public void canGetFeedsOfDay() throws Exception
    {
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("19:33:45"), 7.5, 4));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("20:33:45"), 7.5, 2));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("21:33:45"), 7.5, 3));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("22:33:45"), 7.5, 4));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("23:33:45"), 7.5, 2));


        MvcResult mvcResult = mockMvc.perform(get("/dailyfeeds/2017-08-05")).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        assertThat(modelAndView.getViewName(), is("daysummary"));
        assertThat(modelAndView.getModel().get("date"), is("2017-08-05"));

        List<FeedEntity> feeds = (List<FeedEntity>) modelAndView.getModel().get("feeds");

        assertThat(feeds.size(), is(5));
    }


    @Test
    public void summariesAreAdded() throws Exception
    {
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("19:33:45"), 7.5, 4));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("20:33:45"), 7.5, 2));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("21:33:45"), 7.5, 3));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("22:33:45"), 7.5, 4));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("23:33:45"), 7.5, 2));


        MvcResult mvcResult = mockMvc.perform(get("/dailyfeeds/2017-08-05")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        Object summariesObject = modelAndView.getModel().get("summaries");

        assertThat(summariesObject, is(notNullValue()));

        // check size and type
        assertThat(((Map<String, Map<String, String>>) summariesObject).size(), is(4));
        Map<String, Map<String, String>> summaries = (Map<String, Map<String, String>>) summariesObject;

        assertThat(summaries.get("2").get("first"), is("20:33:45"));
        assertThat(summaries.get("2").get("last"), is("23:33:45"));
        assertThat(summaries.get("2").get("count"), is("2"));
        assertThat(summaries.get("2").get("progress"), is("7%"));
    }

    @Test
    public void canGetSummaryForCat() throws Exception
    {
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("19:33:45"), 7.5, 4));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("20:33:45"), 7.5, 2));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("21:33:45"), 7.5, 3));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("22:33:45"), 7.5, 4));
        feedRepository.save(new FeedEntity(
            Date.valueOf("2017-08-05"), Time.valueOf("23:33:45"), 7.5, 2));


        MvcResult mvcResult = mockMvc.perform(get("/dailyfeeds/2017-08-05/2")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        Object summariesObject = modelAndView.getModel().get("summaries");
        assertThat(summariesObject, is(notNullValue()));

        // check size and type
        assertThat(((Map<String, Map<String, String>>) summariesObject).size(), is(1));
        Map<String, Map<String, String>> summaries = (Map<String, Map<String, String>>) summariesObject;

        assertThat(summaries.get("2").get("first"), is("20:33:45"));
        assertThat(summaries.get("2").get("last"), is("23:33:45"));
        assertThat(summaries.get("2").get("count"), is("2"));
        assertThat(summaries.get("2").get("progress"), is("7%"));
    }
}
