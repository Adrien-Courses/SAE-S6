package com.example.demo.dao;

import com.example.demo.model.PatientHistory;
import java.util.List;
import java.util.Date;

public interface PatientHistoryDao extends GenericDao<PatientHistory, Long> {
    List<PatientHistory> findCompleteHistoryByPatientId(Long patientId);
    List<PatientHistory> searchByMultipleCriteria(String keyword, Date startDate, Date endDate);
} 