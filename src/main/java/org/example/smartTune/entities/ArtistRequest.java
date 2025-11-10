package org.example.smartTune.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "artist_requests")
public class ArtistRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String numTel;
    private String bio;
    private long nbrAboonnés;
    private String pdfPath;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Integer age;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtistStatus status = ArtistStatus.PENDING;

    private LocalDateTime submittedAt = LocalDateTime.now();
}