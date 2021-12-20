package be.codecoach.services;

import be.codecoach.api.dtos.TopicDto;
import be.codecoach.domain.Topic;
import be.codecoach.repositories.TopicRepository;
import be.codecoach.services.mappers.TopicMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    public Optional<Topic> findById(String topicId) {
        return topicRepository.findById(topicId);
    }

    public List<TopicDto> getAllTopics() {
        return topicMapper.toDto(topicRepository.findAll());
    }

    public void createTopic(TopicDto topicDto) {
        if(topicRepository.existsById(topicDto.getName())) {
            throw new IllegalArgumentException("Topic already saved");
        }
        topicRepository.save(topicMapper.toEntity(topicDto));
    }

    /*public void deleteTopic(TopicDto topicDto) {
        if(!topicRepository.existsById(topicDto.getName())) {
            throw new IllegalArgumentException("Topic already saved");
        }
        // get all coaches
        // delete topic from all coaches

        topicRepository.delete(topicMapper.toEntity(topicDto));
    }*/
}
