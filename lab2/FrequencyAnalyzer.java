package lab2;

import java.util.*;

public class FrequencyAnalyzer {
    private final String text;
    private final int[] counts = new int[EnglishFrequency.ALPHABET_SIZE];
    private int total;

    /**
     * Counts every Latin letter of the text (case-insensitive).
     * Digits, punctuation and spaces are ignored, as they were not encrypted.
     */
    public FrequencyAnalyzer(String text) {
        this.text = text;
        for (char c : text.toCharArray()) {
            int i = EnglishFrequency.index(c);
            if (i >= 0) {
                counts[i]++;
                total++;
            }
        }
    }

    public int count(char c) {
        return counts[EnglishFrequency.index(c)];
    }

    public int total() {
        return total;
    }

    public double percent(char c) {
        return total == 0 ? 0 : 100.0 * count(c) / total;
    }

    /**
     * Ciphertext letters ordered by descending count (ties broken alphabetically).
     */
    public String lettersByFrequency() {
        Character[] letters = new Character[EnglishFrequency.ALPHABET_SIZE];
        for (int i = 0; i < letters.length; i++) {
            letters[i] = EnglishFrequency.ALPHABET.charAt(i);
        }
        Arrays.sort(letters, (a, b) -> count(b) != count(a) ? count(b) - count(a) : a - b);

        StringBuilder sb = new StringBuilder();
        for (char c : letters) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Most frequent words of the given length, e.g. 3-letter words to spot "THE" and "AND".
     */
    public List<Map.Entry<String, Integer>> topWords(int length, int limit) {
        Map<String, Integer> words = new HashMap<>();
        for (String w : text.toUpperCase().split("[^A-Z]+")) {
            if (w.length() == length) {
                words.merge(w, 1, Integer::sum);
            }
        }
        return top(words, limit);
    }

    /**
     * Most frequent n-grams (digraphs, trigraphs) counted inside words only.
     */
    public List<Map.Entry<String, Integer>> topNgrams(int n, int limit) {
        Map<String, Integer> grams = new HashMap<>();
        for (String w : text.toUpperCase().split("[^A-Z]+")) {
            for (int i = 0; i + n <= w.length(); i++) {
                grams.merge(w.substring(i, i + n), 1, Integer::sum);
            }
        }
        return top(grams, limit);
    }

    /**
     * Most frequent doubled letters (in English: SS, EE, TT, OO, FF).
     */
    public List<Map.Entry<String, Integer>> topDoubles(int limit) {
        Map<String, Integer> doubles = new HashMap<>();
        for (Map.Entry<String, Integer> e : topNgrams(2, Integer.MAX_VALUE)) {
            if (e.getKey().charAt(0) == e.getKey().charAt(1)) {
                doubles.put(e.getKey(), e.getValue());
            }
        }
        return top(doubles, limit);
    }

    private static List<Map.Entry<String, Integer>> top(Map<String, Integer> map, int limit) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> !a.getValue().equals(b.getValue())
                ? b.getValue() - a.getValue()
                : a.getKey().compareTo(b.getKey()));
        return list.subList(0, Math.min(limit, list.size()));
    }
}
