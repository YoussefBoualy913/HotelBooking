package view;

import model.User;
import repository.RoomRepository;
import service.AuthService;
import service.AuthServiceReservationService;
import service.RoomService;
import util.InputUtils;

public class ClientView {
   private final AuthService authService;
   private final RoomService roomService;
   private final AuthServiceReservationService authServiceReservationService;
   private final RoomRepository roomRepository;
   private final InputUtils inputUtils;
   public ClientView(AuthService authService,
                     RoomService roomService,
                     AuthServiceReservationService authServiceReservationService,
                     RoomRepository roomRepository,
                     InputUtils inputUtils
                    ) {
       this.authService = authService;
       this.roomService = roomService;
       this.authServiceReservationService = authServiceReservationService;
       this.inputUtils = inputUtils;
       this.roomRepository = roomRepository;

   }
    public void showClientMenu(User user) {
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

    public boolean show(User user) {
        while (true) {

            showClientMenu(user);

            int choice = inputUtils.readInt("");

            switch (choice) {

                case 1:
                    roomService.searchAvailableRooms();
                    break;

                case 2:
                    roomService.showRooms();
                    return true;

                case 3:
                    try {
                        authServiceReservationService.createReservation(authService.getCurrentUser(),roomRepository);
                        System.out.println("Reservation Created");
                    }catch (Exception e){
                        System.out.println("errur:"+e.getMessage());
                    }
                    return true;
                case 4:
                    try {
                        authServiceReservationService.reservationDetailes(authService);
                    }catch (Exception e){
                        System.out.println("errur:"+e.getMessage());
                    }
                    return true;
                case 5:
                    try {
                        authServiceReservationService.reservationDetailes(authService);
                    }catch (Exception e){
                        System.out.println("errur:"+e.getMessage());
                    }
                    return true;
                case 6:
                    try {
                        authServiceReservationService.updateReservation(authService,roomRepository);
                        System.out.println("Reservation Updated");
                    }catch (Exception e){
                        System.out.println("errur:"+e.getMessage());
                    }
                    return true;
                case 7:
                    try {
                        authServiceReservationService.cancelReservation();
                        System.out.println("Reservation Cancelled");
                    }catch (Exception e){
                        System.out.println("errur:"+e.getMessage());
                    }
                    return true;
                case 8:
                    try {
                        authService.updateProfile();
                    }catch (Exception e){
                        System.out.println("errur:"+e.getMessage());
                    }
                    return true;
                case 9:
                    try {
                        authService.changePassword();
                    }catch (Exception e){
                        System.out.println("errur:"+e.getMessage());
                    }
                    return true;
                case 10:
                    authService.logout();
                    return true;
                case 0:
                    return false;
                default:
                    System.out.println("Wrong choice");
            }
        }
    }

}
