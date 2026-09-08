import repository.impl.InMemoryUserRepository;
import service.AuthService;
import util.InputUtils;

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

    private static void showUserMenu() {
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
    public static void regester(AuthService authService,InputUtils inputUtils){
        String fullname =  inputUtils.readString("Fullname:");
        String email = inputUtils.readString("Email:");
        String phone = inputUtils.readString("Phone:");
        String password = inputUtils.readString("Password:");
        authService.register(fullname,email,phone,password);
    }
    public static void login(AuthService authService,InputUtils inputUtils){
        String email = inputUtils.readString("Email:");
        String password = inputUtils.readString("Password:");
        try {
            authService.login(email,password);
        }catch (Exception e){
            System.out.println("errur :"+e.getMessage());
        }

    }

    public static void main(String[] args) {

        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        AuthService authService = new AuthService(userRepository);
        InputUtils inputUtils = new InputUtils();
        while (true) {
            if(authService.getCurrentUser() == null){
                showGuestMenu();
                int choix = inputUtils.readInt("");
                switch (choix) {
                    case 1:
                    regester(authService,inputUtils);
                    break;
                    case 2:
                        login(authService,inputUtils);
                    break;
                    case 0:
                     return;
                    default:
                     System.out.println("Wrong choice");
                     break;
                }
            }

        }
    }
}