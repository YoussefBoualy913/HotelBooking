package view;

import exception.UnauthorizedException;
import model.Reservation;
import model.RoomType;
import model.User;
import repository.RoomRepository;
import service.AuthService;
import service.AuthServiceReservationService;
import service.RoomService;
import service.UserService;
import util.InputUtils;

import java.math.BigDecimal;
import java.util.List;

public class AdminView {
    private final UserService userService;
    private final RoomService roomService;
    private final InputUtils inputUtils;
    private final AuthService authService;
    private final RoomRepository roomRepository;
    private final AuthServiceReservationService authServiceReservationService;

    public AdminView(AuthService authService,
                     RoomService roomService,
                     InputUtils inputUtils,
                     UserService userService,
                     AuthServiceReservationService authServiceReservationService,
                     RoomRepository roomRepository) {
        this.userService = userService;
        this.roomService = roomService;
        this.inputUtils = inputUtils;
        this.authService = authService;
        this.authServiceReservationService = authServiceReservationService;
        this.roomRepository = roomRepository;

    }
    public  void showAdminMenu() {
        System.out.println("========================");
        System.out.println("     Administrateur");
        System.out.println("========================");
        System.out.println("1. Gestion des room");
        System.out.println("2. Gestion des reservations");
        System.out.println("3. Gestion des Users");
        System.out.println("4. Gestion de Profile");
        System.out.println("5. logout");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }
    public  void showRoomMenu() {
        System.out.println("========================");
        System.out.println("     Ecpace Room");
        System.out.println("========================");
        System.out.println("1. Search available rooms");
        System.out.println("2. View all rooms");
        System.out.println("3. Create room");
        System.out.println("4. update room");
        System.out.println("5. change room status");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }
    public  void showReservationMenu() {
        System.out.println("========================");
        System.out.println("     Ecpace Reservations");
        System.out.println("========================");
        System.out.println("1. My reservations");
        System.out.println("2. View all reservations");
        System.out.println("3. Create reservation");
        System.out.println("4. update reservation");
        System.out.println("5. Reservation details");
        System.out.println("6. cancel reservation");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }
    public  void showUsersMenu() {
        System.out.println("========================");
        System.out.println("     Ecpace Users");
        System.out.println("========================");
        System.out.println("1. View all Users");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }
    public  void showProfileMenu() {
        System.out.println("========================");
        System.out.println("     Profile");
        System.out.println("========================");
        System.out.println("1. update profile");
        System.out.println("2. Change password");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    public  void updateRoom() {
        String roomNumber = inputUtils.readString("Enter Room Number: ");

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

        try {
            roomService.updateRoom(
                    roomNumber,
                    roomType,
                    capacity,
                    pricePerNight
            );

            System.out.println("Room updated successfully.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public boolean show() {
        while (true) {

            showAdminMenu();
            int choice0 = inputUtils.readInt("");

            switch (choice0) {

                case 1:
                    while (true) {
                        showRoomMenu();
                        int choice1 = inputUtils.readInt("");
                        switch (choice1) {
                            case 1:
                                try {
                                    roomService.searchAvailableRooms();
                                }catch(Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 2:
                                try {
                                    roomService.showRooms();
                                }catch(Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                 break;
                            case 3:
                                try {
                                    roomService.createRoom();
                                }catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 4:
                                try {
                                    try {
                                        updateRoom();
                                    }catch(Exception e) {
                                        System.out.println(e.getMessage());
                                    }

                                }catch(Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 5:
                                String roomNumber = inputUtils.readString("Room number: ");

                                try {
                                    roomService.putRoomInMaintenance(roomNumber);
                                    System.out.println("Room placed in maintenance.");
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                               break;
                            case 0:
                                return true;
                            default:
                                System.out.println("Wrong choice");
                        }
                    }

                case 2:
                    while (true) {
                        showReservationMenu();
                        int choice2 = inputUtils.readInt("");
                        switch (choice2) {
                            case 1:
                                  try {
                                      List<Reservation> reservations =
                                              authServiceReservationService.getMyReservations(authService);

                                      if (reservations.isEmpty()) {
                                          System.out.println("You have no reservations.");
                                      } else {
                                          System.out.println("You have " + reservations.size() + " reservations.");
                                          reservations.forEach(System.out::println);
                                      }
                                  }catch (Exception e) {
                                      System.out.println(e.getMessage());
                                  }
                                  break;
                            case 2:
                                try {
                                    List<Reservation> reservations = authServiceReservationService.getAllReservations();

                                    if (reservations.isEmpty()) {
                                        System.out.println("No reservation found.");
                                    } else {
                                        reservations.forEach(System.out::println);
                                    }
                                }catch(Exception e) {
                                    System.out.println(e.getMessage());
                                }
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
                                try {
                                   authServiceReservationService.updateReservation(authService,roomRepository);
                                   System.out.println("Reservation Updated");
                                }catch (Exception e){
                                    System.out.println("errur:"+e.getMessage());
                                }
                            case 5:
                                try {
                                    authServiceReservationService.reservationDetailes(authService);
                                }catch (Exception e){
                                    System.out.println("errur:"+e.getMessage());
                                }
                                break;
                            case 6:
                                try {
                                    authServiceReservationService.cancelReservation();
                                    System.out.println("Reservation Cancelled");
                                }catch (Exception e){
                                    System.out.println("errur:"+e.getMessage());
                                }
                                break;
                            case 0:
                                return true;
                            default:
                                System.out.println("Wrong choice");
                        }
                    }

                case 3:
                    while (true) {
                        showUsersMenu();
                        int choice3 = inputUtils.readInt("");
                        switch (choice3) {
                            case 1:
                                try {
                                    List<User> users = userService.getAllUsers();

                                    if (users.isEmpty()) {
                                        System.out.println("No users found.");
                                    } else {
                                        users.forEach(System.out::println);
                                    }

                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                                break;
                            case 0:
                                return true;
                            default:
                                System.out.println("Wrong choice");
                        }
                    }
                case 4:
                    while (true) {
                        showProfileMenu();
                        int choice2 = inputUtils.readInt("");
                        switch (choice2) {
                            case 1:
                                try {
                                    userService.updateProfile();
                                }catch(Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 2:
                                try {
                                    userService.changePassword();
                                }catch(Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 0:
                                return true;
                            default:
                                System.out.println("Wrong choice");
                        }
                    }
                case 5:
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
