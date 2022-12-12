package org.luke.mesa.data.date_time;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Date {
    private final int year;
    private final int month;
    private final int day;

    private Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static Date of(int year, int month, int day) {
        return new Date(year, month, day);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    private String field(int val) {
        return (val < 10 ? "0" : "") + val;
    }

    @NonNull
    @Override
    public String toString() {
        return field(day) + '/' + field(month) + '/' + year;
    }

    @NonNull
    public Date copy() {
        return of(year, month, day);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return year == date.year && month == date.month && day == date.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }
}
