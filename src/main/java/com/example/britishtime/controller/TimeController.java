// File: src/main/java/com/example/britishtime/controller/TimeController.java
package com.example.britishtime.controller;

import com.example.britishtime.model.TimeResponse;
import com.example.britishtime.service.TimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/spoken-time")
@Tag(
        name = "British Spoken Time API",
        description = "Convert 24-hour digital time into British English spoken form (e.g. 07:30 → 'half past seven')."
)
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @Operation(
            summary = "Convert time (HH:mm) to British spoken form",
            description = "Takes a time in 24-hour format and returns its equivalent British spoken form. "
                    + "Handles special cases like 'midnight' and 'noon'."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful conversion",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TimeResponse.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{ \"original\": \"07:30\", \"spoken\": \"half past seven\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid time input",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{ \"code\": \"invalid_time\", \"message\": \"hour must be between 0 and 23\" }"
                            )
                    )
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TimeResponse spokenTime(
            @Parameter(description = "Time in 24-hour format (HH:mm)", example = "07:30")
            @RequestParam("time") String time) {
        return timeService.toSpokenTime(time);
    }

    @Operation(
            summary = "Convert hour and minute to British spoken form",
            description = "Accepts hour and minute as path variables and returns the British spoken form."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful conversion",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TimeResponse.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{ \"original\": \"09:45\", \"spoken\": \"quarter to ten\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid hour or minute",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{ \"code\": \"invalid_time\", \"message\": \"minute must be between 0 and 59\" }"
                            )
                    )
            )
    })
    @GetMapping(value = "/{hour}/{minute}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TimeResponse spokenTime(
            @Parameter(description = "Hour in 24-hour format (0–23)", example = "9")
            @PathVariable int hour,
            @Parameter(description = "Minute in range (0–59)", example = "45")
            @PathVariable int minute) {
        return timeService.toSpokenTime(hour, minute);
    }

    @Operation(
            summary = "Upload CSV file with times",
            description = "Accepts a CSV file containing one or multiple times (HH:mm) and returns a list of British spoken forms."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "File processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimeResponse.class),
                            examples = @ExampleObject(value = "[{ \"original\": \"07:30\", \"spoken\": \"half past seven\" }, { \"original\": \"09:45\", \"spoken\": \"quarter to ten\" }]"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid file or malformed data",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"code\": \"invalid_file\", \"message\": \"Invalid CSV format or unreadable input\" }"))
            )
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TimeResponse> uploadCsv(
            @Parameter(description = "CSV file with times (e.g., times.csv)", required = true)
            @RequestPart("file") MultipartFile file) throws IOException {

        List<TimeResponse> responses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] times = line.split(",");
                for (String time : times) {
                    time = time.trim();
                    if (!time.isEmpty()) {
                        responses.add(timeService.toSpokenTime(time));
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading CSV file", e);
        }

        return responses;
    }
}
