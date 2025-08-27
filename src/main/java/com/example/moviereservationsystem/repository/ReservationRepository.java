package com.example.moviereservationsystem.repository;

import com.example.moviereservationsystem.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository <Reservation, Long> {
    List <Reservation> findByUserId (Long userId);
    List <Reservation> findByShowTimeId (Long showTimeId);
    List <Reservation> findByCanceledFalse ();
}