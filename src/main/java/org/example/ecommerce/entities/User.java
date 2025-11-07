package org.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;

    private String numTel;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Integer age;

    private String password;

    private LocalDate dateInscription = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Role role; // STANDARD, ARTIST, ADMIN

    private boolean isActive; // TRUE pour standard / admin, FALSE pour artiste en attente
}
