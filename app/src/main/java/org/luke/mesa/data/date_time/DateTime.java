package org.luke.mesa.data.date_time;

import androidx.annotation.NonNull;

import java.util.Objects;

public class DateTime {
    private final Date date;
    private final Time time;

    private DateTime(Date date, Time time) {
        this.date = date;
        this.time = time;
    }

    private DateTime(int year, int month, int day, int hour, int minute, int second) {
        this.date = Date.of(year, month, day);
        this.time = Time.of(hour, minute, second);
    }

    public static DateTime of(Date date, Time time) {
        return new DateTime(date, time);
    }

    public static DateTime of(int year, int month, int day, int hour, int minute, int second) {
        return new DateTime(year, month, day, hour, minute, second);
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public int getYear() {
        return date.getYear();
    }

    public int getMonth() {
        return date.getMonth();
    }

    public int getDay() {
        return date.getDay();
    }

    public int getHour() {
        return time.getHour();
    }

    public int getMinute() {
        return time.getMinute();
    }

    public int getSecond() {
        return time.getSecond();
    }

    @Override
    public String toString() {
        return date.toString() + " " + time.toString();
    }

    @NonNull
    public DateTime copy() {
        return of(date.copy(), time.copy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTime dateTime = (DateTime) o;
        return Objects.equals(date, dateTime.date) && Objects.equals(time, dateTime.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time);
    }
}
