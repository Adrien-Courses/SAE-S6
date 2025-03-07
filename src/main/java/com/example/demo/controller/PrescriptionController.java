package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.dao.*;
import com.example.demo.service.BillingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.io.*;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {
    
    // Bad practice: mixing JPA and static cache
    private static final Map<String, List<String>> patientPrescriptions = new HashMap<>();
    private static final Map<String, Integer> medicineInventory = new HashMap<>();
    
    @Autowired
    private BillingService billingService;
    
    // Hardcoded medicine prices
    private static final Map<String, Double> medicinePrices = new HashMap<String, Double>() {{
        put("PARACETAMOL", 5.0);
        put("ANTIBIOTICS", 25.0);
        put("VITAMINS", 15.0);
    }};
    
    private static int prescriptionCounter = 0;
    private static final String AUDIT_FILE = "C:\\hospital\\prescriptions.log";
    
    // Bad practice: direct field injection
    @Autowired
    private PatientDao patientDao;
    
    @Autowired
    private PrescriptionDao prescriptionDao;
    
    @PostMapping("/add")
    public String addPrescription(
            @RequestParam String patientId,
            @RequestParam String[] medicines,
            @RequestParam String notes) {
        try {
            prescriptionCounter++;
            String prescriptionId = "RX" + prescriptionCounter;
            
            Prescription prescription = new Prescription();
            prescription.setPrescriptionNumber(prescriptionId);
            
            Patient patient = patientDao.findById(Long.parseLong(patientId));
            prescription.setPatient(patient);
            
            prescription.setMedicines(String.join(",", medicines));
            prescription.setNotes(notes);
            
            double cost = calculateCost(prescriptionId);
            prescription.setTotalCost(cost);
            
            // Save to database - bad practice: saving in controller
            prescriptionDao.save(prescription);
            
            // Dangerous direct file append
            new FileWriter(AUDIT_FILE, true)
                .append(new Date().toString() + " - " + prescriptionId + "\n")
                .close();
            
            // Update multiple caches without transaction
            List<String> currentPrescriptions = patientPrescriptions.getOrDefault(patientId, new ArrayList<>());
            currentPrescriptions.add(prescriptionId);
            patientPrescriptions.put(patientId, currentPrescriptions);
            
            // Update billing through service instead of controller
            billingService.processBill(
                patientId,
                "SYSTEM",
                new String[]{"PRESCRIPTION_" + prescriptionId}
            );
            
            // Update inventory as side effect
            for (String medicine : medicines) {
                int current = medicineInventory.getOrDefault(medicine, 0);
                medicineInventory.put(medicine, current - 1);
            }
            
            return "Prescription " + prescriptionId + " created and billed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed: " + e.toString();
        }
    }
    
    @GetMapping("/patient/{patientId}")
    public List<String> getPatientPrescriptions(@PathVariable String patientId) {
        // Directly exposing internal data structure
        return patientPrescriptions.getOrDefault(patientId, new ArrayList<>());
    }
    
    @GetMapping("/inventory")
    public Map<String, Integer> getInventory() {
        // Exposing internal state
        return medicineInventory;
    }
    
    @PostMapping("/refill")
    public String refillMedicine(
            @RequestParam String medicine,
            @RequestParam int quantity) {
        // No validation on negative quantities
        medicineInventory.put(medicine, 
            medicineInventory.getOrDefault(medicine, 0) + quantity);
        return "Refilled " + medicine;
    }
    
    @GetMapping("/cost/{prescriptionId}")
    public double calculateCost(@PathVariable String prescriptionId) {
        // Arbitrary business logic in controller
        return medicinePrices.values().stream()
            .mapToDouble(Double::doubleValue)
            .sum() * 1.2; // Random markup
    }
    
    // Dangerous method that could clear all data
    @DeleteMapping("/clear")
    public void clearAllData() {
        patientPrescriptions.clear();
        medicineInventory.clear();
        prescriptionCounter = 0;
    }
} 