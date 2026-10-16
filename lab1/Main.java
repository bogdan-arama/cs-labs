package lab1;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    // Force UTF-8 encoding on both input and output streams
    private static final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void main(String[] args) {
        out.println("==================================================");
        out.println("   LABORATORY WORK NO. 1: CAESAR CIPHER (ROMANIAN)");
        out.println("==================================================");

        while (true) {
            out.println("\nSelect Task:");
            out.println("1. Task 1.1 - Standard Caesar Cipher (1 Key)");
            out.println("2. Task 1.2 - Caesar Cipher with Permutation (2 Keys)");
            out.println("0. Exit");
            out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                out.println("Exiting application.");
                break;
            } else if (choice.equals("1")) {
                runTask1();
            } else if (choice.equals("2")) {
                runTask2();
            } else {
                out.println("[Error] Invalid option. Please enter 0, 1, or 2.");
            }
        }
    }

    private static void runTask1() {
        out.println("\n--- Task 1.1: Standard Caesar Cipher ---");
        int op = readOperation();
        int key = readKey("Enter shift key k (1-30): ");
        String text = readValidText(op == 1 ? "Enter plaintext: " : "Enter ciphertext: ");

        String alphabet = RomanianAlphabet.ALPHABET_STRING;
        if (op == 1) {
            String encrypted = CaesarCipher.encrypt(text, key, alphabet);
            out.println("\n[Result] Normalized Plaintext : " + text);
            out.println("[Result] Ciphertext           : " + encrypted);
        } else {
            String decrypted = CaesarCipher.decrypt(text, key, alphabet);
            out.println("\n[Result] Normalized Ciphertext: " + text);
            out.println("[Result] Decrypted Plaintext  : " + decrypted);
        }
    }

    private static void runTask2() {
        out.println("\n--- Task 1.2: Caesar Cipher with Permutation ---");
        int op = readOperation();
        int key1 = readKey("Enter shift key k1 (1-30): ");
        String key2 = readKeyword();

        String permutedAlphabet = RomanianAlphabet.buildPermutedAlphabet(key2);
        out.println("\n[Generated Permuted Alphabet]:");
        out.println(permutedAlphabet);

        String text = readValidText(op == 1 ? "Enter plaintext: " : "Enter ciphertext: ");

        if (op == 1) {
            String encrypted = CaesarCipher.encrypt(text, key1, permutedAlphabet);
            out.println("\n[Result] Normalized Plaintext : " + text);
            out.println("[Result] Ciphertext           : " + encrypted);
        } else {
            String decrypted = CaesarCipher.decrypt(text, key1, permutedAlphabet);
            out.println("\n[Result] Normalized Ciphertext: " + text);
            out.println("[Result] Decrypted Plaintext  : " + decrypted);
        }
    }

    private static int readOperation() {
        while (true) {
            out.print("Select operation (1 = Encrypt, 2 = Decrypt): ");
            String input = scanner.nextLine().trim();
            if (input.equals("1")) return 1;
            if (input.equals("2")) return 2;
            out.println("[Error] Please choose 1 (Encrypt) or 2 (Decrypt).");
        }
    }

    private static int readKey(String prompt) {
        while (true) {
            out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int key = Integer.parseInt(input);
                if (key >= 1 && key <= 30) {
                    return key;
                }
                out.println("[Error] The key must be an integer between 1 and 30 inclusive.");
            } catch (NumberFormatException e) {
                out.println("[Error] Invalid number. The key must be an integer between 1 and 30 inclusive.");
            }
        }
    }

    private static String readKeyword() {
        while (true) {
            out.print("Enter Key 2 (keyword, >= 7 letters): ");
            String input = scanner.nextLine();
            String normalized = RomanianAlphabet.normalizeText(input);

            if (normalized.length() < 7) {
                out.println("[Error] Keyword must be at least 7 letters long. Provided length: " + normalized.length());
                continue;
            }

            try {
                RomanianAlphabet.validateText(normalized);
                return normalized;
            } catch (IllegalArgumentException e) {
                out.println("[Error] " + e.getMessage());
            }
        }
    }

    private static String readValidText(String prompt) {
        while (true) {
            out.print(prompt);
            String rawInput = scanner.nextLine();
            String normalized = RomanianAlphabet.normalizeText(rawInput);

            if (normalized.isEmpty()) {
                out.println("[Error] Input text cannot be empty.");
                continue;
            }

            try {
                RomanianAlphabet.validateText(normalized);
                return normalized;
            } catch (IllegalArgumentException e) {
                out.println("[Error] " + e.getMessage());
            }
        }
    }
}