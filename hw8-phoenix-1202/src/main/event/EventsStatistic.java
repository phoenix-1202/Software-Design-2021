package main.event;

import java.util.Map;

public interface EventsStatistic {
    void incEvent(String eventName);

    Double getEventStatisticByName(String eventName);

    Map<String, Double> getAllEventStatistic();

    void printStatistic();
}
