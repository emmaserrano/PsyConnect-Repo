package ni.edu.uam.psyconnect_backend.service;

import ni.edu.uam.psyconnect_backend.model.TestResult;
import ni.edu.uam.psyconnect_backend.repository.TestResultRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestResultService {

    private final TestResultRepository repository;

    public TestResultService(
            TestResultRepository repository
    ) {
        this.repository = repository;
    }

    public TestResult saveResult(
            TestResult result
    ) {

        result.setCreatedAt(
                LocalDateTime.now()
        );

        return repository.save(result);
    }

    public List<TestResult> getHistory(
            Long userId
    ) {
        return repository
                .findByUserIdOrderByCreatedAtDesc(
                        userId
                );
    }
}