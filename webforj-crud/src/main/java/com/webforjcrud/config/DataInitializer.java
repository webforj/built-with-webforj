package com.webforjcrud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.webforjcrud.entity.MusicArtist;
import com.webforjcrud.service.MusicArtistService;

/**
 * Data initialization class that adds sample music artists to the database
 * when the application starts (only if the database is empty).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MusicArtistService artistService;

    @Override
    public void run(String... args) throws Exception {
        // Only add sample data if the database is empty
        long currentCount = artistService.getTotalArtistsCount();
        
        if (currentCount == 0) {
            initializeSampleData();
        } else {
        }
    }

    private void initializeSampleData() {
        // Create sample music artists
        createArtist("The Beatles", "Rock", "United Kingdom", 1960, true, 
                "English rock band formed in Liverpool in 1960. Widely regarded as the most influential band of all time.");
        
        createArtist("Elvis Presley", "Rock", "United States", 1954, false, 
                "Known as the 'King of Rock and Roll', one of the most significant cultural figures of the 20th century.");
        
        createArtist("Michael Jackson", "Pop", "United States", 1964, false, 
                "The 'King of Pop' who transformed music, dance, and fashion. One of the best-selling music artists of all time.");
        
        createArtist("Madonna", "Pop", "United States", 1982, true, 
                "The 'Queen of Pop' known for pushing boundaries and reinventing both her music and image.");
        
        createArtist("Queen", "Rock", "United Kingdom", 1970, false, 
                "British rock band formed in London, known for their theatrical performances and anthemic songs.");
        
        createArtist("Bob Dylan", "Folk", "United States", 1961, true, 
                "Influential American singer-songwriter, Nobel Prize winner, and voice of a generation.");
        
        createArtist("Led Zeppelin", "Rock", "United Kingdom", 1968, false, 
                "English rock band formed in London, considered one of the most influential rock bands in history.");
        
        createArtist("Beyoncé", "Pop", "United States", 1997, true, 
                "Started with Destiny's Child, became a global superstar as a solo artist and cultural icon.");
        
        createArtist("The Rolling Stones", "Rock", "United Kingdom", 1962, true, 
                "English rock band formed in London, known as 'The World's Greatest Rock and Roll Band'.");
        
        createArtist("Adele", "Pop", "United Kingdom", 2006, true, 
                "British singer-songwriter known for her powerful vocals and emotional ballads.");
        
        createArtist("Prince", "Pop", "United States", 1976, false, 
                "Innovative musician, songwriter, and performer known for his eclectic style and prolific output.");
        
        createArtist("Radiohead", "Alternative", "United Kingdom", 1985, true, 
                "English rock band known for their experimental approach and influence on alternative music.");
        
        createArtist("Taylor Swift", "Pop", "United States", 2004, true, 
                "American singer-songwriter known for her narrative songwriting and genre versatility.");
        
        createArtist("Nirvana", "Grunge", "United States", 1987, false, 
                "Seattle-based rock band that brought grunge music to mainstream audiences in the early 1990s.");
        
        createArtist("Johnny Cash", "Country", "United States", 1954, false, 
                "The 'Man in Black' known for his deep voice and rebellious image in country music.");
    }

    private void createArtist(String name, String genre, String country, Integer yearFormed, 
                             Boolean isActive, String biography) {
            MusicArtist artist = new MusicArtist(name);
            artist.setGenre(genre);
            artist.setCountry(country);
            artist.setYearFormed(Double.valueOf(yearFormed));
            artist.setIsActive(isActive);
            artist.setBiography(biography);
            artistService.createArtist(artist);
    }
}