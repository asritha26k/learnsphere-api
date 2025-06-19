package com.learnsphere.flashcards.repository;

import com.learnsphere.flashcards.model.Subject;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Keep List here for the return type of findAll()

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // Use EntityGraph to eagerly fetch the entire required graph
    @EntityGraph(attributePaths = {"topics", "topics.children", "topics.flashcards"})
    List<Subject> findAll(); // This method remains the same
}