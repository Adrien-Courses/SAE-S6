package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.*;
import com.example.demo.dao.*;
import java.util.*;

@RestController
@RequestMapping("/patient-history")
public class PatientHistoryController {
    
    @Autowired
    private PatientHistoryDao patientHistoryDao;
    
    // Bad practice: Returns full result set without pagination
    @GetMapping("/search")
    public List<PatientHistory> searchHistory(
            @RequestParam String keyword,
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        
        // Bad practice: Loads everything into memory
        List<PatientHistory> results = patientHistoryDao.searchByMultipleCriteria(
            keyword, startDate, endDate);
            
        return results;
    }
    
    // Bad practice: N+1 query problem
    @GetMapping("/patient/{patientId}/summary")
    public Map<String, Object> getPatientSummary(@PathVariable Long patientId) {
        List<PatientHistory> histories = patientHistoryDao.findCompleteHistoryByPatientId(patientId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("visitCount", histories.size());
        
        // Bad practice: Triggers lazy loading for each history
        double totalBilled = histories.stream()
            .mapToDouble(PatientHistory::getTotalBilledAmount)
            .sum();
            
        summary.put("totalBilled", totalBilled);
        return summary;
    }
} 