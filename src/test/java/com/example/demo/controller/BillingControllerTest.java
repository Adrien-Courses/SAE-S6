package com.example.demo.controller;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class BillingControllerTest {
    
    // Bad practice: Testing singleton and using real file system
    private BillingController billingController = BillingController.getInstance();
    
    @Test
    public void testProcessBill() {
        // Bad practice: Test depends on file system
        File billingFile = new File("C:\\hospital\\billing.txt");
        long initialFileSize = billingFile.length();
        
        String result = billingController.processBill(
            "TEST001",
            "DOC001",
            new String[]{"CONSULTATION"}
        );
        
        // Bad practice: Brittle assertions
        assertTrue(result.contains("successfully"));
        assertTrue(billingFile.length() > initialFileSize);
    }
    
    @Test
    public void testCalculateInsurance() {
        // Bad practice: Magic numbers
        double result = Double.parseDouble(
            billingController.calculateInsurance(1000.0)
                .replace("Insurance coverage: $", "")
        );
        
        // Bad practice: Hard-coded expected values
        assertEquals(700.0, result, 0.01);
    }
    
    // Bad practice: Testing internal state
    @Test
    public void testUpdatePrice() {
        billingController.updatePrice("CONSULTATION", 75.0);
        assertEquals(75.0, billingController.getPrices().get("CONSULTATION"), 0.01);
    }
} 