import de.famst.activity.Activity;
import de.famst.activity.ActivityParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.Is.is;

@RunWith(Parameterized.class)
public class TestActivity
{
    @Parameterized.Parameters(
    name = "{index}: line={0} -> {2} {3} {4}")
    public static Iterable<Object[]> data()
    {
        return Arrays.asList(new Object[][]{

            {"2017-08-05 20:24:33.336 opening 4",
                "2017-08-05 20:24:33.336", Activity.OPERATION.OPENING, 4, true},
            {"2012-12-08 15:12:33.336 closing 5",
                "2012-12-08 15:12:33.336", Activity.OPERATION.CLOSING, 5, true},
            {"2012-12-08 15:12:33.336 asdf a",
                "2012-12-08 15:12:33.336", Activity.OPERATION.OTHER, null, false},
            {"2012-12-08 15:12:33.336 started 0",
                "2012-12-08 15:12:33.336", Activity.OPERATION.OTHER, 0, true},
            {"  ",
                "", null, null, false},

        });
    }

    private String line;
    private final String expDate;
    private final Activity.OPERATION expOperation;
    private final Integer expChannel;
    private final Boolean valid;

    public TestActivity(String line, String expDate, Activity.OPERATION expOperation, Integer expChannel, Boolean  valid)
    {
        this.line = line;
        this.expDate = expDate;
        this.expOperation = expOperation;
        this.expChannel = expChannel;
        this.valid = valid;
    }

    @Test
    public void canReadActivityLine() throws Exception
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        //String line = "2017-08-05 20:24:33.336761 opening 4";
        Activity activity = new Activity(line);

        assertThat(activity, is(notNullValue()));
        assertThat(activity.getOperation(), is(expOperation));

        if (activity.getTimestamp() != null)
        {
            Date activityTimestamp = activity.getTimestamp();
            String timestamp = dt.format(activityTimestamp);

            assertThat(timestamp, is(expDate));
            assertThat(activity.getChannel(), is(expChannel));
        }

        assertThat(activity.isValid(), is(valid));
    }


}
