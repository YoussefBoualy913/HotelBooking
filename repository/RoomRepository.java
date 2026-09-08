package repository;
import model.Room;
import java.util.Optional;
import java.util.List;
public interface RoomRepository {
    void save(Room room);
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findAll();
}