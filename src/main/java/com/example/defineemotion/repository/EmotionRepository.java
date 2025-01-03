package com.example.defineemotion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.defineemotion.model.Emotion;
import com.example.defineemotion.model.User;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    
    List<Emotion> findByUser(User user);
}
