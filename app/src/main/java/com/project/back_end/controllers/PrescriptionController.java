package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.TokenValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private TokenValidationService tokenValidationService;
    
    // Get all prescriptions
    @GetMapping
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }
    
    // Get prescription by ID
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        Prescription prescription = prescriptionService.getPrescriptionById(id);
        if (prescription != null) {
            return new ResponseEntity<>(prescription, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Get prescriptions by patient ID
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }
    
    // Get prescriptions by doctor ID
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDoctor(@PathVariable Long doctorId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }
    
    // UPDATED METHOD: Add prescription with token validation and @Valid
    @PostMapping("/add/{token}")
    public ResponseEntity<Map<String, Object>> addPrescription(
            @PathVariable String token,
            @Valid @RequestBody Prescription prescription) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Validate token
        if (!tokenValidationService.isValidToken("admin", token) && 
            !tokenValidationService.isValidToken("doctor", token)) {
            
            response.put("error", "Invalid or expired token");
            response.put("status", "401");
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        
        try {
            // Save the prescription
            Prescription savedPrescription = prescriptionService.savePrescription(prescription);
            
            // Build success response
            response.put("message", "Prescription added successfully");
            response.put("prescriptionId", savedPrescription.getPrescriptionId());
            response.put("patientName", savedPrescription.getPatient().getName());
            response.put("doctorName", savedPrescription.getDoctor().getName());
            response.put("medication", savedPrescription.getMedication());
            response.put("dosage", savedPrescription.getDosage());
            response.put("status", "201");
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (Exception e) {
            // Build error response
            response.put("error", "Failed to add prescription: " + e.getMessage());
            response.put("status", "400");
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Update prescription
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(
            @PathVariable Long id, 
            @Valid @RequestBody Prescription prescription) {
        prescription.setPrescriptionId(id);
        Prescription updatedPrescription = prescriptionService.savePrescription(prescription);
        return new ResponseEntity<>(updatedPrescription, HttpStatus.OK);
    }
    
    // Delete prescription
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
