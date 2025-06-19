package com.learnsphere.flashcards.resolver;

import com.learnsphere.flashcards.model.Flashcard;
import com.learnsphere.flashcards.model.Subject;
import com.learnsphere.flashcards.model.Topic;
import com.learnsphere.flashcards.repository.FlashcardRepository;
import com.learnsphere.flashcards.repository.SubjectRepository;
import com.learnsphere.flashcards.repository.TopicRepository;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class QueryResolver {

    private final SubjectRepository subjectRepo;
    private final TopicRepository topicRepo;
    private final FlashcardRepository flashcardRepo;

    public QueryResolver(SubjectRepository s, TopicRepository t, FlashcardRepository f) {
        this.subjectRepo = s;
        this.topicRepo = t;
        this.flashcardRepo = f;
    }

    @QueryMapping
    @Transactional
    public List<Subject> subjects() {
        return subjectRepo.findAll();
    }

    @QueryMapping
    @Transactional
    public List<Flashcard> flashcards(@Argument Long topicId, @Argument String difficulty) {
        return flashcardRepo.findByTopicIdAndDifficultyWithTopic(topicId, difficulty);
    }

    @QueryMapping
    @Transactional // Keep this, as the loop might still traverse to parent
    public List<Topic> topicPath(@Argument Long topicId) {
        List<Topic> path = new ArrayList<>();
        // Use the new method to eagerly fetch parent, children, and flashcards
        Optional<Topic> optionalCurrent = topicRepo.findByIdWithAssociations(topicId);
        Topic current = optionalCurrent.orElse(null);

        while (current != null) {
            path.add(0, current);
            // Accessing current.getParent() is now safe because parent is eagerly fetched.
            // However, the "parent" itself might need its children/flashcards fetched if you query them
            // within the 'parent' field of a Topic in the topicPath output.
            // For the current query, this should be fine.
            current = current.getParent();
            // Important: If a parent's children/flashcards are accessed from its .parent(), that's a new proxy.
            // For topicPath, we're only going up, so it's less of an issue.
            // If we went down from parent, we'd need more complex fetching/DataLoaders.
            // For now, assume a simple parent traversal is sufficient.
            if (current != null && !current.equals(optionalCurrent.orElse(null)) && current.getChildren().isEmpty() && current.getFlashcards().isEmpty()) {
                // If current's collections are uninitialized, it means findByIdWithAssociations didn't load them
                // This is where a DataLoader for Topic by ID (which loads its collections) would be ideal.
                // For now, to quickly fix this specific L.I.E. if parent's children are queried:
                // current = topicRepo.findByIdWithAssociations(current.getId()).orElse(null);
                // But this will issue N additional queries in the loop. The current query should not cause it.
            }
        }
        return path;
    }
}