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
 * @author webforJ Bookstore
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

    @Id
    private String id;

    @EqualsAndHashCode.Include
    @Column
    private String name;

    @Column(length = 1000)
    private String description;

    @Column
    private String founded;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "publisher_notable_works", joinColumns = @JoinColumn(name = "publisher_id"))
    @Column(name = "notable_work")
    @Builder.Default
    private List<String> notableWorks = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "publisher_specialties", joinColumns = @JoinColumn(name = "publisher_id"))
    @Column(name = "specialty")
    @Builder.Default
    private List<String> specialties = new ArrayList<>();

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
