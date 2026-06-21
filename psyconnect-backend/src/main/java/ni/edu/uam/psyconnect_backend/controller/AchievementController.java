package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.model.Achievement;
import ni.edu.uam.psyconnect_backend.service.AchievementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin("*")
public class AchievementController {

    private final AchievementService service;

    public AchievementController(
            AchievementService service
    ) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public List<Achievement> getAchievements(
            @PathVariable Long userId
    ) {
        return service.getAchievements(
                userId
        );
    }
}