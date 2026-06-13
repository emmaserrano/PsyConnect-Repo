package ni.edu.uam.psyconnect_backend.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;

@Entity
@Table(name = "psychologists")
public class Psychologist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String specialty;

    private String phone;

    private String city;

    private String email;

    private String description;

    private String photoUrl;

    private Boolean virtualAttention;

    private Double rating;

    private Boolean featured;

    public Psychologist() {
    }

    // constructor
    // getters
    // setters

}
