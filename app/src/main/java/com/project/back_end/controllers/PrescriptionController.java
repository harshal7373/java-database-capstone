package com.project.back_end.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private List<String> prescriptions = new ArrayList<>();

    @GetMapping
    public List<String> getAllPrescriptions() {
        return prescriptions;
    }

    @PostMapping
    public String addPrescription(@RequestBody String prescription) {
        prescriptions.add(prescription);
        return "Prescription added successfully";
    }
}