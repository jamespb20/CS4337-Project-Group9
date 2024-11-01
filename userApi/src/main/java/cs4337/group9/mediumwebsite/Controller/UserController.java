package cs4337.group9.mediumwebsite.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")  // Base URL for the user-related endpoints
public class UserController {

    // Simple GET endpoint
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, User!";
    }
}