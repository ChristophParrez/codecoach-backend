package be.codecoach.api;

import be.codecoach.api.dtos.TopicDto;
import be.codecoach.services.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@CrossOrigin(value = "http://localhost:4200/")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<TopicDto> getTopics(){
        return topicService.getAllTopics();
    }

}
