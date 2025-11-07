package org.example.ecommerce.metier;

import org.example.ecommerce.entities.Artiste;
import org.example.ecommerce.entities.Role;
import org.example.ecommerce.entities.User;
import org.example.ecommerce.dao.ArtisteRepository;
import org.example.ecommerce.dao.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ArtisteRepository artisteRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, ArtisteRepository artisteRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.artisteRepository = artisteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Inscription Utilisateur Standard
    public User registerStandard(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.STANDARD);
        user.setActive(true);
        return userRepository.save(user);
    }

    // Inscription Artiste (en attente de validation admin)
    public Artiste registerArtiste(Artiste artiste) {
        artiste.setPassword(passwordEncoder.encode(artiste.getPassword()));
        artiste.setRole(Role.ARTIST);
        artiste.setActive(false);
        return artisteRepository.save(artiste);
    }

    // Validation Artiste par Admin
    public Artiste validateArtiste(Long id) {
        Artiste artiste = artisteRepository.findById(id).orElseThrow();
        artiste.setActive(true);
        return artisteRepository.save(artiste);
    }
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email incorrect ❌"));

        if (!user.isActive()) {
            throw new RuntimeException("Compte non activé (Artiste en attente de validation admin) ⏳");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect ❌");
        }

        return user; // success
    }
}
