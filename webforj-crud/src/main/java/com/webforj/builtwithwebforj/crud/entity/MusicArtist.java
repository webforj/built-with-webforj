package com.webforj.builtwithwebforj.crud.entity;

import com.webforj.data.HasEntityKey;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * Entity representing a music artist or band.
 * Stores basic information about musical artists including their name, genre,
 * country of origin, formation year, active status, and biography.
 */
@Entity
@Table(name = "music_artists")
public class MusicArtist implements HasEntityKey {

	/**
	 * Unique identifier for the artist.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Name of the artist or band.
	 */
	@NotBlank(message = "Artist name is required")
	@Size(max = 100, message = "Artist name must be less than 100 characters")
	@Column(name = "name", nullable = false, length = 100)
	private String name;

	/**
	 * Musical genre of the artist.
	 */
	@NotBlank(message = "Artist genre is required")
	@Size(max = 50, message = "Genre must be less than 50 characters")
	@Column(name = "genre", length = 50)
	private String genre;

	/**
	 * Country of origin for the artist.
	 */
	@NotBlank(message = "Artist country is required")
	@Size(max = 50, message = "Country must be less than 50 characters")
	@Column(name = "country", length = 50)
	private String country;

	/**
	 * Year the artist or band was formed.
	 */
	@NotNull(message = "Artist year formed is required")
	@Min(value = 1900, message = "Year must be 1900 or later")
	@Max(value = 2025, message = "Year must be current year or earlier")
	@Column(name = "year_formed")
	private Double yearFormed;

	/**
	 * Whether the artist is currently active.
	 */
	@Column(name = "is_active")
	private Boolean isActive = true;

	/**
	 * Brief biography or description of the artist.
	 */
	@Size(max = 500, message = "Biography must be less than 500 characters")
	@Column(name = "biography", length = 500)
	private String biography;

	/**
	 * Default constructor required by JPA.
	 */
	public MusicArtist() {
	}

	/**
	 * Creates a new music artist with the specified name.
	 * Sets the artist as active by default.
	 *
	 * @param name the name of the artist or band
	 */
	public MusicArtist(String name) {
		this.name = name;
		this.isActive = true;
	}

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

	/**
	 * Returns a string representation of the music artist.
	 *
	 * @return string representation containing all artist fields
	 */
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

	/**
	 * Returns the entity key for WebforJ integration.
	 *
	 * @return the unique identifier of this artist
	 */
	@Override
	public Object getEntityKey() {
		return id;
	}
}
