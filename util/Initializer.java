package util;

import model.*;
import repository.ReservationRepository;
import repository.RoomRepository;
import repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Initializer {

    public static void initializeUsers(UserRepository userRepository) {

        User user1 = new User(
                "Youssef boualy",
                "y@gmail.com",
                "0612345678",
                "pass12"
        );
        user1.setRole(UserRole.ADMIN);
        User user2 = new User(
                "Bob Smith",
                "bob@gmail.com",
                "0623456789",
                "password456"
        );

        User user3 = new User(
                "sara Smith",
                "sara@gmail.com",
                "0623456789",
                "password45"
        );

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    public static void initializeRooms(RoomRepository roomRepository) {

        roomRepository.save(
                new Room(
                        "101",
                        RoomType.SINGLE,
                        1,
                        new BigDecimal("300.00"),
                        RoomStatus.AVAILABLE
                )
        );

        roomRepository.save(
                new Room(
                        "102",
                        RoomType.DOUBLE,
                        2,
                        new BigDecimal("500.00"),
                        RoomStatus.AVAILABLE
                )
        );

        roomRepository.save(
                new Room(
                        "201",
                        RoomType.SUITE,
                        4,
                        new BigDecimal("1000.00"),
                        RoomStatus.AVAILABLE
                )
        );
    }

    public static void initializeReservations(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            RoomRepository roomRepository
    ) {
        User user1 = userRepository.findByEmail("y@gmail.com")
                .orElseThrow();

        User user2 = userRepository.findByEmail("sara@gmail.com")
                .orElseThrow();

        Room room101 = roomRepository.findByRoomNumber("101")
                .orElseThrow();

        Room room102 = roomRepository.findByRoomNumber("102")
                .orElseThrow();

        Room room201 = roomRepository.findByRoomNumber("201")
                .orElseThrow();


        Reservation reservation1 = new Reservation(
                user1.getId(),
                "RES-2026-0001"
                ,
                room101.getRoomNumber(),
                LocalDate.of(2026, 9, 20),
                LocalDate.of(2026, 9, 23),
                1,
                3,
                room101.getPricePerNight()
                        .multiply(BigDecimal.valueOf(3))
        );


        reservationRepository.save(reservation1);



        Reservation reservation2 = new Reservation(
                user2.getId(),
                "RES-2026-0002",
                room102.getRoomNumber(),
                LocalDate.of(2026, 9, 25),
                LocalDate.of(2026, 9, 28),
                2,
                3,
                room102.getPricePerNight()
                        .multiply(BigDecimal.valueOf(3))
        );


        reservationRepository.save(reservation2);



        Reservation reservation3 = new Reservation(
                user1.getId(),
                "RES-2026-0003"
                ,
                room201.getRoomNumber(),
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 5),
                2,
                4,
                room201.getPricePerNight()
                        .multiply(BigDecimal.valueOf(4))
        );

        reservation3.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation3);

        Reservation reservation4 = new Reservation(
                user1.getId(),
                "RES-2026-0004",
                room101.getRoomNumber(),
                LocalDate.of(2026, 8, 10),
                LocalDate.of(2026, 8, 13),
                1,
                3,
                room101.getPricePerNight()
                        .multiply(BigDecimal.valueOf(3))
        );

        reservationRepository.save(reservation4);
    }
}