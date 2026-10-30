package lab2;

public class SubstitutionKey {
    private static final char UNKNOWN = 0;

    // cipherToPlain[i] = plaintext letter (lowercase) for ciphertext letter 'A' + i
    private final char[] cipherToPlain = new char[EnglishFrequency.ALPHABET_SIZE];

    /**
     * Records the guess "ciphertext letter -> plaintext letter".
     * A monoalphabetic key is a bijection, so a plaintext letter may have only one ciphertext image.
     */
    public void set(char cipher, char plain) {
        int ci = EnglishFrequency.index(cipher);
        int pi = EnglishFrequency.index(plain);
        if (ci < 0 || pi < 0) {
            throw new IllegalArgumentException("Both letters must be in A-Z.");
        }

        char p = Character.toLowerCase(plain);
        for (int i = 0; i < cipherToPlain.length; i++) {
            if (i != ci && cipherToPlain[i] == p) {
                throw new IllegalArgumentException("Plaintext '" + p + "' is already the image of ciphertext '"
                        + EnglishFrequency.ALPHABET.charAt(i) + "'. Remove that substitution first.");
            }
        }
        cipherToPlain[ci] = p;
    }

    public void remove(char cipher) {
        cipherToPlain[EnglishFrequency.index(cipher)] = UNKNOWN;
    }

    public void clear() {
        java.util.Arrays.fill(cipherToPlain, UNKNOWN);
    }

    public boolean isMapped(char cipher) {
        return cipherToPlain[EnglishFrequency.index(cipher)] != UNKNOWN;
    }

    public int size() {
        int n = 0;
        for (char p : cipherToPlain) {
            if (p != UNKNOWN) n++;
        }
        return n;
    }

    /**
     * Working view used during the attack (same notation as section 2.3):
     * recovered letters are written in lowercase, still unknown ciphertext letters in UPPERCASE.
     */
    public String apply(String cipherText) {
        StringBuilder sb = new StringBuilder();
        for (char c : cipherText.toUpperCase().toCharArray()) {
            int i = EnglishFrequency.index(c);
            if (i >= 0 && cipherToPlain[i] != UNKNOWN) {
                sb.append(cipherToPlain[i]);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Final decryption: keeps the letter case of the ciphertext; unknown letters become '?'.
     */
    public String decrypt(String cipherText) {
        StringBuilder sb = new StringBuilder();
        for (char c : cipherText.toCharArray()) {
            int i = EnglishFrequency.index(c);
            if (i < 0) {
                sb.append(c);
            } else if (cipherToPlain[i] == UNKNOWN) {
                sb.append('?');
            } else {
                sb.append(Character.isUpperCase(c) ? Character.toUpperCase(cipherToPlain[i]) : cipherToPlain[i]);
            }
        }
        return sb.toString();
    }

    /**
     * Reconstructed alphabet (table 2.5): for each plaintext letter a..z, its ciphertext letter ('-' if unknown).
     */
    public String cipherAlphabet() {
        StringBuilder sb = new StringBuilder();
        for (char p = 'a'; p <= 'z'; p++) {
            char found = '-';
            for (int i = 0; i < cipherToPlain.length; i++) {
                if (cipherToPlain[i] == p) {
                    found = EnglishFrequency.ALPHABET.charAt(i);
                }
            }
            sb.append(found);
        }
        return sb.toString();
    }
}
