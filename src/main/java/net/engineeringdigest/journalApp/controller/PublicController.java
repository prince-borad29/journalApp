package net.engineeringdigest.journalApp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.dto.ErrorDTO;
import net.engineeringdigest.journalApp.dto.ForgotEmailDTO;
import net.engineeringdigest.journalApp.dto.TokenDTO;
import net.engineeringdigest.journalApp.dto.UserDTO;
import net.engineeringdigest.journalApp.entity.Users;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("public")
@Slf4j
@Tag(name = "Public APIs")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("health-check")
    public ResponseEntity<?> healthCheck() {
        Map<String, String> res = new HashMap<>();
        res.put("health", "OK");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO user) {
        Users createUser = new Users();
        createUser.setUsername(user.getUsername());
        createUser.setPassword(user.getPassword());
        createUser.setEmail(user.getEmail());
        createUser.setSentimentAnalysis(user.isSentimentAnalysis());
        userService.saveNewUser(createUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtils.generateToken(userDetails.getUsername(), 60);

            TokenDTO token = new TokenDTO();
            token.setToken(jwt);

            return ResponseEntity.ok(
                    token
            );
            } catch (BadCredentialsException e) {
            ErrorDTO error = new ErrorDTO();
            error.setError("Invalid Username Or Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        catch (Exception e) {
            log.error("Error in login", e);
            ErrorDTO error = new ErrorDTO();
            error.setError("Authentication failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotEmailDTO forgotEmailDTO) {
        try {
            userService.findByUsername(forgotEmailDTO.getEmail());

            String token = jwtUtils.generateToken(forgotEmailDTO.getEmail(), 5);
            TokenDTO tokenDto = new TokenDTO();
            tokenDto.setToken(token);

            String body = "<html>" +
                    "<body style=\"margin:0; padding:0; font-family: Arial, sans-serif; background:#f4f6f8;\">" +
                    "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">" +
                    "<tr>" +
                    "<td align=\"center\" style=\"padding:40px 10px;\">" +

                    "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" " +
                    "style=\"max-width:600px; background:#ffffff; border-radius:8px; padding:40px;\">" +

                    "<tr>" +
                    "<td align=\"center\" style=\"font-size:22px; font-weight:bold; color:#222;\">" +
                    "Secure Login Link" +
                    "</td>" +
                    "</tr>" +

                    "<tr>" +
                    "<td style=\"padding:20px 0; font-size:16px; color:#444;\">" +
                    "Hi " + forgotEmailDTO.getEmail() + "," +
                    "</td>" +
                    "</tr>" +

                    "<tr>" +
                    "<td style=\"font-size:16px; color:#444;\">" +
                    "Click the button below to securely sign in to your account. " +
                    "This link will expire in <strong>" + 5 + " minutes</strong>." +
                    "</td>" +
                    "</tr>" +

                    "<tr>" +
                    "<td align=\"center\" style=\"padding:30px 0;\">" +
                    "<a href=\"" + "http://localhost:5173/reset-password?token="+ tokenDto.getToken() + "\" " +
                    "style=\"background:#4F46E5; color:#ffffff; text-decoration:none; padding:14px 28px; " +
                    "border-radius:6px; font-weight:bold; display:inline-block;\">" +
                    "Sign In Securely" +
                    "</a>" +
                    "</td>" +
                    "</tr>" +

                    "<tr>" +
                    "<td style=\"font-size:14px; color:#666;\">" +
                    "If you didn’t request this email, you can safely ignore it." +
                    "</td>" +
                    "</tr>" +

                    "<tr>" +
                    "<td style=\"padding-top:30px; font-size:12px; color:#999; text-align:center;\">" +
                    "© " + Year.now().getValue() + " Journal App. All rights reserved." +
                    "</td>" +
                    "</tr>" +

                    "</table>" +
                    "</td>" +
                    "</tr>" +
                    "</table>" +
                    "</body>" +
                    "</html>";

            forgotEmailDTO.setBody(body);


            emailService.sendMail(
                    forgotEmailDTO.getEmail(),
                    "Reset Journal App Password",
                    forgotEmailDTO.getBody()
            );

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO user) {
        try {
            Users userInDb = userService.findByUsername(user.getUsername());
            userInDb.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(userInDb);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error reset PWD " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}