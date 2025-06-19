package com.learnsphere.flashcards.repository;

import com.learnsphere.flashcards.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import this

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    // Keep the old method if other parts of your app use it, or rename this one
    // List<Flashcard> findByTopicIdAndDifficulty(Long topicId, String difficulty);

    // New method with JOIN FETCH to eagerly load the associated Topic
    @Query("SELECT fc FROM Flashcard fc JOIN FETCH fc.topic t WHERE t.id = :topicId AND fc.difficulty = :difficulty")
    List<Flashcard> findByTopicIdAndDifficultyWithTopic(Long topicId, String difficulty);
}