package lab3;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class RomanianAlphabet {
    // 31 letters in standard Romanian order:
    // A, Ă (Ă), Â (Â), B, C, D, E, F, G, H, I, Î (Î),
    // J, K, L, M, N, O, P, Q, R, S, Ș (Ș), T, Ț (Ț), U, V, W, X, Y, Z
    public static final String ALPHABET_STRING = "AĂÂBCDEFGHIÎJKLMNOPQRSȘTȚUVWXYZ";

    // A 5x6 matrix has 30 cells, so J is merged into I, as in the laboratory guide
    public static final String MATRIX_ALPHABET = ALPHABET_STRING.replace("J", "");

    public static final String ALLOWED_RANGE =
            "'A'-'Z', 'a'-'z' and the Romanian letters Ă Â Î Ș Ț / ă â î ș ț";

    private static final String ALLOWED_CHARS = ALPHABET_STRING + ALPHABET_STRING.toLowerCase(Locale.ROOT);

    /**
     * Normalizes text: replaces cedillas with comma-below
     * (Ş/ş -> Ș/ș, Ţ/ţ -> Ț/ț), removes all
     * whitespace and converts to uppercase.
     */
    public static String normalizeText(String input) {
        if (input == null) return "";

        return fixCedillas(input).replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
    }

    /**
     * Validates the raw input, before uppercasing, so that characters such as the
     * German sharp s cannot turn into valid letters. Whitespace is ignored.
     * Throws IllegalArgumentException listing the invalid characters and the correct range.
     */
    public static void validateText(String input) {
        Set<String> invalid = new LinkedHashSet<>();

        for (char c : fixCedillas(input).replaceAll("\\s+", "").toCharArray()) {
            if (ALLOWED_CHARS.indexOf(c) < 0) {
                invalid.add(Character.isISOControl(c) ? String.format("U+%04X", (int) c) : "'" + c + "'");
            }
        }

        if (!invalid.isEmpty()) {
            throw new IllegalArgumentException("Invalid characters: " + String.join(", ", invalid)
                    + ". Allowed range: " + ALLOWED_RANGE + ". Spaces are ignored.");
        }
    }

    private static String fixCedillas(String input) {
        return input.replace('Ş', 'Ș') // Ş -> Ș
                    .replace('ş', 'ș') // ş -> ș
                    .replace('Ţ', 'Ț') // Ţ -> Ț
                    .replace('ţ', 'ț'); // ţ -> ț
    }
}
