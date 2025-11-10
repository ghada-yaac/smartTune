package org.example.smartTune.dao;

import org.example.smartTune.entities.ArtistRequest;
import org.example.smartTune.entities.ArtistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtistRequestRepository extends JpaRepository<ArtistRequest, Long> {
    List<ArtistRequest> findByStatus(ArtistStatus status);
    boolean existsByEmail(String email);
}