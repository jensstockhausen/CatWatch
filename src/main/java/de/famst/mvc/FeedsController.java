package de.famst.mvc;

import de.famst.persistance.FeedEntity;
import de.famst.persistance.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

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
        List<FeedEntity> feedByDate = feedRepository.findByDateOrderByTimeAsc(Date.valueOf(date));

        model.addAttribute("date", date);
        model.addAttribute("feeds", feedByDate);

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
