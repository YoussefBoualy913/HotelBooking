package util;
import model.Reservation;
import model.ReservationStatus;
import repository.ReservationRepository;
import repository.impl.InMemoryReservationRepository;

import java.time.LocalDate;
import java.util.List;

public  class Roomutils
{
    private ReservationRepository reservationRepository;

    public Roomutils(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public boolean isRoomAvailable(
            String roomNumber,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        List<Reservation> reservations =
                reservationRepository.findByRoomNumber(roomNumber);
        return reservations.stream()
                .filter(reservation ->
                        reservation.getStatus() == ReservationStatus.CONFIRMED)
                .noneMatch(reservation ->
                        reservation.getCheckIn().isBefore(checkOut)
                                && reservation.getCheckOut().isAfter(checkIn)
                );
    }
}