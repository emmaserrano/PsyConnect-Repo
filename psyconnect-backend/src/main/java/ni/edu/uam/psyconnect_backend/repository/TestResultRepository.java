package ni.edu.uam.psyconnect_backend.repository;

import ni.edu.uam.psyconnect_backend.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResultRepository
        extends JpaRepository<TestResult, Long> {

    List<TestResult> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );
}