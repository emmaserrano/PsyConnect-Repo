package ni.edu.uam.psyconnect_backend.model;

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

    public Psychologist(
            Long id,
            String name,
            String specialty,
            String phone,
            String city,
            String email,
            String description,
            String photoUrl,
            Boolean virtualAttention,
            Double rating,
            Boolean featured
    ) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.phone = phone;
        this.city = city;
        this.email = email;
        this.description = description;
        this.photoUrl = photoUrl;
        this.virtualAttention = virtualAttention;
        this.rating = rating;
        this.featured = featured;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(
            String specialty
    ) {
        this.specialty = specialty;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(
            String phone
    ) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(
            String city
    ) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description
    ) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(
            String photoUrl
    ) {
        this.photoUrl = photoUrl;
    }

    public Boolean getVirtualAttention() {
        return virtualAttention;
    }

    public void setVirtualAttention(
            Boolean virtualAttention
    ) {
        this.virtualAttention = virtualAttention;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(
            Double rating
    ) {
        this.rating = rating;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(
            Boolean featured
    ) {
        this.featured = featured;
    }
}