package com.example.demo.model;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "rooms")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "room_number", unique = true)
    private String roomNumber;
    
    @Column(name = "floor")
    private Integer floor;
    
    @Column(name = "type")
    private String type; // Bad practice: Should be enum
    
    @Column(name = "capacity")
    private Integer capacity;
    
    @Column(name = "is_occupied")
    private Boolean isOccupied = false;
    
    @OneToMany(mappedBy = "room")
    private Set<Appointment> appointments = new HashSet<>();
    
    // Bad practice: Mutable state in entity
    @Column(name = "current_patient_count")
    private Integer currentPatientCount = 0;
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public Integer getFloor() {
        return floor;
    }
    
    public void setFloor(Integer floor) {
        this.floor = floor;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Boolean getIsOccupied() {
        return isOccupied;
    }
    
    public void setIsOccupied(Boolean isOccupied) {
        this.isOccupied = isOccupied;
    }
    
    public Set<Appointment> getAppointments() {
        return appointments;
    }
    
    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }
    
    public Integer getCurrentPatientCount() {
        return currentPatientCount;
    }
    
    public void setCurrentPatientCount(Integer currentPatientCount) {
        this.currentPatientCount = currentPatientCount;
        // Bad practice: Side effect in setter
        this.isOccupied = currentPatientCount >= capacity;
    }
    
    // Bad practice: Business logic in entity
    public boolean canAcceptPatient() {
        return currentPatientCount < capacity && !isOccupied;
    }
} 