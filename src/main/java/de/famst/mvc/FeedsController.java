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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        model.addAttribute("date", date);
        model.addAttribute("feeds", feedByDate);

        Map<String, Map<String, String>> summaries = new HashMap<>();

        for (int c = 2; c < 6; c++)
        {
            Map<String, String> summary = new HashMap<>();

            List<FeedEntity> feedByDateChannel = feedRepository.findByDateAndChannelOrderByTimeAsc(Date.valueOf(date), c);

            if (feedByDateChannel.size() != 0)
            {
                String first = feedByDateChannel.get(0).getTime().toString();
                String last = feedByDateChannel.get(feedByDateChannel.size() - 1).getTime().toString();
                String count = String.format("%3d", feedByDateChannel.size());

                summary.put("first", first);
                summary.put("last", last);
                summary.put("count", count);
            } else
            {
                summary.put("first", "");
                summary.put("last", "");
                summary.put("count", "0");
            }

            summaries.put(String.valueOf(c), summary);
        }

        model.addAttribute("summaries", summaries);

        return "daysummary";
    }


    @RequestMapping("/dailyfeeds/{date}/{channel}")
    public String dailyFeedChannel(
        @PathVariable("date") String date,
        @PathVariable("channel") Integer channel, Model model)
    {
        List<FeedEntity> feedByDate = feedRepository.findByDateAndChannelOrderByTimeAsc(Date.valueOf(date), channel);

        model.addAttribute("date", date);
        model.addAttribute("feeds", feedByDate);

        return "daysummary";
    }


}
