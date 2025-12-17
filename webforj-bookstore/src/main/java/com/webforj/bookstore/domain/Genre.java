package com.webforj.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webforj.data.HasEntityKey;
import jakarta.persistence.*;
import lombok.*;

/**
 * Genre entity representing a book genre.
 * 
 * @author webforJ Bookstore
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
        return this.name.compareTo(other.getName());
    }

    @JsonIgnore
    @Override
    public Object getEntityKey() {
        return id;
    }
}
