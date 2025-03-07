package com.example.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "prescription_number")
    private String prescriptionNumber; // Duplicate with static counter in controller
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @Column(name = "medicines")
    private String medicines; // Bad practice: storing array as comma-separated string
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "total_cost")
    private Double totalCost; // Redundant storage of calculated value
    
    @Column(name = "is_billed")
    private Boolean isBilled = false; // State that should be in a different table
    
    @Column(name = "inventory_updated")
    private Boolean inventoryUpdated = false; // Another state flag
    
    // Bad practice: storing audit info in entity
    @Column(name = "created_date")
    private Date createdDate = new Date();
    
    @Column(name = "last_modified")
    private Date lastModified = new Date();
    
    // Default constructor
    public Prescription() {}
    
    // Getters and setters with no validation
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPrescriptionNumber() {
        return prescriptionNumber;
    }
    
    public void setPrescriptionNumber(String prescriptionNumber) {
        this.prescriptionNumber = prescriptionNumber;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public String getMedicines() {
        return medicines;
    }
    
    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Double getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
    
    public Boolean getBilled() {
        return isBilled;
    }
    
    public void setBilled(Boolean billed) {
        isBilled = billed;
        lastModified = new Date(); // Side effect in setter
    }
    
    public Boolean getInventoryUpdated() {
        return inventoryUpdated;
    }
    
    public void setInventoryUpdated(Boolean inventoryUpdated) {
        this.inventoryUpdated = inventoryUpdated;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Date getLastModified() {
        return lastModified;
    }
    
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
} 