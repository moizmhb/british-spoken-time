package com.example.britishtime.formatter;

import org.springframework.stereotype.Component;

/**
 * Simple factory for formatters. Extensible to other locales.
 */
@Component
public class TimeFormatterFactory {

    public TimeFormatter getFormatter(String key) {
        if (key == null || key.isBlank() || key.equalsIgnoreCase("british")) {
            return new BritishTimeFormatter();
        }
        // Future: other locale implementations
        throw new IllegalArgumentException("Unknown formatter: " + key);
    }
}
