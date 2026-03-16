package com.project.back_end.repositories;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Find patient by email (exact match)
    Optional<Patient> findByEmail(String email);
    
    // Find patient by phone number (exact match)
    Optional<Patient> findByPhone(String phone);
    
    // Find patient by email OR phone
    @Query("SELECT p FROM Patient p WHERE p.email = :emailOrPhone OR p.phone = :emailOrPhone")
    List<Patient> findByEmailOrPhone(@Param("emailOrPhone") String emailOrPhone);
    
    // Check if patient exists by email
    boolean existsByEmail(String email);
    
    // Check if patient exists by phone
    boolean existsByPhone(String phone);
    
    // Find patients by name containing (case insensitive)
    List<Patient> findByNameContainingIgnoreCase(String name);
    
    // Find patients by email domain
    @Query("SELECT p FROM Patient p WHERE p.email LIKE %:domain")
    List<Patient> findByEmailDomain(@Param("domain") String domain);
}
