package com.example.moviereservationsystem.controller;

import com.example.moviereservationsystem.model.Reservation;
import com.example.moviereservationsystem.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public Reservation createReservation (@RequestParam Long userId, @RequestParam Long showTimeId, @RequestBody List <String> seats) {
        return this.reservationService.createReservation (userId, showTimeId, seats);
    }

    @GetMapping("/user/{userId}")
    public List <Reservation> getReservationsByUser (@PathVariable Long userId) {
        return this.reservationService.getReservationsByUser (userId);
    }

    @PostMapping("/cancel/{reservationId}")
    public void cancelReservations (@PathVariable Long reservationId, @RequestParam Long userId) {
        this.reservationService.cancelReservation (reservationId, userId);
    }

    @GetMapping("/all")
    public List <Reservation> getAllReservations () {
        return this.reservationService.getAllReservations ();
    }
}