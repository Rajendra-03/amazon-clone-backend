package com.amazon.clone.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.amazon.clone.model.User;
import com.amazon.clone.repository.UserRepository;
import com.amazon.clone.service.EmailService;
import com.amazon.clone.service.OtpService;

//@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-otp")
    public Map<String, Object> sendOtp(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            response.put("success", false);
            response.put("message", "Email already exists");
            return response;
        }

        Map<String, String> pendingData = new HashMap<>();
        pendingData.put("name", user.getName());
        pendingData.put("email", user.getEmail());
        pendingData.put("password", user.getPassword());
        pendingData.put("address", user.getAddress());

        otpService.savePendingUser(user.getEmail(), pendingData);
        String otp = otpService.generateOtp(user.getEmail());
        emailService.sendOtpEmail(user.getEmail(), otp);

        response.put("success", true);
        response.put("message", "OTP sent to email");
        return response;
    }

    @PostMapping("/verify-otp")
    public Map<String, Object> verifyOtp(@RequestBody Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();

        String email = data.get("email");
        String otp = data.get("otp");

        if (!otpService.verifyOtp(email, otp)) {
            response.put("success", false);
            response.put("message", "Invalid OTP");
            return response;
        }

        Map<String, String> pendingUser = otpService.getPendingUser(email);
        if (pendingUser == null) {
            response.put("success", false);
            response.put("message", "No signup data found");
            return response;
        }

        User user = new User();
        user.setName(pendingUser.get("name"));
        user.setEmail(pendingUser.get("email"));
        user.setPassword(pendingUser.get("password"));
        user.setAddress(pendingUser.get("address"));

        userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail(), user.getName());
        otpService.clearOtp(email);

        response.put("success", true);
        response.put("message", "Signup verified successfully");
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();

        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> existing = userRepository.findByEmail(email);

        if (existing.isPresent() && existing.get().getPassword().equals(password)) {
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("user", existing.get());
            return response;
        }

        response.put("success", false);
        response.put("message", "Invalid email or password");
        return response;
    }
}
