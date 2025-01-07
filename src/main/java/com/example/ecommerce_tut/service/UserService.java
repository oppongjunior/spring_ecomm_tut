package com.example.ecommerce_tut.service;

import com.example.ecommerce_tut.dto.ChangePasswordRequest;
import com.example.ecommerce_tut.exception.ResourceNotFoundException;
import com.example.ecommerce_tut.model.Role;
import com.example.ecommerce_tut.model.User;
import com.example.ecommerce_tut.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public User save(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setConfirmationCode(generateConfirmationCode());
        emailService.sendConfirmationCode(user);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getUserByEmail(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void confirmEmail(String email, String confirmationCode) {
        User user = getUserByEmail(email);
        if (!confirmationCode.equals(user.getConfirmationCode())) {
            throw new BadCredentialsException("Confirmation code is incorrect");
        }
        user.setEmailConfirmation(true);
        user.setConfirmationCode(null);
        userRepository.save(user);
    }

    private String generateConfirmationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(100000);
        return String.valueOf(code);
    }
}
