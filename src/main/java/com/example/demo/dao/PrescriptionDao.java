package com.example.demo.dao;

import com.example.demo.model.Prescription;
import java.util.List;

public interface PrescriptionDao extends GenericDao<Prescription, Long> {
    List<Prescription> findByPatientId(Long patientId);
} 