package repository.impl;
import java.util.*;
import repository.ReservationRepository;
import model.Reservation;
public class InMemoryReservationRepository implements ReservationRepository {

    private Map<UUID, Reservation> reservations = new HashMap<>();

    @Override
    public void save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public Optional<Reservation> findByCode(String reservationCode) {
        return reservations.values()
                .stream()
                .filter(reservation ->
                        reservation.getReservationCode()
                                .equalsIgnoreCase(reservationCode))
                .findFirst();
    }

    @Override
    public List<Reservation> findByUserId(UUID userId) {
        return reservations.values()
                .stream()
                .filter(reservation ->
                        reservation.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<Reservation> findByRoomNumber(String roomNumber) {
        return reservations.values()
                .stream()
                .filter(reservation ->
                        reservation.getRoomNumber().equals(roomNumber))
                .toList();
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }
}