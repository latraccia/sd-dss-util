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

package it.latraccia.dss.cli.main.util;

import eu.europa.ec.markt.dss.signature.SignaturePackaging;

import java.util.HashMap;

/**
 * Helper for asserting some properties and validating the signature parameters.
 *
 * Date: 30/11/13
 * Time: 8.46
 *
 * @author Francesco Pontillo
 */
public class AssertHelper {
    public static boolean stringMustEqual(String what, String string, String equalToThis) {
        if (!(string == null && equalToThis == null)
                || !string.equals(equalToThis)) {
            System.err.println(String.format("The %s has to be \"%s\" instead of \"%s\"",
                    what, equalToThis, string));
            return false;
        }
        return true;
    }

    public static boolean stringMustNotEqual(String what, String string, String notEqualToThis) {
        if ((string == null && notEqualToThis == null)
                || string.equals(notEqualToThis)) {
            System.err.println(String.format("The %s has to be different than \"%s\" so it can't be \"%s\"",
                    what, notEqualToThis, string));
            return false;
        }
        return true;
    }

    public static boolean stringMustBeInList(String what, String string, String[] list) {
        if (Util.isNullOrEmpty(string)) {
            System.err.println(String.format("The %s must be defined.", what));
            return false;
        }

        if (list != null) {
            for (String s : list) {
                if (s.equals(string)) {
                    return true;
                }
            }
        }

        System.err.println(String.format(
                "The %s can't be \"%s\". It must be one of \"%s\"",
                what, string,
                stringifyStringArray(list)));
        return false;

    }

    public static boolean packageMustBeInList(String what, SignaturePackaging packaging, SignaturePackaging[] list) {
        if (list != null) {
            for (SignaturePackaging e : list) {
                if (e == packaging) {
                    return true;
                }
            }
        }

        HashMap<SignaturePackaging, String> packagingMap = new HashMap<SignaturePackaging, String>();
        packagingMap.put(SignaturePackaging.DETACHED, "DETACHED");
        packagingMap.put(SignaturePackaging.ENVELOPING, "ENVELOPING");
        packagingMap.put(SignaturePackaging.ENVELOPED, "ENVELOPED");

        String[] valuesArray = new String[0];
        valuesArray = packagingMap.values().toArray(valuesArray);
        System.err.println(String.format(
                "The %s can't be \"%s\". It must be one of \"%s\"",
                what,
                packagingMap.get(packaging),
                stringifySignaturePackagingArray(list)));
        return false;
    }

    public static String stringifyStringArray(String[] list) {
        String stringified = "";
        for (String s : list) {
            stringified += s + ", ";
        }
        stringified = stringified.substring(0, stringified.length() - 2);
        return stringified;
    }

    public static String stringifySignaturePackagingArray(SignaturePackaging[] list) {
        String[] stringList = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            stringList[i] = list[i].name();
        }
        return stringifyStringArray(stringList);
    }
}
