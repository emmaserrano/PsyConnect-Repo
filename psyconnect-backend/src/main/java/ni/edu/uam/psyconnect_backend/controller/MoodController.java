package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.model.Mood;
import ni.edu.uam.psyconnect_backend.service.MoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/moods")
@CrossOrigin("*")
public class MoodController {

    private final MoodService moodService;

    public MoodController(
            MoodService moodService
    ) {
        this.moodService = moodService;
    }

    @PostMapping
    public Mood saveMood(
            @RequestBody Mood mood
    ) {
        return moodService.saveMood(
                mood
        );
    }

    @GetMapping("/today/{userId}")
    public ResponseEntity<Boolean> hasMoodToday(
            @PathVariable Long userId
    ) {

        Optional<Mood> mood =
                moodService.getTodayMood(
                        userId
                );

        return ResponseEntity.ok(
                mood.isPresent()
        );
    }
}