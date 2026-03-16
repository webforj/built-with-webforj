package com.webforj.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webforj.data.HasEntityKey;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * Genre entity representing a book genre.
 * 
 */
@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Genre implements Comparable<Genre>, HasEntityKey {

  /** Unique identifier for the genre. */
  @Id
  private String id;

  /** The name of the genre. */
  @EqualsAndHashCode.Include
  @Column
  @NotBlank(message = "Genre name is required")
  private String name;

  /** A description of the genre. */
  @Column(length = 1000)
  private String description;

  /** Additional notes about the genre. */
  @Column(length = 2000)
  private String notes;

  /** Detailed information about the genre. */
  @Column(length = 2000)
  private String details;

  /** The color associated with the genre for UI display. */
  @Column
  @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Color must be a valid hex color (#RRGGBB)")
  private String color;

  /**
   * Compares this genre to another based on name.
   * 
   * @param other the other genre to compare to
   * @return a negative integer, zero, or a positive integer as this genre's name
   *         is less than, equal to, or greater than the specified genre's name
   */
  @Override
  public int compareTo(Genre other) {
    if (other == null) {
      return 1;
    }
    if (this.name == null) {
      return other.getName() == null ? 0 : -1;
    }
    return other.getName() == null ? 1 : this.name.compareTo(other.getName());
  }

  @JsonIgnore
  @Override
  public Object getEntityKey() {
    return id;
  }
}
