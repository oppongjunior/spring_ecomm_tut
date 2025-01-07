package com.example.ecommerce_tut.controller;

import com.example.ecommerce_tut.dto.ChangePasswordRequest;
import com.example.ecommerce_tut.dto.EmailConfirmationRequest;
import com.example.ecommerce_tut.dto.LoginRequest;
import com.example.ecommerce_tut.exception.ResourceNotFoundException;
import com.example.ecommerce_tut.model.User;
import com.example.ecommerce_tut.service.AuthenticationService;
import com.example.ecommerce_tut.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        userService.changePassword(name, changePasswordRequest);
        return ResponseEntity.ok("Password changed successfully");
    }
    @PostMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestBody EmailConfirmationRequest request){
        try {
            userService.confirmEmail(request.getEmail(), request.getConfirmationCode());
            return ResponseEntity.ok("Email confirmed successfully");
        }catch(BadCredentialsException e){
            return ResponseEntity.badRequest().body("Invalid Confirmation Code");
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
