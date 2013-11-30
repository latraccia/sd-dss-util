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

package it.latraccia.dss.cli.main.test.util;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    // All possibilities to be tested are listed here, then they are combined together
    public static String[] INPUT = new String[]{
            Input.getPdfFile(), Input.getDocFile(), Input.getDocxFile(), Input.getOdtFile(), "made_up"
    };
    public static String[] OUTPUT = new String[]{
            Output.getFile("pdf"), Output.getFile("made_up")
    };
    public static String[] PKCS12_PATH = new String[]{
            PKCS12.getKey(), "W:/made/up/path/you/cant/possibly/have/this"
    };
    public static String[] PKCS12_PASSWORD = new String[]{
            PKCS12.getPassword(), "wrong_password"
    };
    public static String[] PKCS11_PATH = new String[]{
            PKCS11.getLibrary(), "W:/made/up/path/you/cant/possibly/have/this"
    };
    public static String[] PKCS11_PASSWORD = new String[]{
            PKCS11.getPassword(), "wrong_password"
    };
    public static String[] FORMAT = new String[]{
            Format.getCades(), Format.getPades(), Format.getXades(), Format.getAsics()
    };
    public static String[] LEVEL = new String[]{
            Level.getBep(), Level.getEpes(), Level.getLTV(), Level.getT(),
            Level.getC(), Level.getX(), Level.getXL(), Level.getA()
    };
    public static String[] PACKAGING = new String[]{
            Packaging.getEnveloped(), Packaging.getEnveloping(), Packaging.getDetached()
    };
    public static String[] DIGEST_ALGORITHM = new String[]{
            DigestAlgorithm.getSha256()
    };

    protected static class Input {
        public static String getFile(String extension) {
            return FileHelper.getResource("test." + extension);
        }

        public static String getPdfFile() {
            return getFile("pdf");
        }

        public static String getDocFile() {
            return getFile("doc");
        }

        public static String getDocxFile() {
            return getFile("docx");
        }

        public static String getOdtFile() {
            return getFile("odt");
        }
    }

    private static class Output extends Input {
        public static String getFile(String extension) {
            return FileHelper.getFileFromResource("test." + extension).getParent();
        }
    }

    protected static class PKCS12 {
        public static String getKey() {
            return FileHelper.getResource("0F.p12");
        }

        public static String getPassword() {
            return "password";
        }
    }

    protected static class PKCS11 {
        public static String getLibrary() {
            throw new NotImplementedException();
        }

        public static String getPassword() {
            throw new NotImplementedException();
        }
    }

    protected static class Format {
        public static String getCades() {
            return "CAdES";
        }

        public static String getPades() {
            return "PAdES";
        }

        public static String getXades() {
            return "XAdES";
        }

        public static String getAsics() {
            return "ASiC-S";
        }
    }

    protected static class Level {
        public static String getBep() {
            return "BES";
        }

        public static String getEpes() {
            return "EPES";
        }

        public static String getLTV() {
            return "LTV";
        }

        public static String getT() {
            return "T";
        }

        public static String getC() {
            return "C";
        }

        public static String getX() {
            return "X";
        }

        public static String getXL() {
            return "XL";
        }

        public static String getA() {
            return "A";
        }
    }

    protected static class Packaging {
        public static String getEnveloped() {
            return "ENVELOPED";
        }

        public static String getEnveloping() {
            return "ENVELOPING";
        }

        public static String getDetached() {
            return "DETACHED";
        }
    }

    protected static class DigestAlgorithm {
        public static String getSha256() {
            return "SHA256";
        }
    }

    public List<String> getBaseArgs() {
        List<String> arguments = new ArrayList<String>();
        Collections.addAll(arguments);
        return arguments;
    }
}
