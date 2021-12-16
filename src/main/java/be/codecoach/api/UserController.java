package be.codecoach.api;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserDto userDto) {
        userService.createAccount(userDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable String userId) {
        return userService.getCoacheeProfileDto(userId);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable String userId,
                           @RequestBody UserDto userDto
    ) {
        userService.updateUser(userId, userDto);
    }

    @PatchMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void becomeCoach(@PathVariable String userId, HttpServletResponse response) {
        userService.becomeCoach(userId, response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('COACHEE', 'COACH')")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
