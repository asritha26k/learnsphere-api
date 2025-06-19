package com.learnsphere.flashcards.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.HashSet; // Changed from ArrayList
import java.util.Set;    // Changed from List

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties({"children", "flashcards", "parent", "topics"}) // Keep this for JSON serialization
    private Topic parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties({"parent", "flashcards", "children"}) // Keep this for JSON serialization
    private Set<Topic> children = new HashSet<>(); // Changed from List to Set, and ArrayList to HashSet

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    @JsonIgnoreProperties({"topics"}) // Keep this for JSON serialization
    private Subject subject;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties({"topic"}) // Keep this for JSON serialization
    private Set<Flashcard> flashcards = new HashSet<>(); // Changed from List to Set, and ArrayList to HashSet


    // Constructors
    public Topic() {}

    public Topic(Long id, String name, Topic parent, Subject subject) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.subject = subject;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Topic getParent() { return parent; }
    public void setParent(Topic parent) { this.parent = parent; }

    // Changed return type and parameter type to Set
    public Set<Topic> getChildren() { return children; }
    public void setChildren(Set<Topic> children) { this.children = children; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    // Changed return type and parameter type to Set
    public Set<Flashcard> getFlashcards() { return flashcards; }
    public void setFlashcards(Set<Flashcard> flashcards) { this.flashcards = flashcards; }

    public void addChild(Topic child) {
        children.add(child);
        child.setParent(this);
    }

    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
        flashcard.setTopic(this);
    }
}