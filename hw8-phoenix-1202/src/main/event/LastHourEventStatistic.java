package main.event;

import main.clock.Clock;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LastHourEventStatistic implements EventsStatistic {
    public Clock clock;
    private final Instant startTime;
    private final Map<String, List<Instant>> stat = new HashMap<>();

    private static final int SECONDS_IN_HOUR = 3600;
    private static final int MINUTES_IN_HOUR = 60;

    public LastHourEventStatistic(Clock newClock) {
        clock = newClock;
        startTime = clock.now();
    }

    @Override
    public void incEvent(String eventName) {
        stat.putIfAbsent(eventName, new ArrayList<>());
        List<Instant> curValue = stat.get(eventName);
        curValue.add(clock.now());
        stat.replace(eventName, curValue);
    }

    @Override
    public double getEventStatisticByName(String eventName) {
        if (!stat.containsKey(eventName)) {
            return 0.;
        }
        long inLastHour = stat.get(eventName).stream().filter(it -> (clock.now().getEpochSecond() - it.getEpochSecond()) <= SECONDS_IN_HOUR).count();
        return (double) inLastHour / MINUTES_IN_HOUR;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        Map<String, Double> answer = new HashMap<>();
        stat.keySet().forEach(key -> answer.put(key, getEventStatisticByName(key)));
        return answer;
    }

    public Map<String, Double> fullStatistic() {
        double curMinutes = (double) (clock.now().getEpochSecond() - startTime.getEpochSecond()) / MINUTES_IN_HOUR;
        Map<String, Double> answer = new HashMap<>();
        stat.keySet().forEach(key -> {
            double rpm = stat.get(key).size() / curMinutes;
            answer.put(key, rpm);
        });
        return answer;
    }

    @Override
    public void printStatistic() {
        System.out.println(".......... STATISTICS ..........");
        Map<String, Double> fullStat = fullStatistic();
        fullStat.forEach((key, value) -> System.out.println("Event " + key + ": " + value));
        System.out.println("................................");
    }
}
