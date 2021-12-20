package be.codecoach.api;


import be.codecoach.api.dtos.FeedbackDto;
import be.codecoach.api.dtos.SessionDto;
import be.codecoach.services.SessionService;
import be.codecoach.twilio.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sessions")
@CrossOrigin
public class SessionController {

    private final SessionService sessionService;
    private final SmsSender sms;

    @Autowired
    public SessionController(SessionService sessionService, SmsSender sms) {
        this.sessionService = sessionService;
        this.sms = sms;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('COACHEE')")
    public void requestSession(@RequestBody SessionDto sessionDto) {
        sms.sendMessage();
        sessionService.requestSession(sessionDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('COACHEE', 'COACH', 'ADMIN')")
    public List<SessionDto> getSessions(@RequestParam(required = false) String role) {
        return sessionService.getSessions(role);
    }

    @PatchMapping(path = "/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateSessionStatus(@PathVariable String sessionId,
                                    @RequestParam String newStatus) {
        sessionService.updateSessionStatus(sessionId, newStatus);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/{sessionId}/feedback")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('COACHEE', 'COACH')")
    public void giveFeedback(@PathVariable String sessionId,
                             @RequestBody FeedbackDto feedbackDto){
        sessionService.giveFeedback(sessionId, feedbackDto);
    }
}
