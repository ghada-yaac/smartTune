package org.example.ecommerce.controlleur;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entities.Artiste;
import org.example.ecommerce.entities.User;
import org.example.ecommerce.metier.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/standard")
    public User registerStandard(@RequestBody User user) {
        return authService.registerStandard(user);
    }

    @PostMapping("/register/artist")
    public Artiste registerArtiste(@RequestBody Artiste artiste) {
        return authService.registerArtiste(artiste);
    }

    @PutMapping("/admin/validate-artist/{id}")
    public Artiste validateArtiste(@PathVariable Long id) {
        return authService.validateArtiste(id);
    }
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> loginData, HttpSession session) {

        String email = loginData.get("email");
        String password = loginData.get("password");

        User user = authService.login(email, password);

        // Sauvegarde dans la session
        session.setAttribute("connectedUser", user);

        return "Connexion réussie ✅ Bienvenue " + user.getNom() + " (" + user.getRole() + ")";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Déconnexion réussie 👋";
    }

    @GetMapping("/me")
    public Object currentUser(HttpSession session) {
        return session.getAttribute("connectedUser");
    }
}
