package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.TokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private TokenValidationService tokenValidationService;
    
    // Get all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    
    // Get doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor != null) {
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Add new doctor
    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
        Doctor savedDoctor = doctorService.saveDoctor(doctor);
        return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
    }
    
    // Delete doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // NEW METHOD: Get doctor availability with token validation
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String token) {
        
        // Validate token
        if (!tokenValidationService.isValidToken(user, token)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid or expired token");
            errorResponse.put("status", "401");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        
        // Get doctor availability for the specified date
        List<LocalDateTime> availableSlots = doctorService.getDoctorAvailability(doctorId, date);
        
        if (availableSlots == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Doctor not found");
            errorResponse.put("status", "404");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("date", date.toString());
        response.put("availableSlots", availableSlots);
        response.put("user", user);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
