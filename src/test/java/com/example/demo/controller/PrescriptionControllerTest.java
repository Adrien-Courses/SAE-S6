package com.example.demo.controller;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class PrescriptionControllerTest {
    
    private PrescriptionController prescriptionController;
    
    @Before
    public void setUp() {
        prescriptionController = new PrescriptionController();
    }
    
    // Bad practice: Testing multiple things in one test
    @Test
    public void testAddAndRetrievePrescription() {
        String result = prescriptionController.addPrescription(
            "PAT001",
            new String[]{"PARACETAMOL"},
            "Test notes"
        );
        
        assertTrue(result.contains("created"));
        
        List<String> prescriptions = prescriptionController.getPatientPrescriptions("PAT001");
        assertFalse(prescriptions.isEmpty());
        
        // Bad practice: Assuming implementation details
        assertTrue(prescriptions.get(0).startsWith("RX"));
    }
    
    // Bad practice: No error cases tested
    @Test
    public void testInventory() {
        prescriptionController.refillMedicine("PARACETAMOL", 10);
        assertEquals(10, (int) prescriptionController.getInventory().get("PARACETAMOL"));
    }
    
    // Bad practice: Testing dangerous operations
    @Test
    public void testClearData() {
        prescriptionController.clearAllData();
        assertTrue(prescriptionController.getInventory().isEmpty());
    }
} 