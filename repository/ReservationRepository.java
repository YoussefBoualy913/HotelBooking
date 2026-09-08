public interface ReservationRepository {

    void save(Reservation reservation);
    Optional<Reservation> findById(UUID id);
    Optional<Reservation> findByCode(String code);
    List<Reservation> findByUserId(UUID userId);
    List<Reservation> findByRoomNumber(String roomNumber);
    List<Reservation> findAll();
}