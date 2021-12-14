package be.codecoach.services;

import be.codecoach.domain.CoachingTopic;
import be.codecoach.domain.Topic;
import be.codecoach.repositories.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }


    public Optional<Topic> findById(String topicId) {
        return topicRepository.findById(topicId);
    }
}
