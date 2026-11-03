package lab3;

public class PlayfairCipher {
    // Q, W and X are almost never used in Romanian words. Z from the guide's English
    // list (Q, X, Z) is replaced by W, because Z is common in Romanian (AZI, ZIUA).
    private static final char[] FILLERS = {'X', 'Q', 'W'};

    /**
     * Prepares a normalized message for encryption: J becomes I, a filler letter
     * separates two equal letters of a pair and the last letter is completed if
     * the length is odd. A run such as SSS becomes SX SQ S..., with different fillers.
     */
    public static String prepare(String text) {
        String letters = text.replace('J', 'I');
        StringBuilder prepared = new StringBuilder();
        int filler = 0;
        int i = 0;

        while (i < letters.length()) {
            char first = letters.charAt(i);

            if (i + 1 < letters.length() && letters.charAt(i + 1) != first) {
                prepared.append(first).append(letters.charAt(i + 1));
                filler = 0;
                i += 2;
            } else {
                // Equal letters: only the first one is used now, the other starts the next pair
                if (FILLERS[filler % FILLERS.length] == first) {
                    filler++;
                }
                prepared.append(first).append(FILLERS[filler % FILLERS.length]);
                filler++;
                i++;
            }
        }

        return prepared.toString();
    }

    /**
     * Checks the properties of every valid cryptogram: it has no J, an even
     * length and no pair of two equal letters.
     */
    public static void validateCiphertext(String text) {
        if (text.indexOf('J') >= 0) {
            throw new IllegalArgumentException("A cryptogram cannot contain J, because J is not in the matrix.");
        }
        if (text.length() % 2 != 0) {
            throw new IllegalArgumentException("A cryptogram must have an even number of letters. Provided: "
                    + text.length() + ".");
        }
        for (int i = 0; i < text.length(); i += 2) {
            if (text.charAt(i) == text.charAt(i + 1)) {
                throw new IllegalArgumentException("Pair " + (i / 2 + 1) + " has two equal letters ("
                        + text.substring(i, i + 2) + "), which encryption never produces.");
            }
        }
    }

    /** Encrypts a prepared text: letters on one row or column move forward by one. */
    public static String encrypt(String prepared, PlayfairMatrix matrix) {
        return transform(prepared, matrix, 1);
    }

    /** Decrypts a cryptogram: letters on one row or column move back by one. */
    public static String decrypt(String ciphertext, PlayfairMatrix matrix) {
        return transform(ciphertext, matrix, -1);
    }

    private static String transform(String text, PlayfairMatrix matrix, int shift) {
        StringBuilder result = new StringBuilder();
        int rows = matrix.rows();
        int cols = matrix.cols();

        for (int i = 0; i < text.length(); i += 2) {
            int[] a = matrix.positionOf(text.charAt(i));
            int[] b = matrix.positionOf(text.charAt(i + 1));

            if (a[0] == b[0]) {
                // Same row: next (or previous) letter on the row, wrapping around
                result.append(matrix.at(a[0], (a[1] + shift + cols) % cols));
                result.append(matrix.at(b[0], (b[1] + shift + cols) % cols));
            } else if (a[1] == b[1]) {
                // Same column: letter below (or above), wrapping around
                result.append(matrix.at((a[0] + shift + rows) % rows, a[1]));
                result.append(matrix.at((b[0] + shift + rows) % rows, b[1]));
            } else {
                // Rectangle: same row, column of the other letter
                result.append(matrix.at(a[0], b[1]));
                result.append(matrix.at(b[0], a[1]));
            }
        }

        return result.toString();
    }

    /** Writes a text in groups of two letters, for display. */
    public static String pairs(String text) {
        return text.replaceAll("(..)(?=.)", "$1 ");
    }
}
