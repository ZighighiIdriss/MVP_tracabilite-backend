package com.projet.tracabilite.controller;

import com.projet.tracabilite.dto.LoginRequest;
import com.projet.tracabilite.entity.User;
import com.projet.tracabilite.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur pour l'authentification et le mode démo.
 * Écran : Login / S'authentifier
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    /**
     * Authentification de l'utilisateur.
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Email ou mot de passe incorrect");
            return ResponseEntity.status(401).body(error);
        }

        User user = userOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", user.getId());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

    /**
     * Accès en mode démo (sans authentification).
     * POST /api/auth/demo
     */
    @PostMapping("/demo")
    public ResponseEntity<Map<String, Object>> demoMode() {
        // Récupérer l'utilisateur démo (id=1, inséré via data.sql)
        User demoUser = userRepository.findByEmail("demo@tracabilite.ma")
                .orElseThrow(() -> new RuntimeException("Utilisateur démo introuvable"));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", demoUser.getId());
        response.put("email", demoUser.getEmail());
        response.put("mode", "demo");

        return ResponseEntity.ok(response);
    }
}
