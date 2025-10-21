package com.example.britishtime.formatter;

public interface TimeFormatterStrategy {
    String format(int hour24, int minute);
}
