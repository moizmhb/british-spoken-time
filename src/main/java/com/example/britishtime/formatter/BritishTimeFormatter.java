package com.example.britishtime.formatter;

import java.util.Map;

public class BritishTimeFormatter implements TimeFormatter {

    private static final Map<Integer, String> SMALL = Map.ofEntries(
            Map.entry(0, "zero"), Map.entry(1, "one"), Map.entry(2, "two"),
            Map.entry(3, "three"), Map.entry(4, "four"), Map.entry(5, "five"),
            Map.entry(6, "six"), Map.entry(7, "seven"), Map.entry(8, "eight"),
            Map.entry(9, "nine"), Map.entry(10, "ten"), Map.entry(11, "eleven"),
            Map.entry(12, "twelve"), Map.entry(13, "thirteen"), Map.entry(14, "fourteen"),
            Map.entry(15, "fifteen"), Map.entry(16, "sixteen"), Map.entry(17, "seventeen"),
            Map.entry(18, "eighteen"), Map.entry(19, "nineteen")
    );

    private static final Map<Integer, String> TENS = Map.of(
            20, "twenty",
            30, "thirty",
            40, "forty",
            50, "fifty"
    );

    @Override
    public String format(int hour24, int minute) {
        // Special cases
        if (hour24 == 0 && minute == 0) return "midnight";
        if (hour24 == 12 && minute == 0) return "noon";

        int hour12 = to12Hour(hour24);
        String hourWord = numberToWords(hour12);

        if (minute == 0) {
            return String.format("%s o'clock", hourWord);
        }
        if (minute == 15) {
            return String.format("quarter past %s", hourWord);
        }
        if (minute == 30) {
            return String.format("half past %s", hourWord);
        }
        if (minute == 45) {
            return String.format("quarter to %s", numberToWords(nextHour(hour12)));
        }

        if (minute < 30) {
            if (minute % 5 == 0) {
                return String.format("%s past %s", numberToWords(minute), hourWord);
            } else {
                return String.format("%s %s", hourWord, numberToWords(minute)); // e.g., six thirty two
            }
        } else { // minute > 30
            int to = 60 - minute;
            if (to % 5 == 0) {
                return String.format("%s to %s", numberToWords(to), numberToWords(nextHour(hour12)));
            } else {
                return String.format("%s %s", hourWord, numberToWords(minute)); // e.g., six thirty two
            }
        }
    }

    private static int nextHour(int hour12) {
        return hour12 == 12 ? 1 : hour12 + 1;
    }

    private static int to12Hour(int hour24) {
        int h = hour24 % 12;
        return h == 0 ? 12 : h;
    }

    private static String numberToWords(int n) {
        if (n < 0 || n > 59) throw new IllegalArgumentException("number out of range");
        if (n < 20) return SMALL.get(n);
        if (n % 10 == 0) return TENS.get(n);
        int tens = (n / 10) * 10;
        int ones = n % 10;
        String tensWord = TENS.get(tens);
        String onesWord = SMALL.get(ones);
        return tensWord + " " + onesWord;
    }
}
