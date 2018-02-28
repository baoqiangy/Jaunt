package csc445.missouriwestern.edu.jaunt.fragments.hours;

import org.joda.time.LocalTime;

/**
 * Created by byan on 2/17/2018.
 */

class TimeRange {
    private LocalTime start;
    private LocalTime end;

    public TimeRange(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return start.toString("K:m a") + " - " + end.toString("K:m a");
    }
}
