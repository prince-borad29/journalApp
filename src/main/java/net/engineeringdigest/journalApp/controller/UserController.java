package net.engineeringdigest.journalApp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.entity.Users;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.scheduler.UserScheduler;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name="User APIs",description = "Add , Edit , Delete User")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private UserScheduler userScheduler;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody Users user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users userInDb = userService.findByUsername(username);
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userRepo.deleteByUsername(authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greetings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WeatherResponse res = weatherService.getWeather("Rajkot");

        String greeting = "";

        if(res != null){
            try {
                greeting += " , Weather Feels Like " + res.getCurrent().getFeelsLike();
            } catch (Exception e) {
                log.error("Error : User Service :: " + e.getMessage());
            }
        }

        return new ResponseEntity<>("Hi , " + authentication.getName() + greeting ,
                HttpStatus.OK);
    }

//    @GetMapping("get-sa")
//    public void getSaNow(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        userScheduler.fetchUserAndSendSaMail();
//    }

}