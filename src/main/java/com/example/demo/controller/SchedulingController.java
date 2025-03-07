package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.*;
import com.example.demo.dao.*;
import com.example.demo.service.EmailService;
import java.util.*;

@RestController
@RequestMapping("/scheduling")
public class SchedulingController {
    
    @Autowired
    private AppointmentDao appointmentDao;
    
    @Autowired
    private DoctorDao doctorDao;
    
    private final EmailService emailService = EmailService.getInstance();
    
    // Bad practice: Complex scheduling logic in controller
    @PostMapping("/appointment")
    public String scheduleAppointment(
            @RequestParam Long doctorId,
            @RequestParam Long patientId,
            @RequestParam Date appointmentDate) {
        try {
            Doctor doctor = doctorDao.findById(doctorId);
            
            // Bad practice: Business logic in controller
            List<Appointment> doctorAppointments = appointmentDao.findByDoctorId(doctorId);
            for (Appointment existing : doctorAppointments) {
                // Bad practice: Direct date comparison
                if (existing.getAppointmentDate().equals(appointmentDate)) {
                    return "Doctor is not available at this time";
                }
            }
            
            // Bad practice: Magic numbers
            Calendar cal = Calendar.getInstance();
            cal.setTime(appointmentDate);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (hour < 9 || hour > 17) {
                return "Appointments only available between 9 AM and 5 PM";
            }
            
            // Bad practice: Direct email in controller
            emailService.sendEmail(
                doctor.getEmail(),
                "New Appointment Scheduled",
                "You have a new appointment on " + appointmentDate
            );
            
            return "Appointment scheduled successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    // Bad practice: Complex business logic in controller
    @GetMapping("/available-slots")
    public List<Date> getAvailableSlots(@RequestParam Long doctorId, @RequestParam Date date) {
        List<Date> availableSlots = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        // Bad practice: Hardcoded business rules
        for (int hour = 9; hour <= 17; hour++) {
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, 0);
            
            boolean slotAvailable = true;
            for (Appointment app : appointmentDao.findByDoctorId(doctorId)) {
                Calendar appCal = Calendar.getInstance();
                appCal.setTime(app.getAppointmentDate());
                if (appCal.get(Calendar.HOUR_OF_DAY) == hour) {
                    slotAvailable = false;
                    break;
                }
            }
            
            if (slotAvailable) {
                availableSlots.add(cal.getTime());
            }
        }
        
        return availableSlots;
    }
} 