package org.example.smartTune.controlleur;

import org.example.smartTune.entities.ArtistRequest;
import org.example.smartTune.entities.Genre;
import org.example.smartTune.entities.User;
import org.example.smartTune.metier.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired private AuthService authService;

    // INSCRIPTION USER
    @PostMapping("/register/user")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(authService.registerUser(user));
    }

    // INSCRIPTION ARTISTE + PDF
    @PostMapping("/register/artist")
    public ResponseEntity<ArtistRequest> registerArtist(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String email,
            @RequestParam(required = false) String numTel,
            @RequestParam(required = false) String bio,
            @RequestParam long nbrAboonnés,
            @RequestParam String password,
            @RequestParam Integer age,
            @RequestParam(required = false) String genre,
            @RequestParam("pdf") MultipartFile pdf) throws IOException {

        ArtistRequest request = new ArtistRequest();
        request.setNom(nom);
        request.setPrenom(prenom);
        request.setEmail(email);
        request.setNumTel(numTel);
        request.setBio(bio);
        request.setNbrAboonnés(nbrAboonnés);
        request.setPasswordHash(password);
        request.setAge(age);
        request.setGenre(genre != null ? Genre.valueOf(genre) : null);

        return ResponseEntity.ok(authService.registerArtist(request, pdf));
    }

    // CONNEXION
    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestParam String email,
            @RequestParam String password) {
        return ResponseEntity.ok(authService.login(email, password));
    }

    // ADMIN : APPROUVER
    @PostMapping("/admin/approve/{id}")
    public ResponseEntity<User> approveArtist(@PathVariable Long id) {
        return ResponseEntity.ok(authService.approveArtist(id));
    }

    // ADMIN : REJETER
    @PostMapping("/admin/reject/{id}")
    public ResponseEntity<String> rejectArtist(@PathVariable Long id) {
        authService.rejectArtist(id);
        return ResponseEntity.ok("Artiste rejeté");
    }
}