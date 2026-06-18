package ni.edu.uam.psyconnect_backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String category;

    private Integer percentage;

    private String level;

    private LocalDateTime createdAt;

    public TestResult() {
    }

    public TestResult(
            Long userId,
            String category,
            Integer percentage,
            String level,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.category = category;
        this.percentage = percentage;
        this.level = level;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public String getLevel() {
        return level;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}