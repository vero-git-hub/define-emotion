package com.example.defineemotion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.defineemotion.model.Emotion;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
