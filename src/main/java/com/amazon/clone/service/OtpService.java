package com.amazon.clone.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, Map<String, String>> pendingUsers = new HashMap<>();

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        return otp;
    }

    public void savePendingUser(String email, Map<String, String> userData) {
        pendingUsers.put(email, userData);
    }

    public Map<String, String> getPendingUser(String email) {
        return pendingUsers.get(email);
    }

    public boolean verifyOtp(String email, String otp) {
        return otp != null && otp.equals(otpStore.get(email));
    }

    public void clearOtp(String email) {
        otpStore.remove(email);
        pendingUsers.remove(email);
    }
}