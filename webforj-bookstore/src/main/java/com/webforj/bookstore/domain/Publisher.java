package com.webforj.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webforj.data.HasEntityKey;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Publisher entity representing a book publisher.
 * 
 */
@Entity
@Table(name = "publishers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Publisher implements Comparable<Publisher>, HasEntityKey {

    /** Unique identifier for the publisher. */
    @Id
    private String id;

    /** The name of the publisher. */
    @EqualsAndHashCode.Include
    @Column
    private String name;

    /** A description of the publisher. */
    @Column(length = 1000)
    private String description;

    /** The founding year or date of the publisher. */
    @Column
    private String founded;

    /** A list of notable works published by this publisher. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "publisher_notable_works", joinColumns = @JoinColumn(name = "publisher_id"))
    @Column(name = "notable_work")
    @Builder.Default
    private List<String> notableWorks = new ArrayList<>();

    /** A list of specialties or genres this publisher is known for. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "publisher_specialties", joinColumns = @JoinColumn(name = "publisher_id"))
    @Column(name = "specialty")
    @Builder.Default
    private List<String> specialties = new ArrayList<>();

    /**
     * Compares this publisher to another based on name.
     * 
     * @param other the other publisher to compare to
     * @return a negative integer, zero, or a positive integer as this publisher's
     *         name
     *         is less than, equal to, or greater than the specified publisher's
     *         name
     */
    @Override
    public int compareTo(Publisher other) {
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
