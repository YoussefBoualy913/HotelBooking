package util;

import model.Room;
import model.RoomStatus;
import model.RoomType;
import repository.RoomRepository;
import model.User;
import repository.UserRepository;
import java.math.BigDecimal;

public class Initializer {

    public static void initializeUsers(UserRepository userRepository) {

        User user1 = new User(
                "Youssef boualy",
                "y@gmail.com",
                "0612345678",
                "pass12"
        );

        User user2 = new User(
                "Bob Smith",
                "bob@gmail.com",
                "0623456789",
                "password456"
        );

        userRepository.save(user1);
        userRepository.save(user2);
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
}