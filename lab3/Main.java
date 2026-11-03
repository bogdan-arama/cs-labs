package lab3;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class Main {
    private static final int MIN_KEY_LENGTH = 7;

    // Force UTF-8 encoding on the output stream
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void main(String[] args) {
        ConsoleInput.prepareConsole();

        out.println("==================================================");
        out.println("  LABORATORY WORK NO. 3: PLAYFAIR CIPHER (ROMANIAN)");
        out.println("==================================================");

        try {
            while (true) {
                out.println("\nSelect Operation:");
                out.println("1. Encrypt");
                out.println("2. Decrypt");
                out.println("0. Exit");
                out.print("Choice: ");

                String choice = ConsoleInput.readLine().trim();

                if (choice.equals("0")) {
                    out.println("Exiting application.");
                    break;
                } else if (choice.equals("1")) {
                    runEncrypt();
                } else if (choice.equals("2")) {
                    runDecrypt();
                } else {
                    out.println("[Error] Invalid option. Please enter 0, 1, or 2.");
                }
            }
        } catch (NoSuchElementException e) {
            out.println("\nInput closed. Exiting application.");
        }
    }

    private static void runEncrypt() {
        out.println("\n--- Playfair Encryption ---");
        PlayfairMatrix matrix = readMatrix();
        String text = readMessage();
        String prepared = PlayfairCipher.prepare(text);
        String encrypted = PlayfairCipher.encrypt(prepared, matrix);

        out.println("\n[Result] Normalized Plaintext : " + text);
        out.println("[Result] Prepared Pairs       : " + PlayfairCipher.pairs(prepared));
        out.println("[Result] Ciphertext Pairs     : " + PlayfairCipher.pairs(encrypted));
        out.println("[Result] Ciphertext           : " + encrypted);
    }

    private static void runDecrypt() {
        out.println("\n--- Playfair Decryption ---");
        PlayfairMatrix matrix = readMatrix();
        String text = readCiphertext();
        String decrypted = PlayfairCipher.decrypt(text, matrix);

        out.println("\n[Result] Normalized Ciphertext: " + text);
        out.println("[Result] Decrypted Pairs      : " + PlayfairCipher.pairs(decrypted));
        out.println("[Result] Decrypted Plaintext  : " + decrypted);
        out.println("[Info] Remove the filler letters, restore J where needed and add the spaces manually.");
    }

    private static PlayfairMatrix readMatrix() {
        String key = readKey();
        PlayfairMatrix matrix = PlayfairMatrix.forRomanian(key);

        out.println("\n[Key without repeated letters]: " + matrix.distinctKey());
        out.println("[Encryption Matrix " + matrix.rows() + "x" + matrix.cols() + "]:");
        out.print(matrix);
        out.println();

        return matrix;
    }

    private static String readKey() {
        while (true) {
            out.print("Enter key (>= " + MIN_KEY_LENGTH + " letters): ");
            String input = ConsoleInput.readLine();

            try {
                RomanianAlphabet.validateText(input);
            } catch (IllegalArgumentException e) {
                out.println("[Error] " + e.getMessage());
                continue;
            }

            String normalized = RomanianAlphabet.normalizeText(input);
            if (normalized.length() >= MIN_KEY_LENGTH) {
                return normalized;
            }
            out.println("[Error] The key must have at least " + MIN_KEY_LENGTH
                    + " letters. Provided length: " + normalized.length());
        }
    }

    private static String readMessage() {
        while (true) {
            out.print("Enter plaintext: ");
            String input = ConsoleInput.readLine();

            try {
                RomanianAlphabet.validateText(input);
            } catch (IllegalArgumentException e) {
                out.println("[Error] " + e.getMessage());
                continue;
            }

            String normalized = RomanianAlphabet.normalizeText(input);
            if (!normalized.isEmpty()) {
                return normalized;
            }
            out.println("[Error] Input text cannot be empty.");
        }
    }

    private static String readCiphertext() {
        while (true) {
            out.print("Enter ciphertext: ");
            String input = ConsoleInput.readLine();

            try {
                RomanianAlphabet.validateText(input);
                String normalized = RomanianAlphabet.normalizeText(input);
                if (normalized.isEmpty()) {
                    out.println("[Error] Input text cannot be empty.");
                    continue;
                }
                PlayfairCipher.validateCiphertext(normalized);
                return normalized;
            } catch (IllegalArgumentException e) {
                out.println("[Error] " + e.getMessage());
            }
        }
    }
}
