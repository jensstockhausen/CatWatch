package de.famst.logparser;

import de.famst.activity.Activity;
import de.famst.activity.Feed;
import de.famst.activity.GraphHandler;
import org.apache.commons.io.FileUtils;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SvgGraphHandler implements GraphHandler
{
    private static Logger LOG = LoggerFactory.getLogger(SvgGraphHandler.class);
    private final float secondsPerDay;

    private int w;
    private int h;
    private SVGGraphics2D graph;

    Map<LocalDate, List<Activity>> activitiesPerDay;
    Map<LocalDate, List<Feed>> feedsPerDay;

    private float border;
    private float skip;
    private float stepY;

    public SvgGraphHandler()
    {
        activitiesPerDay = new HashMap<>();
        feedsPerDay = new HashMap<>();

        secondsPerDay = 24 * 60 * 60 - 1;
    }

    @Override
    public void drawActivity(Activity activity)
    {
        LOG.trace("draw activity [{}]", activity);

        LocalDate ld = activity.getLocalDate();

        if (!activitiesPerDay.containsKey(ld))
        {
            activitiesPerDay.put(ld, new ArrayList<>());
        }

        activitiesPerDay.get(ld).add(activity);
    }

    @Override
    public void drawFeed(Feed feed)
    {
        LOG.trace("draw feed [{}]", feed);

        LocalDate ld = feed.getOpen().getLocalDate();

        if (!feedsPerDay.containsKey(ld))
        {
            feedsPerDay.put(ld, new ArrayList<>());
        }

        feedsPerDay.get(ld).add(feed);
    }

    @Override
    public void finish()
    {
        w = 1024;
        h = activitiesPerDay.size() * 50;

        graph = new SVGGraphics2D(w, h);

        border = 5.0f;
        skip = 3.0f;
        stepY = (h - (2f * border)) / (float) activitiesPerDay.size();

        graph.setColor(new Color(0x000000));
        graph.setStroke(new BasicStroke(0.5f));
        graph.fill(new Rectangle2D.Float(0, 0, w, h));

        Font font = new Font("Sans", Font.PLAIN, 9);
        graph.setFont(font);

        drawDays();

        try
        {
            LOG.info("Save SVG");

            String svgDocument = graph.getSVGDocument();
            FileUtils.writeStringToFile(new File("./build/activity.svg"), svgDocument, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void drawDays()
    {
        LOG.info("draw days [{}]", activitiesPerDay.size());

        float idx[] = new float[1];
        idx[0] = 0.0f;

        activitiesPerDay.keySet().stream().forEach(
            ld -> {
                LOG.info("day [{}]", ld);

                float x = border;
                float y = (h - border) - stepY * idx[0] - stepY;

                graph.setColor(new Color(0x358D43));
                graph.setStroke(new BasicStroke(0.5f));

                graph.draw(new Rectangle2D.Float(x, y, w - (2.0f * border), stepY - skip));
                graph.drawString(ld.toString(), x + 1.0f, y + stepY - skip - 1.0f);

                for (float h = 1; h < 23; h++)
                {
                    float xOffset = border + (w - (2.0f * border)) / secondsPerDay * (h * 60 * 60);

                    if ((h == 12) || (h == 6) || (h == 18))
                    {
                        graph.setStroke(new BasicStroke(0.5f));
                        graph.draw(new Line2D.Float(xOffset, y, xOffset, y + stepY - skip));
                    } else
                    {
                        graph.setStroke(new BasicStroke(0.5f));
                        graph.draw(new Line2D.Float(xOffset, y, xOffset, y + stepY / 4.0f - skip));
                    }
                }

                //drawActivities(x, y, ld, activitiesPerDay.get(ld));
                drawFeeds(x, y, ld, feedsPerDay.get(ld));

                idx[0] += 1.0f;
            }
        );

    }


    private void drawActivities(float x, float y, LocalDate ld, List<Activity> activities)
    {
        float base = 2.0f;
        float height = (stepY - skip - 2.0f) / 4.0f;

        activities.forEach(a ->
        {
            float xOffset = offsetFromTime(a);
            float yOffset = stepY - skip - height * (a.getChannel() - 2) - 1.0f;

            LOG.info("  draw activity [{}]", a);

            if (a.getOperation() == Activity.OPERATION.OPENING)
            {
                Path2D.Double tri = new Path2D.Double();

                tri.moveTo(x + xOffset - base, y + yOffset);
                tri.lineTo(x + xOffset, y + yOffset - height);
                tri.lineTo(x + xOffset, y + yOffset);
                tri.lineTo(x + xOffset - base, y + yOffset);

                graph.setColor(colorForActivity(a).darker().darker());
                graph.setStroke(new BasicStroke(0.5f));
                graph.draw(new Line2D.Float(
                    x + xOffset, y + yOffset - height * 0.5f,
                    x + xOffset, y + yOffset));

                //graph.fill(tri);

                //graph.setColor(new Color(0));
                //graph.setStroke(new BasicStroke(0.3f));
                //graph.draw(tri);

            } else
            {
                Path2D.Double tri = new Path2D.Double();

                tri.moveTo(x + xOffset, y + yOffset - height);
                tri.lineTo(x + xOffset + base, y + yOffset - height);
                tri.lineTo(x + xOffset, y + yOffset);
                tri.lineTo(x + xOffset, y + yOffset - height);

                graph.setColor(colorForActivity(a).darker().darker());
                graph.setStroke(new BasicStroke(0.5f));
                graph.draw(new Line2D.Float(
                    x + xOffset, y + yOffset - height,
                    x + xOffset, y + yOffset - height * 0.5f));

                //graph.fill(tri);

                //graph.setColor(new Color(0));
                //graph.setStroke(new BasicStroke(0.3f));
                //graph.draw(tri);
            }

        });
    }

    private void drawFeeds(float x, float y, LocalDate ld, List<Feed> feeds)
    {
        float base = 2.0f;
        float height = (stepY - skip - 2.0f) / 4.0f;

        feeds.forEach(f ->
        {
            float xOffsetO = offsetFromTime(f.getOpen());
            float xOffsetC = offsetFromTime(f.getClose());

            float xDuration = xOffsetC - xOffsetO;

            if (xDuration < 1.0f)
            {
                xDuration = 1.0f;
            }

            float yOffset = stepY - skip - height * (f.channel() - 2) - 1.0f;

            LOG.info("  draw feed [{}]", f);

            graph.setColor(colorForActivity(f.getOpen()));
            graph.fill(new Rectangle2D.Float(x + xOffsetO, y + yOffset - height + 1.0f,
                xDuration, height - 2.0f));
        });
    }


    private Color colorForActivity(Activity activity)
    {
        switch (activity.getChannel())
        {
            case 2:
                return new Color(0x6E2B9C);
            case 3:
                return new Color(0xE84139);
            case 4:
                return new Color(0xFFB64B);
            case 5:
                return new Color(0xF6F033);
        }

        return new Color(0);
    }

    private float offsetFromTime(Activity activity)
    {
        long secondOfDay = activity.getLocalTime().toSecondOfDay();
        return (w - (2.0f * border)) / secondsPerDay * (float) secondOfDay;
    }


}
