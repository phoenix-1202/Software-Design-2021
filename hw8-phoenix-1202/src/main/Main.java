package main;

import main.clock.Clock;
import main.clock.NormalClock;
import main.event.EventsStatistic;
import main.event.LastHourEventStatistic;

import java.util.Map;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Clock clock = new NormalClock();
        EventsStatistic myStat = new LastHourEventStatistic(clock);
        myStat.incEvent("1");
        myStat.incEvent("1");
        sleep(1000);
        myStat.incEvent("2");
        myStat.incEvent("1");
        myStat.incEvent("2");
        sleep(1000);
        myStat.incEvent("1");
        Map<String, Double> allStat = myStat.getAllEventStatistic();
        System.out.println("........ LAST HOUR STATISTICS ........");
        allStat.keySet().forEach(key -> System.out.println("Event " + key + ": " + allStat.get(key)));
        System.out.println("......................................\n");
        myStat.printStatistic();
    }
}
