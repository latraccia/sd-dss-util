/*
 * DSS-CLI, a Command Line Interface for SD-DSS.
 * Copyright (C) 2013 La Traccia
 * Developed by Francesco Pontillo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package it.latraccia.dss.cli.main.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.X509Certificate;

/**
 * Utility class for {@link String}, {@link java.io.File} and console handling.
 *
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
        return br.readLine();
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
        return Integer.parseInt(input);
    }

    /**
     * Read an {@link Integer} from the standard input, and ask again until the parsing succeeds.
     * @param maxAttempts Number of maximum attempts for reading, unhandled if less than 1.
     * @param lBound Lower bound accepted for the integer number (included).
     * @param uBound Upper bound accepted for the integer number (included).
     * @return The read {@link Integer}
     */
    @SuppressWarnings("ConstantConditions")
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

    /**
     * Get the file extension from a file name.
     * @param fileName The file name
     * @return The last extension for the file name
     */
    public static String getFileExtension(String fileName) {
        String extension = null;
        int i2 = fileName.lastIndexOf(".");
        if (i2 > 0) {
            extension = fileName.substring(i2);
        }
        return extension;
    }

    /**
     * Get the file name without the extension.
     * @param fileName The file name
     * @return The file name without the final extension
     */
    public static String getFileNameWithoutExtension(String fileName) {
        int i2 = fileName.lastIndexOf(".");
        String simpleFileName = null;
        if (i2 > 0) {
            // Get the original name without the extension
            simpleFileName = fileName.substring(0, i2);
        }
        return simpleFileName;
    }
}
