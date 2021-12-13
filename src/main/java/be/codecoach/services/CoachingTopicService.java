package be.codecoach.services;

import be.codecoach.repositories.CoachingTopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoachingTopicService {

    private final CoachingTopicRepository coachingTopicRepository;

    public CoachingTopicService(CoachingTopicRepository coachingTopicRepository) {
        this.coachingTopicRepository = coachingTopicRepository;
    }
}
