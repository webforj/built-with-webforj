package com.webforj.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webforj.data.HasEntityKey;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Book entity representing a book in the bookstore.
 * 
 * @author webforJ Bookstore
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Book implements Comparable<Book>, HasEntityKey {

    /** Unique identifier for the book. */
    @Id
    private String id;

    /** The title of the book. */
    @Column
    private String title;

    /** The author of the book. */
    @Column(length = 1000)
    private String author;

    /** The language the book is written in. */
    @Column(length = 100)
    private String language;

    /** The list of genres associated with the book. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre")
    @Builder.Default
    private List<String> genres = new ArrayList<>();

    /** The publisher of the book. */
    private String publisher;

    /** The ISBN of the book. */
    @Column
    private String isbn;

    /** The publication date of the book. */
    @Column(name = "publication_date")
    @com.fasterxml.jackson.annotation.JsonProperty("publication_date")
    private String publicationDate;

    /** Additional notes about the book. */
    @Column(length = 2000)
    private String notes;

    /**
     * Compares this book to another based on title.
     * 
     * @param other the other book to compare to
     * @return a negative integer, zero, or a positive integer as this book's title
     *         is less than, equal to, or greater than the specified book's title
     */
    @Override
    public int compareTo(Book other) {
        if (other == null) {
            return 1;
        }
        return this.title.compareTo(other.getTitle());
    }

    @JsonIgnore
    @Override
    public Object getEntityKey() {
        return id;
    }
}
