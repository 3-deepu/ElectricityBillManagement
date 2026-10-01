package electricity.util;

import electricity.exception.InvalidInputException;

import java.util.Scanner;

public final class InputUtil {
    private static final Scanner SC = new Scanner(System.in);

    private InputUtil() {}

    public static String readRequired(String prompt) {
        while (true) {
            System.out.print(Colors.YELLOW + prompt + Colors.RESET);
            String value = SC.nextLine().trim();
            if (!value.isEmpty()) return value;
            Colors.error("Input cannot be empty.");
        }
    }

    public static String readLine(String prompt) {
        System.out.print(Colors.YELLOW + prompt + Colors.RESET);
        return SC.nextLine();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                String value = readRequired(prompt);
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                Colors.error("Enter a valid integer.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            try {
                String value = readRequired(prompt);
                double n = Double.parseDouble(value);
                if (!Double.isFinite(n)) throw new NumberFormatException();
                return n;
            } catch (NumberFormatException e) {
                Colors.error("Enter a valid number.");
            }
        }
    }

    public static String readMobile() {
        while (true) {
            String mobile = readRequired("Mobile number: ");
            if (mobile.matches("\\d{10}")) return mobile;
            Colors.error("Mobile number must contain exactly 10 digits.");
        }
    }

    public static int readChoice(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) return value;
            Colors.error("Choose a value from " + min + " to " + max + ".");
        }
    }

    public static void pause() {
        System.out.println();
        readLine("Press ENTER to continue...");
    }
}
