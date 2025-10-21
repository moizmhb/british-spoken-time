package com.example.britishtime.formatter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BritishTimeFormatterStrategyTest {

    private final BritishTimeFormatter formatter = new BritishTimeFormatter();

    @ParameterizedTest(name = "{0}:{1} -> {2}")
    @CsvSource({
            "1,0,one o'clock",
            "2,5,five past two",
            "3,10,ten past three",
            "4,15,quarter past four",
            "5,20,twenty past five",
            "6,25,twenty five past six",
            "6,32,six thirty two",
            "7,30,half past seven",
            "7,35,twenty five to eight",
            "8,40,twenty to nine",
            "9,45,quarter to ten",
            "10,50,ten to eleven",
            "11,55,five to twelve",
            "0,0,midnight",
            "12,0,noon",
            "23,45,quarter to twelve",
            "0,5,five past twelve",
            "12,5,five past twelve",
            "13,30,half past one",
            "22,15,quarter past ten"
    })
    void shouldFormatAllGivenExamplesCorrectly(int hour, int minute, String expected) {
        assertEquals(expected, formatter.format(hour, minute));
    }

    @Test
    void shouldThrowExceptionForInvalidMinute() {
        assertThrows(IllegalArgumentException.class, () -> formatter.format(10, 70));
    }

    @Test
    void shouldThrowExceptionForNegativeMinute() {
        assertThrows(IllegalArgumentException.class, () -> formatter.format(10, -1));
    }
}
