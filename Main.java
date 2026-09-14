import model.Reservation;
import model.User;
import repository.impl.InMemoryReservationRepository;
import repository.impl.InMemoryRoomRepository;
import repository.impl.InMemoryUserRepository;
import service.AuthService;
import service.AuthServiceReservationService;
import service.RoomService;
import util.Initializer;
import util.InputUtils;
import view.AdminView;
import view.ClientView;
import view.GuestView;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryRoomRepository roomRepository = new InMemoryRoomRepository();
        InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();
        AuthService authService = new AuthService(userRepository);
        AuthServiceReservationService authServiceReservationService = new AuthServiceReservationService(reservationRepository);
        InputUtils inputUtils = new InputUtils();
        RoomService roomService = new RoomService(roomRepository, reservationRepository, inputUtils);
        Initializer.initializeUsers(userRepository);
        Initializer.initializeRooms(roomRepository);
        Initializer.initializeReservations(reservationRepository, userRepository, roomRepository);
        authServiceReservationService.updateExpiredReservations(reservationRepository);
        GuestView guestView = new GuestView(authService, inputUtils);
        ClientView clientView = new ClientView(authService,
                roomService,authServiceReservationService,roomRepository,inputUtils);
        AdminView adminView = new AdminView(authService,roomService,inputUtils);
        authService.autoLogin();

        boolean running = true;

        while (running) {

            User user = authService.getCurrentUser();

            if (user == null) {
                running = guestView.show();
            } else if (user.isAdmin()) {
                running = adminView.show();
            } else {
                running = clientView.show(user);
            }
        }


    }
}