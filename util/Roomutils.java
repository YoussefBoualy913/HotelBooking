package util;
import model.Reservation;
import model.ReservationStatus;
import repository.ReservationRepository;
import repository.impl.InMemoryReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public  class Roomutils
{
    private ReservationRepository reservationRepository;

    public Roomutils(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public boolean isRoomAvailable(
            String roomNumber,
            LocalDate checkIn,
            LocalDate checkOut,
            UUID reservationId
    ) {
        return reservationRepository
                .findByRoomNumber(roomNumber)
                .stream()
                .filter(reservation ->
                        reservation.getStatus() == ReservationStatus.CONFIRMED)
                .filter(reservation ->
                        reservationId == null
                                || !reservation.getId().equals(reservationId))
                .noneMatch(reservation ->
                        reservation.getCheckIn().isBefore(checkOut)
                                && reservation.getCheckOut().isAfter(checkIn)
                );
    }
}