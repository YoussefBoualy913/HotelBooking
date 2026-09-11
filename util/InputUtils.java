package util;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
public class InputUtils {

    private final Scanner scanner;

    public InputUtils() {
        this.scanner = new Scanner(System.in);
    }

    public String readString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    public int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Number cannot be empty.");
                    continue;
                }

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    public LocalDate readDate(String message) {

        while (true) {
            try {
                System.out.print(message);

                String input = scanner.nextLine();

                return LocalDate.parse(input);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use YYYY-MM-DD.");
            }
        }
    }
}