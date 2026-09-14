package view;

import model.RoomType;
import repository.RoomRepository;
import service.AuthService;
import service.RoomService;
import util.InputUtils;

import java.math.BigDecimal;

public class AdminView {
    private final AuthService authService;
    private final RoomService roomService;
    private final InputUtils inputUtils;

    public AdminView(AuthService authService,
                     RoomService roomService,
                     InputUtils inputUtils) {
        this.authService = authService;
        this.roomService = roomService;
        this.inputUtils = inputUtils;

    }
    public  void showAdminMenu() {
        System.out.println("========================");
        System.out.println("     Administrateur");
        System.out.println("========================");
        System.out.println("1. Gestion des room");
        System.out.println("2. Gestion des reservations");
        System.out.println("3. Gestion des Client");
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
                    return true;
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
