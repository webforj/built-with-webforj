package com.webforj.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webforj.data.HasEntityKey;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Author entity representing a book author.
 * 
 */
@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Author implements HasEntityKey {

    /** Unique identifier for the author. */
    @Id
    private String id;

    /** The name of the author. */
    @Column
    private String name;

    /** The pen name of the author, if any. */
    private String penName;

    /** The full name of the author. */
    private String fullName;

    /** The birth date of the author. */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /** The death date of the author, if deceased. */
    @Column(name = "date_of_death")
    private LocalDate dateOfDeath;

    /** The nationality of the author. */
    private String nationality;

    /** A description or biography of the author. */
    @Column(length = 2000)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "author_languages", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "language")
    @Builder.Default
    private List<String> languages = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "author_impacts", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "impact")
    @Builder.Default
    private List<String> impacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "author_influences", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "influence")
    @Builder.Default
    private List<String> influences = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "author_professions", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "profession")
    @Builder.Default
    private List<String> professions = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "author_genres", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "genre")
    @Builder.Default
    private List<String> genres = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "author_publishers", joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "publisher")
    @Builder.Default
    private List<String> publishers = new ArrayList<>();

    @JsonIgnore
    @Override
    public Object getEntityKey() {
        return id;
    }
}
