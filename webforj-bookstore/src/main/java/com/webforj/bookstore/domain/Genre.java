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

    @Id
    private String id;

    @EqualsAndHashCode.Include
    @Column
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 2000)
    private String notes;

    @Column(length = 2000)
    private String details;

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
