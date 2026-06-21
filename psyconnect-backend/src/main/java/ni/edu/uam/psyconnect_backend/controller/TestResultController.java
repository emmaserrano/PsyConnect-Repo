package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.model.TestResult;
import ni.edu.uam.psyconnect_backend.service.AchievementService;
import ni.edu.uam.psyconnect_backend.service.TestResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class TestResultController {

    private final TestResultService service;
    private final AchievementService achievementService;

    public TestResultController(
            TestResultService service,
            AchievementService achievementService
    ) {
        this.service = service;
        this.achievementService = achievementService;
    }

    @PostMapping
    public TestResult saveResult(
            @RequestBody TestResult result
    ) {

        TestResult savedResult =
                service.saveResult(
                        result
                );

        achievementService.unlock(
                result.getUserId(),
                "🌱 Primer Paso",
                "Completaste tu primer test"
        );

        return savedResult;
    }

    @GetMapping("/{userId}")
    public List<TestResult> getHistory(
            @PathVariable Long userId
    ) {
        return service.getHistory(userId);
    }
}