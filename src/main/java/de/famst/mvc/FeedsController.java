package de.famst.mvc;

import de.famst.persistance.FeedEntity;
import de.famst.persistance.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.util.List;

@Controller
public class FeedsController
{
    @Autowired
    FeedRepository feedRepository;

    @RequestMapping("/today")
    public String today(Model model)
    {
        model.addAttribute("date", "1234");

        return "daysummary";
    }


    @RequestMapping("/dailyfeeds/{date}")
    public String today(@PathVariable("date") String date, Model model)
    {
        List<FeedEntity> feedByDate = feedRepository.findByDate(Date.valueOf(date));

        model.addAttribute("date", date);
        model.addAttribute("feeds", feedByDate);

        return "daysummary";
    }

}
