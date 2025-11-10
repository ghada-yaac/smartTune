package org.example.smartTune.metier;

import org.example.smartTune.dao.ArtistRequestRepository;
import org.example.smartTune.dao.UserRepository;
import org.example.smartTune.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private ArtistRequestRepository artistRequestRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private final String UPLOAD_DIR = "uploads/artists/";

    // INSCRIPTION UTILISATEUR
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail()) ||
                artistRequestRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setActive(true);

        return userRepository.save(user);
    }

    // INSCRIPTION ARTISTE + PDF
    public ArtistRequest registerArtist(ArtistRequest request, MultipartFile pdf) throws IOException {
        if (userRepository.existsByEmail(request.getEmail()) ||
                artistRequestRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Sauvegarde PDF
        String fileName = UUID.randomUUID() + "_" + pdf.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, pdf.getBytes());
        request.setPdfPath(path.toString());

        // Hash mot de passe
        request.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
        request.setStatus(ArtistStatus.PENDING);

        return artistRequestRepository.save(request);
    }

    // CONNEXION
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email incorrect"));

        if (!user.isActive()) {
            throw new RuntimeException("Compte désactivé");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return user;
    }

    // APPROUVER ARTISTE → CRÉE UN USER
    public User approveArtist(Long requestId) {
        ArtistRequest request = artistRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        if (request.getStatus() != ArtistStatus.PENDING) {
            throw new RuntimeException("Demande déjà traitée");
        }

        User artiste = new User();
        artiste.setNom(request.getNom());
        artiste.setPrenom(request.getPrenom());
        artiste.setEmail(request.getEmail());
        artiste.setNumTel(request.getNumTel());
        artiste.setGenre(request.getGenre());
        artiste.setAge(request.getAge());
        artiste.setPassword(request.getPasswordHash());
        artiste.setRole(Role.ARTIST);
        artiste.setActive(true);

        User saved = userRepository.save(artiste);

        request.setStatus(ArtistStatus.APPROVED);
        artistRequestRepository.save(request);

        return saved;
    }

    // REJETER ARTISTE
    public void rejectArtist(Long requestId) {
        ArtistRequest request = artistRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        request.setStatus(ArtistStatus.REJECTED);
        artistRequestRepository.save(request);
    }
}