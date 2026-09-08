public interface RoomRepository {
    void save(Room room);
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findAll()
}