package lab1;

import java.util.*;

public class RomanianAlphabet {
    // 31 letters in standard Romanian order:
    // A, Ă (\u0102), Â (\u00C2), B, C, D, E, F, G, H, I, Î (\u00CE),
    // J, K, L, M, N, O, P, Q, R, S, Ș (\u0218), T, Ț (\u021A), U, V, W, X, Y, Z

    public static final String ALPHABET_STRING = "A\u0102\u00C2BCDEFGHI\u00CEJKLMNOPQRS\u0218T\u021AUVWXYZ";
    public static final int ALPHABET_SIZE = ALPHABET_STRING.length(); // 31

    private static final Map<Character, Integer> CHAR_TO_INDEX = new HashMap<>();
    private static final Map<Integer, Character> INDEX_TO_CHAR = new HashMap<>();

    static {
        for (int i = 0; i < ALPHABET_STRING.length(); i++) {
            char c = ALPHABET_STRING.charAt(i);
            CHAR_TO_INDEX.put(c, i);
            INDEX_TO_CHAR.put(i, c);
        }
    }

    /**
     * Normalizes text: converts to uppercase, replaces cedillas with comma-below
     * (\u015E/\u015F -> \u0218, \u0162/\u0163 -> \u021A), and removes all spaces.
     */


    public static String normalizeText(String input) {
        if (input == null) return "";
        
        // Normalize cedillas (Ş, ş, Ţ, ţ) to standard comma-below (Ș, ț)
        String text = input.replace('\u015E', '\u0218') // Ş -> Ș
                           .replace('\u015F', '\u0219') // ş -> ș
                           .replace('\u0162', '\u021A') // Ţ -> Ț
                           .replace('\u0163', '\u021B'); // ţ -> ț
        
        // Convert to uppercase
        text = text.toUpperCase();

        // Remove all whitespace
        return text.replaceAll("\\s+", "");
    }

    /**
     * Validates that all characters belong to the Romanian alphabet.
     * Throws IllegalArgumentException indicating the exact invalid character.
     */
    public static void validateText(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!CHAR_TO_INDEX.containsKey(c)) {
                throw new IllegalArgumentException("Invalid character detected: '" + c + "'. Only Romanian letters and spaces are allowed.");
            }
        }
    }

    /**
     * Builds a permuted alphabet based on the keyword:
     * Unique characters of keyword first, followed by the remaining alphabet.
     */


    public static String buildPermutedAlphabet(String keyword) {
        StringBuilder sb = new StringBuilder();
        Set<Character> seen = new HashSet<>();

        // Add distinct characters from the normalized keyword
        for (char c : keyword.toCharArray()) {
            if (!seen.contains(c)) {
                seen.add(c);
                sb.append(c);
            }
        }

        // Add remaining characters in their natural Romanian order
        for (char c : ALPHABET_STRING.toCharArray()) {
            if (!seen.contains(c)) {
                seen.add(c);
                sb.append(c);
            }
        }

        return sb.toString();
    }

    
    public static int getIndex(char c, String alphabet) {
        return alphabet.indexOf(c);
    }

    public static char getChar(int index, String alphabet) {
        return alphabet.charAt(index);
    }
}