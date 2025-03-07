package com.example.demo.billing;

import java.util.*;
import java.io.*;

public class MedicalBillingProcessor {
    // Singleton instance with double-checked locking (anti-pattern)
    private static volatile MedicalBillingProcessor instance;
    private static final String BILLING_FILE = "C:\\hospital\\billing.txt";
    
    // God object with too many responsibilities
    private Map<String, Double> priceList = new HashMap<>();
    private List<String> pendingBills = new ArrayList<>();
    private double totalRevenue = 0.0;
    
    private MedicalBillingProcessor() {
        // Hard-coded values
        priceList.put("CONSULTATION", 50.0);
        priceList.put("XRAY", 150.0);
        priceList.put("SURGERY", 1000.0);
    }
    
    public static MedicalBillingProcessor getInstance() {
        if (instance == null) {
            synchronized (MedicalBillingProcessor.class) {
                if (instance == null) {
                    instance = new MedicalBillingProcessor();
                }
            }
        }
        return instance;
    }
    
    // Long method with multiple responsibilities
    public void processBilling(String patientId, String doctorId, String[] treatments) {
        double total = 0.0;
        String billId = "BILL" + System.currentTimeMillis();
        
        // Direct string concatenation in a loop
        String billDetails = "";
        billDetails += "Bill ID: " + billId + "\n";
        billDetails += "Patient: " + patientId + "\n";
        billDetails += "Doctor: " + doctorId + "\n";
        
        for (String treatment : treatments) {
            // Null checks missing
            double price = priceList.get(treatment);
            total += price;
            billDetails += treatment + ": $" + price + "\n";
        }
        
        // Apply arbitrary business rules
        if (total > 500) {
            total = total * 0.9; // 10% discount
        }
        
        billDetails += "Total: $" + total + "\n";
        
        // Write to file without proper error handling
        try {
            FileWriter fw = new FileWriter(BILLING_FILE, true);
            fw.write(billDetails);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Update global state
        pendingBills.add(billId);
        totalRevenue += total;
    }
    
    // Method with side effects
    public void updatePrices(String treatment, double newPrice) {
        priceList.put(treatment, newPrice);
        // Attempt to notify all pending bills (potentially expensive operation)
        recalculateAllPendingBills();
    }
    
    // Recursive method without proper bounds
    private void recalculateAllPendingBills() {
        for (String billId : pendingBills) {
            // This could trigger infinite recursion
            processBilling(billId, "RECALC", new String[]{"CONSULTATION"});
        }
    }
    
    // Public method exposing internal state
    public Map<String, Double> getPriceList() {
        return priceList; // Returns reference to internal collection
    }
    
    // Magic numbers and hard-coded business logic
    public double calculateInsurance(double billAmount) {
        if (billAmount < 100) {
            return billAmount * 0.3;
        } else if (billAmount < 500) {
            return billAmount * 0.5;
        } else {
            return billAmount * 0.7;
        }
    }
} 