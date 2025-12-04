package com.webforj.builtwithwebforj.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webforj.builtwithwebforj.dashboard.models.Cryptocurrency;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {
    // JpaRepository provides all basic CRUD operations automatically:
    // save(), findAll(), findById(), deleteById(), etc.
}