package com.learnsphere.flashcards.repository;

import com.learnsphere.flashcards.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Important for findById

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    // Keep findByParentIdIn for DataLoader if you decide to use it later
    List<Topic> findByParentIdIn(java.util.Set<Long> parentIds);

    // New query method to fetch a Topic along with its parent, children, and flashcards
    @Query("SELECT t FROM Topic t LEFT JOIN FETCH t.parent p LEFT JOIN FETCH t.children c LEFT JOIN FETCH t.flashcards f WHERE t.id = :id")
    Optional<Topic> findByIdWithAssociations(Long id);
}