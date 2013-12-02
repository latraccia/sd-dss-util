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

package it.latraccia.dss.cli.main.test;

import it.latraccia.dss.cli.main.test.util.SignatureArgsHelper;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public abstract class PADESSuccessfulGenericTest extends SuccessfulGenericTest {

    public PADESSuccessfulGenericTest(String description, Object[] args) {
        super(description, args);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getSuccessfulTestParams() {
        final Object[][] params =
                {
                        // PDF, BES, ENVELOPED
                        {"PDF, BES, ENVELOPED", new String[] {
                                SignatureArgsHelper.Input.PDF_FILE,
                                "-f=" + SignatureArgsHelper.Format.PADES,
                                "-l=" + SignatureArgsHelper.Level.BES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPED,
                                "-p12=\"" + SignatureArgsHelper.PKCS12.KEY + "\"",
                                "\"" + SignatureArgsHelper.PKCS12.PASSWORD + "\"",
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // PDF, EPES, ENVELOPED
                        {"PDF, EPES, ENVELOPED", new String[] {
                                SignatureArgsHelper.Input.PDF_FILE,
                                "-f=" + SignatureArgsHelper.Format.PADES,
                                "-l=" + SignatureArgsHelper.Level.EPES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPED,
                                "-p12=\"" + SignatureArgsHelper.PKCS12.KEY + "\"",
                                "\"" + SignatureArgsHelper.PKCS12.PASSWORD + "\"",
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }}
                };

        return Arrays.asList(params);
    }
}
