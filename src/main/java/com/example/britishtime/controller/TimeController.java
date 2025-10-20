package com.example.britishtime.controller;

import com.example.britishtime.model.TimeResponse;
import com.example.britishtime.service.TimeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spoken-time")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TimeResponse spokenTime(@RequestParam("time") String time) {
        return timeService.toSpokenTime(time);
    }

    @GetMapping(value = "/{hour}/{minute}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TimeResponse spokenTime(@PathVariable int hour, @PathVariable int minute) {
        return timeService.toSpokenTime(hour, minute);
    }
}
