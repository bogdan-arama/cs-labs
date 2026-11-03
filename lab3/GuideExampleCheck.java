package lab3;

/**
 * Checks the cipher on the English example of the laboratory guide: key FIRST AMENDMENT,
 * a 5x5 matrix without J, and the prepared text and cryptogram printed in the guide.
 */
public class GuideExampleCheck {
    private static final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
    private static final String KEY = "FIRSTAMENDMENT";

    private static final String PREPARED =
            "CONGRESXSZSHALLMMAKENOLAWRESPECTINGANESTAB"
            + "LISHMENTOFRELIGIONORPROHIBITINGTFREXEZEXERCI"
            + "SETHEREOFORABRRIDGINGTHEFREXEDOMOFSPEXECHO"
            + "ROFTHEPRESSORTHERIGHTOFTHEPEOPLEPEACEABLYT"
            + "OASXSEMBLEANDTOPETITIONTHEGOVERNMENTFORARE"
            + "DRESSOFGRIEVANCESB";

    private static final String CIPHERTEXT =
            "OWEHEGRYTYNQBVOAEMGDMQVBXINRXGKISMBEDNTFBLOF"
            + "NQENDSLIEGOFCRQMPIXEQCFCRFSMKRISGRDXGRGEOMRNSK"
            + "GEMPILFEGFSREKSMKRGNISGRNAWCLIRQGRMGCQIPIFGNXE"
            + "NRIQSFGNSRHKIUIFGNXGPQPAXGMBNMLVZSLMRYRNACPAM"
            + "DKDPQDRRFMWDSGNCPXASEENDSILFEEGETNRIQRBSRAXMDG"
            + "MFH";

    public static void main(String[] args) {
        PlayfairMatrix matrix = new PlayfairMatrix(KEY, ALPHABET, 5, 5);
        String encrypted = PlayfairCipher.encrypt(PREPARED, matrix);
        String decrypted = PlayfairCipher.decrypt(CIPHERTEXT, matrix);

        System.out.println("Matrix:");
        System.out.print(matrix);
        System.out.println("Pairs in the guide : " + PREPARED.length() / 2);
        System.out.println("Encryption matches : " + encrypted.equals(CIPHERTEXT));
        System.out.println("Decryption matches : " + decrypted.equals(PREPARED));
    }
}
