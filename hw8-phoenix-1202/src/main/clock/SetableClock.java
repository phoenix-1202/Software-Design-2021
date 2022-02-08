package main.clock;

import java.time.Instant;

public class SetableClock implements Clock {
    private Instant now;

    public SetableClock(Instant newNow) {
        now = newNow;
    }

    public void setNow(Instant newNow) {
        now = newNow;
    }

    @Override
    public Instant now() {
        return now;
    }
}
