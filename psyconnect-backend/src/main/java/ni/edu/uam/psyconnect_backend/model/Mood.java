package ni.edu.uam.psyconnect_backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "moods")
public class Mood {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private Long userId;

    private String mood;

    private LocalDate date;

    public Mood() {
    }

    public Mood(
            Long id,
            Long userId,
            String mood,
            LocalDate date
    ) {
        this.id = id;
        this.userId = userId;
        this.mood = mood;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMood() {
        return mood;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}