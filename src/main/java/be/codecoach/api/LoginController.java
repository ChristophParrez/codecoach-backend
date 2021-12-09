package be.codecoach.api;

import be.codecoach.api.dtos.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security/login")
@CrossOrigin(value = "http://localhost:4200/")
public class LoginController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody LoginDto loginDto){

    }


}
