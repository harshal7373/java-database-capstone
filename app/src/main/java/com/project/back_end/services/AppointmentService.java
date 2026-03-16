package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repositories.AppointmentRepository;
import com.project.back_end.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    // Existing method for saving appointment
    public Appointment saveAppointment(Appointment appointment) {
        // You might want to add validation here to ensure the slot is available
        return appointmentRepository.save(appointment);
    }
    
    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    // Get appointment by ID
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }
    
    // Delete appointment
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
    
    // NEW METHOD: Get appointments by doctor and date
    public List<Appointment> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        return appointmentRepository.findByDoctorDoctorIdAndAppointmentTimeBetween(
            doctorId, startOfDay, endOfDay
        );
    }
    
    // Check if a specific time slot is available for a doctor
    public boolean isTimeSlotAvailable(Long doctorId, LocalDateTime appointmentTime) {
        List<Appointment> existingAppointments = appointmentRepository
            .findByDoctorDoctorIdAndAppointmentTime(doctorId, appointmentTime);
        
        return existingAppointments.isEmpty();
    }
    
    // Book appointment with availability check
    public Appointment bookAppointment(Appointment appointment) {
        if (isTimeSlotAvailable(appointment.getDoctor().getDoctorId(), 
                                 appointment.getAppointmentTime())) {
            return appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Time slot is not available");
        }
    }
}
