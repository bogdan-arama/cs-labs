package lab1;
public class CaesarCipher {

    /**
     * Encrypts text using the provided alphabet mapping and shift key k.
     * Formula: c = (x + k) mod n
     */
    public static String encrypt(String text, int key, String alphabet) {
        int n = alphabet.length();
        StringBuilder cipherText = new StringBuilder();

        for (char c : text.toCharArray()) {
            int x = RomanianAlphabet.getIndex(c, alphabet);
            int encIndex = (x + key) % n;
            cipherText.append(RomanianAlphabet.getChar(encIndex, alphabet));
        }

        return cipherText.toString();
    }

    /**
     * Decrypts text using the provided alphabet mapping and shift key k.
     * Formula: m = (y - k) mod n (adjusted for negative values)
     */
    
    public static String decrypt(String text, int key, String alphabet) {
        int n = alphabet.length();
        StringBuilder plainText = new StringBuilder();

        for (char c : text.toCharArray()) {
            int y = RomanianAlphabet.getIndex(c, alphabet);
            int decIndex = (y - key) % n;
            if (decIndex < 0) {
                decIndex += n; // Wrap around negative modulo
            }
            plainText.append(RomanianAlphabet.getChar(decIndex, alphabet));
        }

        return plainText.toString();
    }
}