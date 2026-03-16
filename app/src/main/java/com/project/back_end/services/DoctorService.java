package com.project.back_end.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    // Method to get available time slots for a doctor
    public List<String> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        // Example logic (replace with database query)
        return List.of("09:00", "10:30", "14:00", "15:30");
    }

    // Method to validate doctor login
    public boolean validateDoctorLogin(String email, String password) {

        // Example validation logic
        if(email.equals("smith@clinic.com") && password.equals("password123")){
            return true;
        }

        return false;
    }
}
