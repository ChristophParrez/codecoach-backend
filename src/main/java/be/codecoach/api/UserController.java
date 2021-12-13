package be.codecoach.api;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.User;
import be.codecoach.exceptions.NotUniqueException;
import be.codecoach.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:4200/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserDto userDto) {
        userService.registerUser(userDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable String userId){
        return userService.getCoacheeProfileDto(userId);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable String userId,
                              @RequestBody UserDto userDto
                           ) {
        userService.updateUser(userId, userDto);
    }
}
