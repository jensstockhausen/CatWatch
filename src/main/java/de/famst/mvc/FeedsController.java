package de.famst.mvc;

import de.famst.persistance.FeedEntity;
import de.famst.persistance.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

@Controller
public class FeedsController
{
    @Autowired
    FeedRepository feedRepository;

    @RequestMapping("/today")
    public String today(Model model)
    {
        Date today = Date.valueOf(LocalDate.now());
        return dailyFeed(today.toString(), model);
    }


    @RequestMapping("/dailyfeeds/{date}")
    public String dailyFeed(
        @PathVariable("date") String date, Model model)
    {
        List<FeedEntity> feedByDate = feedRepository.findByDateOrderByTimeDesc(Date.valueOf(date));

        mapFeedsToModel(date, feedByDate, model);
        addNextPrevDay(date, model);

        return "daysummary";
    }

    @RequestMapping("/dailyfeeds/{date}/{channel}")
    public String dailyFeedChannel(
        @PathVariable("date") String date,
        @PathVariable("channel") Integer channel, Model model)
    {
        List<FeedEntity> feedByDate = feedRepository.findByDateAndChannelOrderByTimeDesc(Date.valueOf(date), channel);

        model.addAttribute("date", date);
        model.addAttribute("feeds", feedByDate);

        addNextPrevDay(date, model);

        Map<String, Map<String, String>> summaries = new HashMap<>();
        summaries.put(String.valueOf(channel), mapChannelToModel(date, channel));

        model.addAttribute("summaries", summaries);

        return "daysummary";
    }

    private void addNextPrevDay(@PathVariable("date") String date, Model model)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = Date.valueOf(date).toLocalDate();

        String prevDay = localDate.minusDays(1).format(formatter);
        String nextDay = localDate.plusDays(1).format(formatter);

        model.addAttribute("nextday", nextDay);
        model.addAttribute("prevday", prevDay);
    }


    private void mapFeedsToModel(String date, List<FeedEntity> feedByDate, Model model)
    {
        model.addAttribute("date", date);
        model.addAttribute("feeds", feedByDate);

        Map<String, Map<String, String>> summaries = new HashMap<>();

        for (int c = 2; c < 6; c++)
        {
            summaries.put(String.valueOf(c), mapChannelToModel(date, c));
        }

        model.addAttribute("summaries", summaries);
    }

    private Map<String, String>  mapChannelToModel(String date, int channel)
    {
        Map<String, String> summary = new HashMap<>();

        List<FeedEntity> feedByDateChannel = feedRepository.findByDateAndChannelOrderByTimeDesc(Date.valueOf(date), channel);
        int count = feedByDateChannel.size();

        if (count != 0)
        {
            String last = feedByDateChannel.get(0).getTime().toString();
            String first = feedByDateChannel.get(count - 1).getTime().toString();

            String progess = String.format("%d%%", (int) (round((float) count / 30.0f * 100.0f)));

            summary.put("first", first);
            summary.put("last", last);
            summary.put("count", String.valueOf(count));
            summary.put("progress", progess);
        } else
        {
            summary.put("first", "");
            summary.put("last", "");
            summary.put("count", "0");
            summary.put("progress", "0%");
        }

        return summary;

    }


    /**
     *  for statistics:
     *
     SELECT ROUND(duration, -2)    AS bucket,
     COUNT(*)                    AS COUNT,
     RPAD('', LN(COUNT(*)), '*') AS bar
     FROM   feed_entity
     GROUP  BY bucket
     order by bucket;
     *
     *
     SELECT floor(duration/60)*60    AS bucket,
     COUNT(*)                    AS COUNT,
     RPAD('', LN(COUNT(*)), '*') AS bar
     FROM   feed_entity
     //where channel = 3 and date = '2017-09-05'
     GROUP  BY bucket
     order by bucket;
     *
     *
     *
     SELECT HOUR(TIME)    AS bucket,
     COUNT(*)                    AS COUNT,
     RPAD('',(COUNT(*)/10), '*') AS bar
     FROM   feed_entity
     GROUP  BY bucket
     order by bucket;
     *
     *
     */


}
