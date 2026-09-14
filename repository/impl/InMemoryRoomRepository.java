package repository.impl;
import java.util.*;
import model.Room;
import repository.RoomRepository;
public class InMemoryRoomRepository implements RoomRepository {

    private HashMap<String, Room> rooms = new HashMap<>();

    @Override
    public void save(Room room) {
        rooms.put(room.getRoomNumber(), room);
    }

    @Override
    public Optional<Room> findByRoomNumber(String roomNumber) {

        return Optional.ofNullable(rooms.get(roomNumber));
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }
}