package be.codecoach.api;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coaches")
@CrossOrigin(value = "http://localhost:4200/")
public class CoachController {

    private final UserService userService;

    @Autowired
    public CoachController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getCoach(@PathVariable String userId){
        return userService.getCoachProfileDto(userId);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public void updateCoach(@PathVariable String userId,
                            @RequestBody UserDto userDto) {
        userService.updateCoach(userId, userDto);
    }
}
