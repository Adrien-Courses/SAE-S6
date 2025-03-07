package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.*;
import com.example.demo.service.EmailService;
import com.example.demo.dao.*;
import java.util.*;
import java.util.stream.Collectors;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    
    @Autowired
    private InventoryDao inventoryDao;
    
    private final EmailService emailService = EmailService.getInstance();
    
    // Bad practice: Direct entity manipulation in controller
    @PostMapping("/supplier-invoice")
    public String processSupplierInvoice(@RequestBody SupplierInvoice invoice) {
        try {
            // Bad practice: No transaction boundary
            for (SupplierInvoiceDetail detail : invoice.getDetails()) {
                Inventory inventory = detail.getInventory();
                
                // Bad practice: Direct inventory manipulation
                inventory.setQuantity(inventory.getQuantity() + detail.getQuantity());
                inventory.setUnitPrice(detail.getUnitPrice());
                inventory.setLastRestocked(new Date());
                
                // Bad practice: No batch processing
                inventoryDao.update(inventory);
            }
            
            return "Supplier invoice processed successfully";
        } catch (Exception e) {
            // Bad practice: Returning exception message
            return "Error: " + e.getMessage();
        }
    }
    
    // Bad practice: Exposing internal inventory state
    @GetMapping("/low-stock")
    public List<Inventory> getLowStockItems() {
        return inventoryDao.findAll().stream()
            .filter(Inventory::needsRestock)
            .collect(Collectors.toList());
    }
    
    // Bad practice: Complex business logic in controller
    @PostMapping("/reorder")
    public String reorderItems() {
        List<Inventory> lowStockItems = inventoryDao.findNeedingRestock();
        
        for (Inventory item : lowStockItems) {
            // Bad practice: Magic numbers and hardcoded logic
            int reorderQuantity = item.getReorderLevel() * 2;
            
            // Bad practice: Direct file I/O in controller
            try (FileWriter fw = new FileWriter("C:\\hospital\\orders.txt", true)) {
                fw.write("REORDER: " + item.getItemCode() + ", Quantity: " + reorderQuantity + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Bad practice: Direct email in controller
            emailService.sendEmail(
                "supplier@example.com",
                "Reorder Request",
                "Please restock " + item.getName() + " (Quantity: " + reorderQuantity + ")"
            );
        }
        
        return "Reorder requests sent for " + lowStockItems.size() + " items";
    }
} 