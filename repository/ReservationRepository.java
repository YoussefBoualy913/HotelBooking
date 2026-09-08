package repository;
import model.Reservation;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
public interface ReservationRepository {

    void save(Reservation reservation);
    Optional<Reservation> findById(UUID id);
    Optional<Reservation> findByCode(String code);
    List<Reservation> findByUserId(UUID userId);
    List<Reservation> findByRoomNumber(String roomNumber);
    List<Reservation> findAll();
}