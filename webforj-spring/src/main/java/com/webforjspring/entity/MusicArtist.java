package com.webforjspring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Entity
@Table(name = "music_artists")
public class MusicArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Artist name is required")
    @Size(max = 100, message = "Artist name must be less than 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 50, message = "Genre must be less than 50 characters")
    @Column(name = "genre", length = 50)
    private String genre;

    @Size(max = 50, message = "Country must be less than 50 characters")
    @Column(name = "country", length = 50)
    private String country;

    @Min(value = 1900, message = "Year must be 1900 or later")
    @Max(value = 2024, message = "Year must be 2024 or earlier")
    @Column(name = "year_formed")
    private Double yearFormed;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Size(max = 500, message = "Biography must be less than 500 characters")
    @Column(name = "biography", length = 500)
    private String biography;

    // Default constructor (required by JPA)
    public MusicArtist() {
    }

    // Constructor with required fields
    public MusicArtist(String name) {
        this.name = name;
        this.isActive = true;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getYearFormed() {
        return yearFormed;
    }

    public void setYearFormed(Double yearFormed) {
        this.yearFormed = yearFormed;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String toString() {
        return "MusicArtist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", country='" + country + '\'' +
                ", yearFormed=" + yearFormed +
                ", isActive=" + isActive +
                ", biography='" + biography + '\'' +
                '}';
    }
}