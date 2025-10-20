package com.example.britishtime.service;

import com.example.britishtime.exception.InvalidTimeException;
import com.example.britishtime.formatter.TimeFormatterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimeServiceTest {

    private TimeService service;

    @BeforeEach
    void setUp() {
        service = new TimeService(new TimeFormatterFactory());
    }

    @Test
    void shouldReturnValidResponseFromString() {
        var res = service.toSpokenTime("07:30");
        assertEquals("07:30", res.original());
        assertEquals("half past seven", res.spoken());
    }

    @Test
    void shouldReturnValidResponseFromInts() {
        var res = service.toSpokenTime(11, 55);
        assertEquals("five to twelve", res.spoken());
    }

    @Test
    void shouldThrowInvalidTimeForBadFormat() {
        assertThrows(InvalidTimeException.class, () -> service.toSpokenTime("badinput"));
    }

    @Test
    void shouldThrowInvalidTimeForNull() {
        assertThrows(InvalidTimeException.class, () -> service.toSpokenTime((String) null));
    }

    @Test
    void shouldThrowInvalidTimeForOutOfRangeHour() {
        assertThrows(InvalidTimeException.class, () -> service.toSpokenTime(25, 0));
    }

    @Test
    void shouldThrowInvalidTimeForOutOfRangeMinute() {
        assertThrows(InvalidTimeException.class, () -> service.toSpokenTime(10, 99));
    }

    @Test
    void shouldHandleLeadingZeros() {
        var res = service.toSpokenTime("00:00");
        assertEquals("midnight", res.spoken());
    }
}
