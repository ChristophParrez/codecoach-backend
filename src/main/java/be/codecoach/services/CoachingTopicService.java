package be.codecoach.services;

import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.domain.CoachingTopic;
import be.codecoach.domain.Topic;
import be.codecoach.exceptions.CoachingTopicException;
import be.codecoach.exceptions.TopicException;
import be.codecoach.repositories.CoachingTopicRepository;
import be.codecoach.services.mappers.CoachingTopicMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoachingTopicService {

    private final CoachingTopicRepository coachingTopicRepository;
    private final TopicService topicService;
    private final CoachingTopicMapper coachingTopicMapper;

    public CoachingTopicService(CoachingTopicRepository coachingTopicRepository, TopicService topicService, CoachingTopicMapper coachingTopicMapper) {
        this.coachingTopicRepository = coachingTopicRepository;
        this.topicService = topicService;
        this.coachingTopicMapper = coachingTopicMapper;
    }

    public void assertInputIsValid(List<CoachingTopicDto> coachingTopicDtos) {
        assertNotTooManyTopicsAreProvided(coachingTopicDtos);
        assertTopicsAreUnique(coachingTopicDtos);
    }

    public void updateTopics(List<CoachingTopicDto> coachingTopicDtos, List<CoachingTopic> coachingTopics) {
        deleteCoachingTopics(coachingTopics);
        addCoachingTopics(coachingTopicDtos, coachingTopics);
    }

    private Optional<CoachingTopic> findById(String coachingTopicId) {
        return coachingTopicRepository.findById(coachingTopicId);
    }

    private void delete(CoachingTopic coachingTopic) {
        coachingTopicRepository.delete(coachingTopic);
    }

    private void assertNotTooManyTopicsAreProvided(List<CoachingTopicDto> coachingTopicDtos) {
        if(coachingTopicDtos.size() > 2) {
            throw new TopicException("Max 2 topics allowed");
        }
    }

    private void assertTopicsAreUnique(List<CoachingTopicDto> coachingTopicDtos) {
        if(coachingTopicDtos.size() == 2) {
            if(coachingTopicDtos.get(0).getTopic().getName().equals(coachingTopicDtos.get(1).getTopic().getName())) {
                throw new TopicException("A coach cannot teach the same topic twice");
            }
        }
    }

    private void deleteCoachingTopics(List<CoachingTopic> coachingTopics) {

        for(CoachingTopic coachingTopic : coachingTopics) {
            deleteCoachingTopic(coachingTopic.getCoachingTopicId());
        }
    }

    private void deleteCoachingTopic(String coachingTopicId) {
        CoachingTopic coachingTopic = findById(coachingTopicId)
                .orElseThrow(() -> new CoachingTopicException("Coaching topic not found"));
        delete(coachingTopic);
    }

    private void addCoachingTopics(List<CoachingTopicDto> coachingTopicDtos, List<CoachingTopic> coachingTopics) {
        for(CoachingTopicDto coachingTopicDto : coachingTopicDtos) {
            Topic topic = topicService.findById(coachingTopicDto.getTopic().getName())
                    .orElseThrow(() -> new TopicException("Topic " + coachingTopicDto.getTopic().getName() + " was not found"));

            CoachingTopic coachingTopic = coachingTopicMapper.toEntity(coachingTopicDto);
            coachingTopic.setExperience(coachingTopicDto.getExperience());
            coachingTopic.setTopic(topic);
            coachingTopics.add(coachingTopic);
        }
    }
}
