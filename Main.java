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

import java.util.List;

public class Main {

    private static void showGuestMenu() {
        System.out.println("========================");
        System.out.println("     HOTEL BOOKING");
        System.out.println("========================");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private static void showUserMenu(User user) {
        System.out.println("================================");
        System.out.println("Logged in as: " + user.getFullName());
        System.out.println("================================");
        System.out.println("1. Search available rooms");
        System.out.println("2. View all rooms");
        System.out.println("3. Create reservation");
        System.out.println("4. My reservations");
        System.out.println("5. Reservation details");
        System.out.println("6. Update reservation");
        System.out.println("7. Cancel reservation");
        System.out.println("8. Update profile");
        System.out.println("9. Change password");
        System.out.println("10. Logout");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }


    public static void main(String[] args) {

        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryRoomRepository  roomRepository = new InMemoryRoomRepository();
        InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();
        AuthService authService = new AuthService(userRepository);
        AuthServiceReservationService authServiceReservationService = new AuthServiceReservationService(reservationRepository);
        InputUtils inputUtils = new InputUtils();
        RoomService roomService = new RoomService(roomRepository,reservationRepository,inputUtils);
        Initializer.initializeUsers(userRepository);
        Initializer.initializeRooms(roomRepository);
        authService.autoLogin();

        while (true) {
            if(authService.getCurrentUser() == null){
                showGuestMenu();
                int choix = inputUtils.readInt("");
                switch (choix) {
                    case 1:
                        try {
                            authService.register();
                        }catch (Exception e){
                            System.out.println("errur:"+e.getMessage());
                        }

                    break;
                    case 2:
                        try {
                            authService.login();
                        }catch (Exception e){
                            System.out.println("errur:"+e.getMessage());
                        }

                    break;
                    case 0:
                     return;
                    default:
                     System.out.println("Wrong choice");
                     break;
                }
            }else {
                showUserMenu(authService.getCurrentUser());
                int choix = inputUtils.readInt("");
                switch (choix) {
                    case 1:
                        roomService.searchAvailableRooms();
                    break;
                    case 2:

                        roomService.showRooms();
                        break;
                    case 3:
                        try {
                            authServiceReservationService.createReservation(authService.getCurrentUser(),roomRepository);
                            System.out.println("Reservation Created");
                        }catch (Exception e){
                            System.out.println("errur:"+e.getMessage());
                        }
                        break;
                    case 4:
                        List<Reservation> reservations =
                                authServiceReservationService.getMyReservations(authService);

                        if (reservations.isEmpty()) {
                            System.out.println("You have no reservations.");
                        } else {
                            System.out.println("You have " + reservations.size() + " reservations.");
                            reservations.forEach(System.out::println);
                        }
                        break;
                    case 7:
                        try {
                            authServiceReservationService.cancelReservation();
                            System.out.println("Reservation Cancelled");
                        }catch (Exception e){
                            System.out.println("errur:"+e.getMessage());
                        }
                        break;
                    case 8:
                        try {
                            authService.updateProfile();
                        }catch (Exception e){
                            System.out.println("errur:"+e.getMessage());
                        }
                        break;
                    case 9:
                        try {
                            authService.changePassword();
                        }catch (Exception e){
                            System.out.println("errur:"+e.getMessage());
                        }
                        break;
                    case 10:
                        authService.logout();
                        break;
                    case 0:
                            return;
                    default:
                        System.out.println("Wrong choice");
                }
            }

        }
    }
}