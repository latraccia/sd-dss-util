/*
 * DSS-CLI, a Command Line Interface for SD-DSS.
 * Copyright (C) 2013 La Traccia http://www.latraccia.it/en/
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

package it.latraccia.dss.cli.main.test.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date: 30/11/13
 * Time: 12.58
 *
 * @author Francesco Pontillo
 */
public class SignatureArgsHelper {

    private static String resourceRoot;

    static {
        resourceRoot = FileHelper.getFileFromResource("test.pdf").getParent();
    }

    // All possibilities to be tested are listed here, then they are combined together

    public static class Url {
        public static String DSS = "http://localhost:9090/service";
    }

    public static class Input {
        public static String PDF_FILE = getFile("pdf");

        public static String DOC_FILE = getFile("doc");

        public static String DOCX_FILE = getFile("docx");

        public static String ODT_FILE = getFile("odt");

        public static String XML_FILE = getFile("xml");

        private static String getFile(String extension) {
            return resourceRoot + "/test." + extension;
        }
    }

    public static class Output {
        public static String ROOT = resourceRoot;
    }

    public static class PKCS12 {
        public static String KEY = FileHelper.getResource("0F.p12");

        public static String PASSWORD = "password";
    }

    public static class PKCS11 {
        public static String LIBRARY = FileHelper.getResource("drivers/bit4ipki.dll");

        public static String PASSWORD = "";
    }

    public static class Format {
        public static String CADES = "CAdES";

        public static String PADES = "PAdES";

        public static String XADES = "XAdES";

        public static String ASICS = "ASiC-S";
    }

    public static class Level {
        public static String BES = "BES";

        public static String EPES = "EPES";

        public static String LTV = "LTV";

        public static String T = "T";

        public static String C = "C";

        public static String X = "X";

        public static String XL = "XL";

        public static String A = "A";
    }

    public static class Packaging {
        public static String ENVELOPED = "ENVELOPED";

        public static String ENVELOPING = "ENVELOPING";

        public static String DETACHED = "DETACHED";
    }

    public static class DigestAlgorithm {
        public static String SHA256 = "SHA256";
    }

    public List<String> getBaseArgs() {
        List<String> arguments = new ArrayList<String>();
        Collections.addAll(arguments);
        return arguments;
    }
}
