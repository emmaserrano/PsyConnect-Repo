package ni.edu.uam.psyconnect_backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String badge;

    private String description;

    private LocalDate unlockedAt;

    public Achievement() {
    }

    public Achievement(
            Long id,
            Long userId,
            String badge,
            String description,
            LocalDate unlockedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.badge = badge;
        this.description = description;
        this.unlockedAt = unlockedAt;
    }

    // getters y setters
}
