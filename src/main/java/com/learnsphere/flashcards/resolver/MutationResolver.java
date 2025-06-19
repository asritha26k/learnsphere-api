package com.learnsphere.flashcards.resolver;

import com.learnsphere.flashcards.model.Flashcard;
import com.learnsphere.flashcards.model.Topic;
import com.learnsphere.flashcards.repository.FlashcardRepository;
import com.learnsphere.flashcards.repository.TopicRepository;
import org.springframework.stereotype.Controller; // CHANGE THIS FROM @Component if it was there
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping; // Import this
import org.springframework.transaction.annotation.Transactional;

@Controller // <--- IMPORTANT CHANGE
public class MutationResolver {

    private final FlashcardRepository flashcardRepo;
    private final TopicRepository topicRepo; // Assuming you need topicRepo here to link flashcard to topic

    public MutationResolver(FlashcardRepository flashcardRepo, TopicRepository topicRepo) {
        this.flashcardRepo = flashcardRepo;
        this.topicRepo = topicRepo;
    }

    @MutationMapping // <--- IMPORTANT: This links to "createFlashcard" field in Mutation type
    @Transactional
    public Flashcard createFlashcard(
            @Argument Long topicId,
            @Argument String question,
            @Argument String answer,
            @Argument String hint,
            @Argument String difficulty
    ) {
        Topic topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found with id: " + topicId));

        Flashcard newFlashcard = new Flashcard(null, question, answer, hint, difficulty, topic);
        return flashcardRepo.save(newFlashcard);
    }
}