package test;

import main.clock.SetableClock;
import main.event.EventsStatistic;
import main.event.LastHourEventStatistic;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Map;

public class EventsStatisticTest {
    SetableClock clock = new SetableClock(Instant.EPOCH);

    private static final double EPS = 1e-5;

    @Test
    public void test1() {
        clock.setNow(Instant.EPOCH);
        EventsStatistic myStat = new LastHourEventStatistic(clock);
        myStat.incEvent("1");
        myStat.incEvent("1");
        myStat.incEvent("1");
        // + 20 minutes
        clock.setNow(clock.now().plusSeconds(1200));
        Assert.assertEquals(myStat.getEventStatisticByName("1"), 3. / 60, EPS);
    }

    @Test
    public void test2() {
        clock.setNow(Instant.EPOCH);
        EventsStatistic myStat = new LastHourEventStatistic(clock);
        myStat.incEvent("1");
        myStat.incEvent("1");
        myStat.incEvent("1");
        // + 20 minutes
        clock.setNow(clock.now().plusSeconds(1200));
        myStat.incEvent("1");
        myStat.incEvent("2");
        // + 1 hour
        clock.setNow(clock.now().plusSeconds(3600));
        Assert.assertEquals(myStat.getEventStatisticByName("1"), 1. / 60, EPS);
        Assert.assertEquals(myStat.getEventStatisticByName("2"), 1. / 60, EPS);
    }

    @Test
    public void test3() {
        clock.setNow(Instant.EPOCH);
        LastHourEventStatistic myStat = new LastHourEventStatistic(clock);
        myStat.incEvent("1");
        myStat.incEvent("1");
        myStat.incEvent("1");
        // + 20 minutes
        clock.setNow(clock.now().plusSeconds(1200));
        myStat.incEvent("1");
        myStat.incEvent("2");
        // + 1 hour
        clock.setNow(clock.now().plusSeconds(3600));
        myStat.incEvent("2");
        myStat.incEvent("3");
        Map<String, Double> allStat = myStat.fullStatistic();
        Assert.assertEquals(allStat.get("1"), 4. / 80, EPS);
        Assert.assertEquals(allStat.get("2"), 2. / 80, EPS);
        Assert.assertEquals(allStat.get("3"), 1. / 80, EPS);
    }
}
