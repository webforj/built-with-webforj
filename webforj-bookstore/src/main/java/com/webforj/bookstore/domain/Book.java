package com.webforj.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webforj.data.HasEntityKey;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Book entity representing a book in the bookstore.
 * 
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
    @NotBlank(message = "Title is required")
    private String title;

    /** The author of the book. */
    @Column(length = 1000)
    @NotBlank(message = "Author is required")
    private String author;

    /** The language the book is written in. */
    @Column(length = 100)
    private String language;

    /** The list of genres associated with the book. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre")
    @Builder.Default
    @NotEmpty(message = "At least one genre is required")
    private List<String> genres = new ArrayList<>();

    /** The publisher of the book. */
    @NotBlank(message = "Publisher is required")
    private String publisher;

    /** The ISBN of the book. */
    @Column
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "\\d{13}", message = "ISBN must be exactly 13 digits")
    private String isbn;

    /** The publication date of the book. */
    @Column(name = "publication_date")
    @JsonProperty("publication_date")
    @NotNull(message = "Publication date is required")
    private LocalDate publicationDate;

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
