package edu.uclm.esi.payments.http;

import edu.uclm.esi.payments.model.UserCredits;
import edu.uclm.esi.payments.service.UserCreditsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/credits")
public class UserCreditsController {

    @Autowired
    private UserCreditsService service;

    @GetMapping("/{userId}")
    public ResponseEntity<UserCredits> getUserCredits(@PathVariable String userId) {
        Optional<UserCredits> credits = service.getUserCredits(userId);
        return credits.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<UserCredits> addCredits(@PathVariable String userId, @RequestParam int amount) {
        return ResponseEntity.ok(service.addCredits(userId, amount));
    }

    @PostMapping("/{userId}/deduct")
    public ResponseEntity<String> deductCredits(@PathVariable String userId, @RequestParam int amount) {
        boolean success = service.deductCredits(userId, amount);
        return success ? ResponseEntity.ok("Credits deducted") : ResponseEntity.badRequest().body("Insufficient credits");
    }
}