package be.codecoach.api;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.exceptions.NotUniqueException;
import be.codecoach.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED).body("Registration is successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
