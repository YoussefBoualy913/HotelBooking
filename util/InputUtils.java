package util;
import java.util.Scanner;
public class InputUtils {

    private final Scanner scanner;

    public InputUtils() {
        this.scanner = new Scanner(System.in);
    }

    public String readString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public int readInt(String message) {
        System.out.print(message);
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}