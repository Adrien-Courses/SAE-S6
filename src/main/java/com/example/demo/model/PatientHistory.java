package com.example.demo.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "patient_history")
public class PatientHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER) // Bad practice: EAGER loading
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @OneToMany(mappedBy = "patientHistory", fetch = FetchType.EAGER) // Bad practice: EAGER loading
    private Set<Appointment> appointments = new HashSet<>();
    
    @OneToMany(fetch = FetchType.EAGER) // Bad practice: EAGER loading
    private Set<Prescription> prescriptions = new HashSet<>();
    
    @OneToMany(fetch = FetchType.EAGER) // Bad practice: EAGER loading
    private Set<Treatment> treatments = new HashSet<>();
    
    @OneToMany(fetch = FetchType.EAGER) // Bad practice: EAGER loading
    private Set<Bill> bills = new HashSet<>();
    
    @OneToMany(fetch = FetchType.EAGER) // Bad practice: EAGER loading
    private Set<LabResult> labResults = new HashSet<>();
    
    @Column(name = "visit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitDate;
    
    @Column(columnDefinition = "TEXT")
    private String diagnosis;
    
    @Column(columnDefinition = "TEXT")
    private String symptoms;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    // Getters and setters with more performance problems
    public Set<Appointment> getAppointments() {
        // Bad practice: Sorting in memory
        return new TreeSet<>(appointments);
    }
    
    public List<Bill> getBillsSorted() {
        // Bad practice: Converting and sorting in memory
        List<Bill> sortedBills = new ArrayList<>(bills);
        Collections.sort(sortedBills, (b1, b2) -> b2.getBillDate().compareTo(b1.getBillDate()));
        return sortedBills;
    }
    
    // Bad practice: Expensive calculation in entity
    public Double getTotalBilledAmount() {
        return bills.stream()
            .mapToDouble(Bill::getTotalAmount)
            .sum();
    }
} 