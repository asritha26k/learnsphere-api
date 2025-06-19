package com.learnsphere.flashcards;

import com.learnsphere.flashcards.model.Flashcard;
import com.learnsphere.flashcards.model.Subject;
import com.learnsphere.flashcards.model.Topic;
import com.learnsphere.flashcards.repository.FlashcardRepository;
import com.learnsphere.flashcards.repository.SubjectRepository;
import com.learnsphere.flashcards.repository.TopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet; // Changed from ArrayList

@SpringBootApplication
public class FlashcardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashcardsApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(SubjectRepository sRepo, TopicRepository tRepo, FlashcardRepository fRepo) {
		return args -> {
			// Create and save Subject
			Subject dsa = new Subject(null, "DSA");
			// Initialize collections with HashSet if your constructor doesn't do it
			// This ensures the collections are not null if you haven't assigned them
			// dsa.setTopics(new HashSet<>()); // If Subject constructor doesn't initialize it

			dsa = sRepo.save(dsa);

			// Create Topics using the updated constructor
			Topic trees = new Topic(null, "Trees", null, dsa);
			// Initialize collections with HashSet if your constructor doesn't do it
			// trees.setChildren(new HashSet<>());
			// trees.setFlashcards(new HashSet<>());

			Topic bst = new Topic(null, "BST", trees, dsa);
			// bst.setChildren(new HashSet<>());
			// bst.setFlashcards(new HashSet<>());


			// Set parent-child relationship (bidirectional)
			trees.addChild(bst); // This helper will correctly use the Set

			// Save topics (parent first, then child to ensure parent ID exists)
			trees = tRepo.save(trees);
			bst = tRepo.save(bst); // Ensure BST is saved after its parent (trees) has an ID

			// Create and save Flashcard
			Flashcard fc = new Flashcard(null, "What is BST?", "A binary search tree", "Think of sorted binary tree", "Easy", bst);
			fc = fRepo.save(fc);

			// Add flashcard to topic's collection and save the topic to persist the relationship from the Topic side
			bst.addFlashcard(fc);
			tRepo.save(bst);

			System.out.println("Sample data created.");
		};
	}
}