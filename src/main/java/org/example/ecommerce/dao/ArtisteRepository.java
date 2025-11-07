package org.example.ecommerce.dao;

import org.example.ecommerce.entities.Artiste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtisteRepository extends JpaRepository<Artiste, Long> {
}
