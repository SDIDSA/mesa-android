package org.luke.mesa.data.date_time;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Time {
    private final int hour;
    private final int minute;
    private final int second;

    private Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public static Time of(int hour, int minute, int second) {
        return new Time(hour, minute, second);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    private String field(int val) {
        return (val < 10 ? "0" : "") + val;
    }

    @NonNull
    @Override
    public String toString() {
        return field(hour) + ':' + field(minute) + ':' + field(second);
    }

    @NonNull
    public Time copy() {
        return of(hour, minute, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return hour == time.hour && minute == time.minute && second == time.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour, minute, second);
    }
}
