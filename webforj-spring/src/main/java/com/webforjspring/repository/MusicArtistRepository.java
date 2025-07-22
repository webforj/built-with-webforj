package com.webforjspring.repository;

import com.webforjspring.entity.MusicArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for MusicArtist entities.
 * Provides standard CRUD operations and specification-based querying.
 */
@Repository
public interface MusicArtistRepository extends JpaRepository<MusicArtist, Long>, JpaSpecificationExecutor<MusicArtist> {
}