package com.learnsphere.flashcards.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.HashSet; // Changed from ArrayList
import java.util.Set;    // Changed from List

@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties({"subject", "parent", "children", "flashcards"}) // Keep this for JSON serialization
    private Set<Topic> topics = new HashSet<>(); // Changed from List to Set, and ArrayList to HashSet

    // Constructors
    public Subject() {}

    public Subject(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Changed return type and parameter type to Set
    public Set<Topic> getTopics() { return topics; }
    public void setTopics(Set<Topic> topics) { this.topics = topics; }

    public void addTopic(Topic topic) {
        topics.add(topic);
        topic.setSubject(this);
    }
}