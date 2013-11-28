package it.latraccia.dss.cli.main.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.X509Certificate;

/**
 * @author Francesco Pontillo
 *
 * Date: 27/11/13
 * Time: 13.14
 */
public class Util {
    /**
     * Check if a {@link String} is null or empty.
     * @param string The {@link String} to check
     * @return true if the {@link String} is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.isEmpty());
    }

    /**
     * Read a line from the standard input.
     * @return The read {@link String}
     * @throws IOException if a read error occurred
     */
    public static String readln() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String password = br.readLine();
        return password;
    }

    /**
     * Get the subject DN {@link String} from a certificate.
     * @param cert A {@link X509Certificate} to read the subject DN from
     * @return The {@link String} representing the subject for the {@link X509Certificate}
     */
    public static String getSubjectDN(X509Certificate cert) {
        String subjectDN = cert.getSubjectDN().getName();
        int dnStartIndex = subjectDN.indexOf("CN=") + 3;
        if (dnStartIndex > 0 && subjectDN.indexOf(",", dnStartIndex) > 0) {
            subjectDN = subjectDN.substring(dnStartIndex, subjectDN.indexOf(",", dnStartIndex)) + " (SN:"
                    + cert.getSerialNumber() + ")";
        }
        return subjectDN;
    }

    /**
     * Read an {@link Integer} from the standard input.
     * @return The read {@link Integer}
     * @throws IOException if a read error has occurred
     * @throws NumberFormatException if a parsing error has occurred
     */
    public static int readInt() throws IOException, NumberFormatException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();
        int number = Integer.parseInt(input);
        return number;
    }

    /**
     * Read an {@link Integer} from the standard input, and ask again until the parsing succeeds.
     * @param maxAttempts Number of maximum attempts for reading, unhandled if less than 1.
     * @param lBound Lower bound accepted for the integer number (included).
     * @param uBound Upper bound accepted for the integer number (included).
     * @return
     */
    public static int readInt(int maxAttempts, int lBound, int uBound) {
        boolean parsed;
        int number = -1;
        int attempt = 0;
        do {
            if (attempt > 0) {
                System.out.println("Not a valid selection. Please try again.");
            }
            try {
                attempt++;
                number = readInt();
                parsed = true;
            } catch (IOException e) {
                parsed = false;
            } catch (NumberFormatException e) {
                parsed = false;
            }                                                   // Repeat if...
        } while (!parsed ||                                     // - the number is not parsed
                (attempt < maxAttempts && maxAttempts >= 0) ||  // - maxAttempts not reached and must be accounted for
                (number < lBound) || (number > uBound));        // - the upper or lower bound is not respected

        return number;
    }
}
