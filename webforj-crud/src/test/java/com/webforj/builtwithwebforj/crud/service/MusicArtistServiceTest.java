package com.webforj.builtwithwebforj.crud.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webforj.builtwithwebforj.crud.entity.MusicArtist;
import com.webforj.builtwithwebforj.crud.repository.MusicArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MusicArtistServiceTest {

  @Autowired
  private MusicArtistService service;

  @Autowired
  private MusicArtistRepository artists;

  @BeforeEach
  void emptyTheDatabase() {
    artists.deleteAll();
  }

  @Test
  void shouldCreateAnArtistAsActiveWhenTheStateIsMissing() {
    MusicArtist artist = artist("Queen");
    artist.setIsActive(null);

    MusicArtist saved = service.createArtist(artist);

    assertNotNull(saved.getId());
    assertTrue(saved.getIsActive());
    assertEquals(1, service.getTotalArtistsCount());
  }

  @Test
  void shouldUpdateAnExistingArtist() {
    MusicArtist saved = service.createArtist(artist("Queen"));
    saved.setGenre("Glam Rock");

    service.updateArtist(saved);

    assertEquals("Glam Rock", artists.findById(saved.getId()).orElseThrow().getGenre());
  }

  @Test
  void shouldDeleteAnArtist() {
    MusicArtist saved = service.createArtist(artist("Queen"));

    service.deleteArtist(saved.getId());

    assertEquals(0, service.getTotalArtistsCount());
  }

  @Test
  void shouldRefuseToDeleteAnArtistThatIsGone() {
    assertThrows(IllegalArgumentException.class, () -> service.deleteArtist(-1L));
  }

  private static MusicArtist artist(String name) {
    MusicArtist artist = new MusicArtist(name);
    artist.setGenre("Rock");
    artist.setCountry("United Kingdom");
    artist.setYearFormed(1970);
    return artist;
  }
}
