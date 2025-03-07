package com.example.demo.dao;

import com.example.demo.model.Appointment;
import java.util.Date;
import java.util.List;

public interface DaoRendezVous extends DaoGenerique<Appointment, Long> {
    List<Appointment> trouverParIdPatient(Long idPatient);
    List<Appointment> trouverParIdMedecin(Long idMedecin);
    List<Appointment> trouverParPlageDates(Date dateDebut, Date dateFin);
} 