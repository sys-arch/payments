package edu.uclm.esi.payments.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.payments.model.UserCredits;

public interface UserCreditsDAO extends JpaRepository<UserCredits, Long> {
    Optional<UserCredits> findByUserId(String userId);
    
}
