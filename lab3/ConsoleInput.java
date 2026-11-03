package lab3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

/**
 * Reads lines of text, keeping the Romanian letters. In a Windows console Java 17 reads
 * Ă, Â, Î, Ș, Ț as NUL or '?' whatever the code page is, so there a short PowerShell
 * call reads the line through the Unicode console API and returns it in Base64.
 */
public class ConsoleInput {
    private static final boolean WINDOWS_CONSOLE = System.console() != null
            && System.getProperty("os.name").startsWith("Windows");

    private static final String READ_LINE =
            "[Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes([string](Read-Host)))";

    // Used when the input comes from a pipe or a file, or if PowerShell is not available
    private static final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());

    /** Makes the console display the UTF-8 output of the program correctly. */
    public static void prepareConsole() {
        if (!WINDOWS_CONSOLE) return;

        try {
            new ProcessBuilder("cmd", "/c", "chcp 65001 >nul").inheritIO().start().waitFor();
        } catch (IOException e) {
            // The program still works, only the diacritics may be displayed incorrectly
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String readLine() {
        if (WINDOWS_CONSOLE) {
            String line = readWithPowerShell();
            if (line != null) return line;
        }
        return scanner.nextLine();
    }

    private static String readWithPowerShell() {
        try {
            Process process = new ProcessBuilder("powershell", "-NoProfile", "-Command", READ_LINE)
                    .redirectInput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.DISCARD)
                    .start();
            String encoded = new String(process.getInputStream().readAllBytes(), StandardCharsets.US_ASCII).trim();

            if (process.waitFor() != 0) return null;
            return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        } catch (IOException | IllegalArgumentException e) {
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
