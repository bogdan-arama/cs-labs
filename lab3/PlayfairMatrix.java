package lab3;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Playfair encryption matrix: the distinct letters of the key, followed by
 * the remaining letters of the alphabet, written row by row.
 */
public class PlayfairMatrix {
    public static final int ROMANIAN_ROWS = 5;
    public static final int ROMANIAN_COLS = 6;

    private final char[][] cells;
    private final Map<Character, int[]> positions = new HashMap<>();
    private final String distinctKey;

    public PlayfairMatrix(String key, String alphabet, int rows, int cols) {
        if (rows * cols != alphabet.length()) {
            throw new IllegalArgumentException("A " + rows + "x" + cols + " matrix cannot hold "
                    + alphabet.length() + " letters.");
        }

        Set<Character> letters = new LinkedHashSet<>();
        for (char c : key.toCharArray()) {
            if (alphabet.indexOf(c) < 0) {
                throw new IllegalArgumentException("Key letter '" + c + "' is not in the matrix alphabet.");
            }
            letters.add(c);
        }

        StringBuilder sb = new StringBuilder();
        for (char c : letters) {
            sb.append(c);
        }
        distinctKey = sb.toString();

        for (char c : alphabet.toCharArray()) {
            letters.add(c);
        }

        cells = new char[rows][cols];
        int i = 0;
        for (char c : letters) {
            cells[i / cols][i % cols] = c;
            positions.put(c, new int[]{i / cols, i % cols});
            i++;
        }
    }

    /** Builds the 5x6 matrix used for Romanian messages. The key must be normalized. */
    public static PlayfairMatrix forRomanian(String key) {
        return new PlayfairMatrix(key.replace('J', 'I'), RomanianAlphabet.MATRIX_ALPHABET,
                ROMANIAN_ROWS, ROMANIAN_COLS);
    }

    public int rows() {
        return cells.length;
    }

    public int cols() {
        return cells[0].length;
    }

    public char at(int row, int col) {
        return cells[row][col];
    }

    public int[] positionOf(char c) {
        return positions.get(c);
    }

    public String distinctKey() {
        return distinctKey;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : cells) {
            for (int j = 0; j < row.length; j++) {
                sb.append(row[j]);
                if (j < row.length - 1) {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
