package edu.uclm.esi.payments.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.payments.dao.UserCreditsDAO;
import edu.uclm.esi.payments.model.UserCredits;

@Service
public class UserCreditsService {

    @Autowired
    private UserCreditsDAO repository;

    public Optional<UserCredits> getUserCredits(String userId) {
        return repository.findByUserId(userId);
    }

    @Transactional
    public UserCredits addCredits(String userId, int amount) {
        UserCredits userCredits = repository.findByUserId(userId)
                .orElse(new UserCredits(userId, 0));
        userCredits.setCredits(userCredits.getCredits() + amount);
        return repository.save(userCredits);
    }

    @Transactional
    public boolean deductCredits(String userId, int amount) {
        Optional<UserCredits> userOpt = repository.findByUserId(userId);
        if (userOpt.isPresent() && userOpt.get().getCredits() >= amount) {
            UserCredits userCredits = userOpt.get();
            userCredits.setCredits(userCredits.getCredits() - amount);
            repository.save(userCredits);
            return true;
        }
        return false;
    }
}

