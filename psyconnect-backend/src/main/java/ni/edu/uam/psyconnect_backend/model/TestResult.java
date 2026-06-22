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

    private Integer score;

    private Integer maxScore;

    private Integer totalQuestions;

    private Integer percentage;

    private String level;

    private LocalDateTime createdAt;

    public TestResult() {
    }

    public TestResult(
            Long userId,
            String category,
            Integer score,
            Integer maxScore,
            Integer totalQuestions,
            Integer percentage,
            String level,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.category = category;
        this.score = score;
        this.maxScore = maxScore;
        this.totalQuestions = totalQuestions;
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

    public Integer getScore() {
        return score;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
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

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
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