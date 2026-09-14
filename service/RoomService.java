package service;
import exception.RoomNotFoundException;
import model.Room;
import model.RoomStatus;
import model.RoomType;
import repository.ReservationRepository;
import repository.RoomRepository;
import util.InputUtils;
import util.Roomutils;
import util.ValidationUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RoomService   {
    private RoomRepository roomRepository;
    private Roomutils roomutils;
    private InputUtils inputUtils;
    private ReservationRepository reservationRepository;
    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository, InputUtils inputUtils) {

        this.roomRepository = roomRepository;
       this.reservationRepository = reservationRepository;
       this.inputUtils = inputUtils;
       this.roomutils = new Roomutils(reservationRepository);
    }

    public void showRooms(){
        List<Room> rooms = roomRepository.findAll();

        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            rooms.forEach(room -> {
                System.out.println(room);
            });
        }
    }
    public Room createRoom(){
        String roomNumber;
       while (true) {
            roomNumber = inputUtils.readString("Enter Room Number: ");
            try {
                if (roomRepository.findByRoomNumber(roomNumber).isPresent()) {
                    throw new IllegalArgumentException("Room number already exists.");
                }
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                continue;
            }
           break;
       }
       int capacity = Integer.parseInt(inputUtils.readString("Enter Room Capacity: "));
        RoomType roomType ;
        if(capacity <1){
            throw new IllegalArgumentException("Room Capacity must be greater than 0.");
        }else if (capacity == 1) {
            roomType = RoomType.SINGLE;
        } else if (capacity == 2) {
            roomType = RoomType.DOUBLE;
        }else {
            roomType = RoomType.SUITE;
        }

        BigDecimal pricePerNight = new BigDecimal(inputUtils.readString("Enter Room price Per night: "));

        Room room = new Room(roomNumber,roomType,capacity,pricePerNight,RoomStatus.AVAILABLE);
        roomRepository.save(room);
        return room;
    }

    public void updateRoom(
            String roomNumber,
            RoomType type,
            int capacity,
            BigDecimal pricePerNight
    ) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() ->
                        new RoomNotFoundException("Room not found"));

        room.setType(type);
        room.setCapacity(capacity);
        room.setPricePerNight(pricePerNight);

        roomRepository.save(room);
    }
    public void putRoomInMaintenance(String roomNumber) {

        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() ->
                        new RoomNotFoundException("Room not found"));

        room.setStatus(RoomStatus.MAINTENANCE);

        roomRepository.save(room);
    }

    public void searchAvailableRooms(){
        LocalDate checkIn;
        LocalDate checkOut;
        while (true) {
             checkIn = inputUtils.readDate("Check-in (YYYY-MM-DD): ");
             checkOut = inputUtils.readDate("Check-out (YYYY-MM-DD): ");

            if (!ValidationUtils.isValidDateRange(checkIn, checkOut)) {
                System.out.println("Check-in must be before check-out.");
                continue;
            }
            break;
        }
        LocalDate finalCheckIn = checkIn;
        LocalDate finalCheckOut = checkOut;


        int guestsNumber;

        do {
            guestsNumber = inputUtils.readInt("Number of guests: ");

            if (guestsNumber < 1) {
                System.out.println("Number of guests must be at least 1.");
            }

        } while (guestsNumber < 1);
        final int finalGuestsNumber = guestsNumber;


        List<Room> availableRooms = roomRepository.findAll()
                .stream()
                .filter(room ->
                        room.getStatus() == RoomStatus.AVAILABLE
                                && roomutils.isRoomAvailable(
                                room.getRoomNumber(),
                                finalCheckIn,
                                finalCheckOut,
                                null
                        )
                                && finalGuestsNumber <= room.getCapacity()
                )
                .toList();

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for the selected period.");

        } else {
            availableRooms.forEach(System.out::println);
        }
    }

}