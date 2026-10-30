package lab2;

public class EnglishFrequency {
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int ALPHABET_SIZE = ALPHABET.length(); // 26

    // Table 2.2: letter frequencies of the English language (percent), A..Z
    public static final double[] PERCENT = {
        8.17, 1.49, 2.78, 4.25, 12.70, 2.23, 2.01, 6.09, 6.97, 0.15, 0.77, 4.03, 2.41,
        6.75, 7.51, 1.93, 0.09, 5.99, 6.33, 9.06, 2.76, 0.98, 2.36, 0.15, 1.97, 0.07
    };

    // Most frequent English digraphs and trigraphs (section 2.2)
    public static final String[] DIGRAPHS = {"TH", "HE", "AN", "IN", "ER", "ON", "RE", "ED", "ND", "HA", "AT", "EN"};
    public static final String[] TRIGRAPHS = {"THE", "AND", "THA", "ENT", "ION", "TIO", "FOR", "NDE", "HAS", "NCE", "TIS", "OFT", "MEN"};

    /**
     * Returns the 0-based index of a Latin letter (case-insensitive), or -1 for any other character.
     */
    public static int index(char c) {
        char upper = Character.toUpperCase(c);
        if (upper >= 'A' && upper <= 'Z') {
            return upper - 'A';
        }
        return -1;
    }

    public static double percent(char c) {
        return PERCENT[index(c)];
    }

    /**
     * English letters ordered by descending frequency: E T A O I N S H R D L ...
     */
    public static String lettersByFrequency() {
        Character[] letters = new Character[ALPHABET_SIZE];
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            letters[i] = ALPHABET.charAt(i);
        }
        java.util.Arrays.sort(letters, (a, b) -> Double.compare(percent(b), percent(a)));

        StringBuilder sb = new StringBuilder();
        for (char c : letters) {
            sb.append(c);
        }
        return sb.toString();
    }
}
