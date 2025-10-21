package com.example.britishtime.service;

import com.example.britishtime.exception.InvalidTimeException;
import com.example.britishtime.formatter.TimeFormatterStrategy;
import com.example.britishtime.formatter.TimeFormatterFactory;
import com.example.britishtime.model.TimeResponse;
import org.springframework.stereotype.Service;

@Service
public class TimeService {

    private final TimeFormatterStrategy formatter;

    public TimeService(TimeFormatterFactory factory) {
        // Currently only British formatter exists; factory allows extensibility.
        this.formatter = factory.getFormatter("british");
    }

    public TimeResponse toSpokenTime(String hhmm) {
        if (hhmm == null || hhmm.isBlank()) {
            throw new InvalidTimeException("time parameter is required");
        }
        String[] parts = hhmm.trim().split(":");
        if (parts.length != 2) {
            throw new InvalidTimeException("time must be in HH:mm format");
        }
        try {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            return toSpokenTime(hour, minute);
        } catch (NumberFormatException ex) {
            throw new InvalidTimeException("invalid numeric time format");
        }
    }

    public TimeResponse toSpokenTime(int hour, int minute) {
        validate(hour, minute);
        String spoken = formatter.format(hour, minute);
        String original = String.format("%02d:%02d", hour, minute);
        return new TimeResponse(original, spoken);
    }

    private void validate(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            throw new InvalidTimeException("hour must be between 0 and 23");
        }
        if (minute < 0 || minute > 59) {
            throw new InvalidTimeException("minute must be between 0 and 59");
        }
    }
}
