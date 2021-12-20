package be.codecoach.services;

import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.domain.CoachingTopic;
import be.codecoach.domain.Topic;
import be.codecoach.exceptions.CoachingTopicException;
import be.codecoach.exceptions.InvalidInputException;
import be.codecoach.exceptions.TopicException;
import be.codecoach.repositories.CoachingTopicRepository;
import be.codecoach.services.mappers.CoachingTopicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoachingTopicService {

    private final CoachingTopicRepository coachingTopicRepository;
    private final TopicService topicService;
    private final CoachingTopicMapper coachingTopicMapper;
    private static final Logger logger = LoggerFactory.getLogger(CoachingTopicService.class);

    public CoachingTopicService(CoachingTopicRepository coachingTopicRepository, TopicService topicService, CoachingTopicMapper coachingTopicMapper) {
        this.coachingTopicRepository = coachingTopicRepository;
        this.topicService = topicService;
        this.coachingTopicMapper = coachingTopicMapper;
    }

    public void assertInputIsValid(List<CoachingTopicDto> coachingTopicDtos) {
        logger.info("Validating coaching topics input is valid");
        assertNotTooManyTopicsAreProvided(coachingTopicDtos);
        assertTopicsAreUnique(coachingTopicDtos);
        assertExperienceIsWithinRange(coachingTopicDtos);
        logger.info("Coaching topics input is valid");
    }

    private Optional<CoachingTopic> findById(String coachingTopicId) {
        return coachingTopicRepository.findById(coachingTopicId);
    }

    public void deleteCoachingTopicsFromDb(List<CoachingTopic> coachingTopics) {
        coachingTopics.stream().map(CoachingTopic::getCoachingTopicId).forEach(this::deleteCoachingTopic);
    }

    private void deleteCoachingTopic(String coachingTopicId) {
        CoachingTopic coachingTopic = findById(coachingTopicId)
                .orElseThrow(() -> new CoachingTopicException("Coaching topic not found"));
        coachingTopicRepository.delete(coachingTopic);
        logger.info("Coaching topic with Id " + coachingTopicId + " has been deleted");
    }

    public List<CoachingTopic> addCoachingTopics(List<CoachingTopicDto> coachingTopicDtos) {
        List<CoachingTopic> coachingTopics = new ArrayList<>();
        for(CoachingTopicDto coachingTopicDto : coachingTopicDtos) {
            Topic topic = topicService.findById(coachingTopicDto.getTopic().getName())
                    .orElseThrow(() -> new TopicException("Topic " + coachingTopicDto.getTopic().getName() + " was not found"));

            CoachingTopic coachingTopic = coachingTopicMapper.toEntity(coachingTopicDto);
            coachingTopic.setExperience(coachingTopicDto.getExperience());
            coachingTopic.setTopic(topic);
            coachingTopicRepository.save(coachingTopic);
            coachingTopics.add(coachingTopic);
        }

        return coachingTopics;
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

    private void assertExperienceIsWithinRange(List<CoachingTopicDto> coachingTopicDtos) {
        int amountOfInvalidExperiences = (int) coachingTopicDtos.stream()
                .map(CoachingTopicDto::getExperience)
                .filter(experience -> experience < 1 || experience > 7)
                .count();

        if(amountOfInvalidExperiences > 0) {
            throw new InvalidInputException("Experience has to be in a range between 1 and 7");
        }
    }
}
