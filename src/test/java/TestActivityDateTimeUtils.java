import de.famst.activity.Activity;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestActivityDateTimeUtils
{
    @Test
    public void activityHasInstanceGetter() throws Exception
    {
        String line = "2017-08-05 20:24:33.336 opening 4";
        Activity activity = new Activity(line);

        LocalDateTime ldt = LocalDateTime.ofInstant(activity.getInstant(), ZoneId.systemDefault());
        assertThat(ldt.getYear(), is(2017));
        assertThat(ldt.getMonthValue(), is(8));
        assertThat(ldt.getDayOfMonth(), is(5));

        assertThat(ldt.getHour(), is(20));
        assertThat(ldt.getMinute(), is(24));
        assertThat(ldt.getSecond(), is(33));

        assertThat(ldt.getNano(), is(336000000));
    }

    @Test
    public void activityHasLocalDateGetter() throws Exception
    {
        String line = "2017-08-05 20:24:33.336 opening 4";
        Activity activity = new Activity(line);

        LocalDate ld = activity.getLocalDate();

        assertThat(ld.getYear(), is(2017));
        assertThat(ld.getMonthValue(), is(8));
        assertThat(ld.getDayOfMonth(), is(5));
    }


    @Test
    public void activityHasLocalTimeGetter() throws Exception
    {
        String line = "2017-08-05 20:24:33.336 opening 4";
        Activity activity = new Activity(line);

        LocalTime lt = activity.getLocalTime();

        assertThat(lt.getHour(), is(20));
        assertThat(lt.getMinute(), is(24));
        assertThat(lt.getSecond(), is(33));

        assertThat(lt.getNano(), is(336000000));
    }



}
