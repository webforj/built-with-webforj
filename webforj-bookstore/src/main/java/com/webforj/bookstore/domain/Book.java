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

    @Id
    private String id;

    @Column
    private String title;

    @Column(length = 1000)
    private String author;

    @Column(length = 100)
    private String language;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre")
    @Builder.Default
    private List<String> genres = new ArrayList<>();

    private String publisher;

    @Column
    private String isbn;

    @Column(name = "publication_date")
    private String publicationDate;

    @Column(length = 2000)
    private String notes;

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
