package lab2;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    // Force UTF-8 encoding on both input and output streams
    private static final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private static final String[] DEFAULT_FILES = {"lab2/ciphertext_v1.txt", "ciphertext_v1.txt"};

    private static String cipherText = "";
    private static final SubstitutionKey key = new SubstitutionKey();

    public static void main(String[] args) {
        out.println("==================================================");
        out.println("   LABORATORY WORK NO. 2: FREQUENCY ANALYSIS");
        out.println("   OF MONOALPHABETIC CIPHERS (VARIANT 1)");
        out.println("==================================================");

        loadDefaultCipherText(args);

        while (true) {
            out.println("\nSelect Action:");
            out.println("1. Show ciphertext");
            out.println("2. Letter frequency analysis (vs. English, table 2.2)");
            out.println("3. Frequent words, digraphs, trigraphs and doubles");
            out.println("4. Naive substitution by frequency rank");
            out.println("5. Add substitution (e.g. W=t)");
            out.println("6. Remove substitution");
            out.println("7. Show current decryption and key");
            out.println("8. Replay the Variant 1 attack step by step");
            out.println("9. Load ciphertext from file");
            out.println("10. Reset key");
            out.println("0. Exit");
            out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0" -> {
                    out.println("Exiting application.");
                    return;
                }
                case "1" -> out.println("\n" + cipherText);
                case "2" -> showLetterFrequencies();
                case "3" -> showPatterns();
                case "4" -> showNaiveSubstitution();
                case "5" -> addSubstitutions();
                case "6" -> removeSubstitution();
                case "7" -> showCurrentState();
                case "8" -> replayAttack();
                case "9" -> loadFromFile();
                case "10" -> {
                    key.clear();
                    out.println("[Info] Key cleared.");
                }
                default -> out.println("[Error] Invalid option. Please enter a number from 0 to 10.");
            }
        }
    }

    private static void loadDefaultCipherText(String[] args) {
        if (args.length > 0 && readFile(args[0])) {
            return;
        }
        for (String file : DEFAULT_FILES) {
            if (readFile(file)) {
                return;
            }
        }
        out.println("[Warning] Default ciphertext not found. Use option 9 to load a file.");
    }

    private static boolean readFile(String file) {
        Path path = Path.of(file);
        if (!Files.isRegularFile(path)) {
            return false;
        }
        try {
            String text = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (new FrequencyAnalyzer(text).total() == 0) {
                out.println("[Error] The file contains no letters.");
                return false;
            }
            cipherText = text;
            key.clear();
            out.println("[Info] Loaded ciphertext from " + path.toAbsolutePath()
                    + " (" + new FrequencyAnalyzer(text).total() + " letters).");
            return true;
        } catch (IOException e) {
            out.println("[Error] Could not read file: " + e.getMessage());
            return false;
        }
    }

    private static void loadFromFile() {
        out.print("Enter path to ciphertext file: ");
        String file = scanner.nextLine().trim();
        if (!readFile(file)) {
            out.println("[Error] File not loaded. Keeping the previous ciphertext.");
        }
    }

    private static void showLetterFrequencies() {
        FrequencyAnalyzer fa = new FrequencyAnalyzer(cipherText);
        out.println("\n--- Letter frequencies (" + fa.total() + " letters) ---");
        out.println("Rank  Cipher  Count      %   Histogram                  | English      %   Histogram");

        // Both columns are sorted by frequency, so each row pairs letters of the same rank
        String cipherOrder = fa.lettersByFrequency();
        String englishOrder = EnglishFrequency.lettersByFrequency();
        for (int i = 0; i < cipherOrder.length(); i++) {
            char c = cipherOrder.charAt(i);
            char e = englishOrder.charAt(i);
            out.printf("%3d     %c   %5d  %6.2f   %-26s |    %c    %6.2f   %s%n",
                    i + 1, c, fa.count(c), fa.percent(c), bar(fa.percent(c)),
                    e, EnglishFrequency.percent(e), bar(EnglishFrequency.percent(e)));
        }

        out.println("\nCiphertext letters by frequency : " + fa.lettersByFrequency());
        out.println("English letters by frequency    : " + EnglishFrequency.lettersByFrequency());
    }

    private static String bar(double percent) {
        return "#".repeat((int) Math.round(percent * 2)); // one '#' = 0.5%
    }

    private static void showPatterns() {
        FrequencyAnalyzer fa = new FrequencyAnalyzer(cipherText);
        out.println("\n--- Frequent patterns in the ciphertext ---");
        out.println("1-letter words : " + format(fa.topWords(1, 5)));
        out.println("2-letter words : " + format(fa.topWords(2, 8)));
        out.println("3-letter words : " + format(fa.topWords(3, 8)));
        out.println("4-letter words : " + format(fa.topWords(4, 8)));
        out.println("Digraphs       : " + format(fa.topNgrams(2, 12)));
        out.println("Trigraphs      : " + format(fa.topNgrams(3, 12)));
        out.println("Double letters : " + format(fa.topDoubles(6)));
        out.println("\nEnglish digraphs  : " + String.join(" ", EnglishFrequency.DIGRAPHS));
        out.println("English trigraphs : " + String.join(" ", EnglishFrequency.TRIGRAPHS));
        out.println("English doubles   : SS EE TT OO FF");
    }

    private static String format(List<Map.Entry<String, Integer>> entries) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : entries) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(e.getKey()).append("(").append(e.getValue()).append(")");
        }
        return sb.toString();
    }

    /**
     * Replaces the i-th most frequent ciphertext letter with the i-th most frequent English letter,
     * to show that pure frequency ranking is not enough to break the cipher.
     */
    private static void showNaiveSubstitution() {
        FrequencyAnalyzer fa = new FrequencyAnalyzer(cipherText);
        String cipherOrder = fa.lettersByFrequency();
        String englishOrder = EnglishFrequency.lettersByFrequency();

        SubstitutionKey naive = new SubstitutionKey();
        for (int i = 0; i < cipherOrder.length(); i++) {
            naive.set(cipherOrder.charAt(i), englishOrder.charAt(i));
        }

        out.println("\n--- Naive substitution by frequency rank ---");
        out.println("Cipher  : " + cipherOrder);
        out.println("English : " + englishOrder.toLowerCase());
        out.println("\n" + preview(naive.apply(cipherText)));
        out.println("\n[Info] The result is unreadable: real texts deviate from the average frequencies,");
        out.println("       so each guess has to be confirmed with words, digraphs and context.");
    }

    private static void addSubstitutions() {
        out.print("Enter substitutions CIPHER=plain separated by spaces (e.g. V=e W=t): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            out.println("[Error] Nothing entered.");
            return;
        }

        for (String pair : input.split("\\s+")) {
            if (!pair.matches("[A-Za-z]=[A-Za-z]")) {
                out.println("[Error] '" + pair + "' is not in the form X=y.");
                continue;
            }
            try {
                key.set(pair.charAt(0), pair.charAt(2));
                out.println("[OK] " + Character.toUpperCase(pair.charAt(0)) + " -> " + Character.toLowerCase(pair.charAt(2)));
            } catch (IllegalArgumentException e) {
                out.println("[Error] " + e.getMessage());
            }
        }
        out.println("\n" + preview(key.apply(cipherText)));
    }

    private static void removeSubstitution() {
        out.print("Enter ciphertext letter to remove: ");
        String input = scanner.nextLine().trim();
        if (input.length() != 1 || EnglishFrequency.index(input.charAt(0)) < 0) {
            out.println("[Error] Please enter a single letter A-Z.");
            return;
        }
        if (!key.isMapped(input.charAt(0))) {
            out.println("[Error] Letter " + input.toUpperCase() + " has no substitution.");
            return;
        }
        key.remove(input.charAt(0));
        out.println("[OK] Removed substitution for " + input.toUpperCase() + ".");
    }

    private static void showCurrentState() {
        out.println("\n--- Current partial decryption (" + key.size() + " letters known) ---");
        out.println("(lowercase = recovered plaintext, UPPERCASE = still encrypted)\n");
        out.println(key.apply(cipherText));
        printKeyTable();

        if (isFullyDecrypted()) {
            out.println("\n--- Recovered plaintext ---\n");
            out.println(key.decrypt(cipherText));
        }
    }

    private static void printKeyTable() {
        out.println("\nReconstructed alphabet (table 2.5):");
        out.println("Plaintext  : " + spaced(EnglishFrequency.ALPHABET.toLowerCase()));
        out.println("Ciphertext : " + spaced(key.cipherAlphabet()));
    }

    private static String spaced(String s) {
        return String.join(" ", s.split(""));
    }

    private static boolean isFullyDecrypted() {
        for (char c : cipherText.toCharArray()) {
            if (EnglishFrequency.index(c) >= 0 && !key.isMapped(c)) {
                return false;
            }
        }
        return true;
    }

    private static void replayAttack() {
        key.clear();
        out.println("\n--- Frequency analysis attack on Variant 1 ---");
        showLetterFrequencies();

        int n = VariantOneAttack.STEPS.size();
        for (int i = 0; i < n; i++) {
            VariantOneAttack.Step step = VariantOneAttack.STEPS.get(i);
            out.print("\nPress Enter for step " + (i + 1) + "/" + n + " (or q to stop)... ");
            if (scanner.nextLine().trim().equalsIgnoreCase("q")) {
                out.println("[Info] Replay stopped. The key keeps the substitutions applied so far.");
                return;
            }

            VariantOneAttack.apply(step, key);
            out.println("\n=== Step " + (i + 1) + ": " + step.substitutions().replace("=", " -> ") + " ===");
            out.println(step.reasoning());
            out.println("\n" + key.apply(cipherText));
        }

        printKeyTable();
        out.println("\n--- Recovered plaintext ---\n");
        out.println(key.decrypt(cipherText));
    }

    private static String preview(String text) {
        return text.length() <= 400 ? text : text.substring(0, 400) + " ...";
    }
}
