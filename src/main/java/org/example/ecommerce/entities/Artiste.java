package org.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("ARTIST")
public class Artiste extends User {

    private String bio;

    private Integer nbrAbonnee = 0;

}
