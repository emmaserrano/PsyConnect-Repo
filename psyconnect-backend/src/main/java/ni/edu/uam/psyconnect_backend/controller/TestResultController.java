package ni.edu.uam.psyconnect_backend.controller;

import ni.edu.uam.psyconnect_backend.model.TestResult;
import ni.edu.uam.psyconnect_backend.service.TestResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class TestResultController {

    private final TestResultService service;

    public TestResultController(
            TestResultService service
    ) {
        this.service = service;
    }

    @PostMapping
    public TestResult saveResult(
            @RequestBody TestResult result
    ) {
        return service.saveResult(result);
    }

    @GetMapping("/{userId}")
    public List<TestResult> getHistory(
            @PathVariable Long userId
    ) {
        return service.getHistory(userId);
    }
}