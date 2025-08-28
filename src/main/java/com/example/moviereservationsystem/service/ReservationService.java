package com.example.moviereservationsystem.service;

import com.example.moviereservationsystem.model.Reservation;
import com.example.moviereservationsystem.model.ShowTime;
import com.example.moviereservationsystem.model.User;
import com.example.moviereservationsystem.repository.ReservationRepository;
import com.example.moviereservationsystem.repository.ShowTimeRepository;
import com.example.moviereservationsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ShowTimeRepository showTimeRepository;

    @Autowired
    private UserRepository userRepository;

    public Reservation createReservation (Long userId, Long showTimeId, List <String> seats) {
        User user = this.userRepository.findById (userId)
                .orElseThrow (() -> new RuntimeException ("User not found"));
        ShowTime showTime = this.showTimeRepository.findById (showTimeId)
                .orElseThrow (() -> new RuntimeException ("ShowTime not found"));

        if (seats.size () > showTime.getAvailableSeats ()) {
            throw new RuntimeException ("Not enough available seats");
        }

        Reservation reservation = new Reservation (user, showTime, LocalDateTime.now ());
        reservation.setReservedSeats (seats);

        showTime.setAvailableSeats (showTime.getAvailableSeats () - seats.size ());
        this.showTimeRepository.save (showTime);

        return this.reservationRepository.save (reservation);
    }

    public List <Reservation> getReservationsByUser (Long userId) {
        return this.reservationRepository.findByUserId (userId);
    }

    public void cancelReservation (Long reservationId, Long userId) {
        Reservation reservation = this.reservationRepository.findById (reservationId)
                .orElseThrow (() -> new RuntimeException ("Reservation not found"));

        if (!reservation.getUser ().getId ().equals (userId)) {
            throw new RuntimeException ("You can only cancel your own reservations");
        }

        if (reservation.getReservationTime ().isBefore (LocalDateTime.now ())) {
            throw new RuntimeException ("Cannot cancel past reservations");
        }

        reservation.setCanceled (true);

        ShowTime showTime = reservation.getShowTime ();
        showTime.setAvailableSeats (showTime.getAvailableSeats () + reservation.getReservedSeats ().size ());

        this.showTimeRepository.save (showTime);
        this.reservationRepository.save (reservation);
    }

    public List <Reservation> getAllReservations () {
        return this.reservationRepository.findAll ();
    }

    public long getTotalCapacity (Long showTimeId) {
        ShowTime showTime = this.showTimeRepository.findById (showTimeId)
                .orElseThrow (() -> new RuntimeException ("ShowTime not found"));
        return showTime.getTotalSeats ();
    }

    public long getTotalRevenue (double pricePerSeat) {
        return this.reservationRepository.findByCanceledFalse ()
                .stream ().mapToLong (r -> r.getReservedSeats ().size ())
                .map (seats -> seats * (long) pricePerSeat).sum ();
    }
}