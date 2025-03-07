package com.example.demo.dao;

import com.example.demo.model.Prescription;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PrescriptionDaoImpl extends AbstractHibernateDao<Prescription, Long> implements PrescriptionDao {
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Prescription> findByPatientId(Long patientId) {
        return getEntityManager()
                .createQuery("FROM Prescription WHERE patient.id = :patientId")
                .setParameter("patientId", patientId)
                .getResultList();
    }
} 