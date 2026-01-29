package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.Users;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/auth/google")
@Slf4j
public class GoogleAuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/callback")
  public ResponseEntity<?> handleGoogleCallback(@RequestParam String code){
      try{
        //1. Exchange auth code for tokens
          String tokenEndpoint = "https://oauth2.googleapis.com/token";

          MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
          params.add("code",code);
          params.add("client_id",clientId);
          params.add("client_secret",clientSecret);
          params.add("redirect_uri","http://localhost:5173/oauth/callback");
          params.add("grant_type","authorization_code");

          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

          HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params,headers);

          ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint,request,Map.class);
          String idToken = (String) tokenResponse.getBody().get("id_token");
          String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
          ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl,Map.class);

          if(userInfoResponse.getStatusCode() == HttpStatus.OK){
              Map<String,Object> userInfo = userInfoResponse.getBody();
              String email = (String)userInfo.get("email");
              UserDetails userDetails = null;

              try{
                  userDetailsService.loadUserByUsername(email);
              } catch (Exception e) {
                  Users user = new Users();
                  user.setEmail(email);
                  user.setUsername(email);
                  user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                  user.setRoles(Arrays.asList("USER"));
                  userRepository.save(user);
              }

              String jwt = jwtUtils.generateToken(email,60);

              return ResponseEntity.ok(Collections.singletonMap("token",jwt));
          }
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      } catch (Exception e) {
          log.error("Error occured while handleGoogleCallback :: " + e.getMessage());
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
  }
}