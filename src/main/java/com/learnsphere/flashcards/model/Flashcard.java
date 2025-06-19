package com.learnsphere.flashcards.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Import this

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType; // Important for LAZY fetching

@Entity
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String answer;
    private String hint;
    private String difficulty;

    @ManyToOne(fetch = FetchType.LAZY) // ManyToOne to Topic, make it LAZY
    @JoinColumn(name = "topic_id")
    @JsonIgnoreProperties({"flashcards", "children", "parent", "subject"}) // Ignore self-referencing properties of Topic
    private Topic topic;

    // Constructors
    public Flashcard() {}

    public Flashcard(Long id, String question, String answer, String hint, String difficulty, Topic topic) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.hint = hint;
        this.difficulty = difficulty;
        this.topic = topic;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getHint() { return hint; }
    public void setHint(String hint) { this.hint = hint; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }
}