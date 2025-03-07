package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.*;
import com.example.demo.dao.*;
import java.util.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    
    @Autowired
    private RoomDao roomDao;
    
    @Autowired
    private AppointmentDao appointmentDao;
    
    // Bad practice: Complex business logic in controller
    @PostMapping("/assign")
    public String assignRoom(@RequestParam Long appointmentId, @RequestParam String roomNumber) {
        try {
            Room room = roomDao.findByRoomNumber(roomNumber);
            Appointment appointment = appointmentDao.findById(appointmentId);
            
            // Bad practice: Business rules in controller
            if (room.getType().equals("SURGERY") && 
                !appointment.getDoctor().getSpecialization().equals("SURGEON")) {
                return "Error: Only surgeons can use surgery rooms";
            }
            
            // Bad practice: Direct entity manipulation
            if (room.getCurrentPatientCount() >= room.getCapacity()) {
                return "Error: Room is at full capacity";
            }
            
            // Bad practice: No transaction boundary
            room.setCurrentPatientCount(room.getCurrentPatientCount() + 1);
            appointment.setRoomNumber(roomNumber);
            
            roomDao.update(room);
            appointmentDao.update(appointment);
            
            return "Room assigned successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    // Bad practice: Exposing internal state
    @GetMapping("/availability")
    public Map<String, Object> getRoomAvailability(@RequestParam String roomNumber) {
        Room room = roomDao.findByRoomNumber(roomNumber);
        Map<String, Object> result = new HashMap<>();
        
        result.put("roomNumber", room.getRoomNumber());
        result.put("capacity", room.getCapacity());
        result.put("currentPatients", room.getCurrentPatientCount());
        result.put("available", room.canAcceptPatient());
        
        return result;
    }
} 