/*
 * SD-DSS-Util, a Utility Library and a Command Line Interface for SD-DSS.
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

package it.latraccia.dss.util.cli.test;

import it.latraccia.dss.util.cli.test.util.SignatureArgsHelper;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public abstract class XADESSuccessfulGenericTest extends SuccessfulGenericTest {

    public XADESSuccessfulGenericTest(String description, Object[] args) {
        super(description, args);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getSuccessfulTestParams() {
        final Object[][] params =
                {
                        // PDF, BES, ENVELOPING
                        {"PDF, BES, ENVELOPING", new String[] {
                                SignatureArgsHelper.Input.PDF_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.BES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPING,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // XML, BES, ENVELOPING
                        {"XML, BES, ENVELOPING", new String[] {
                                SignatureArgsHelper.Input.XML_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.BES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPING,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // DOC, BES, ENVELOPING
                        {"DOC, BES, ENVELOPING", new String[] {
                                SignatureArgsHelper.Input.DOC_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.BES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPING,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // PDF, BES, DETACHED
                        {"PDF, BES, DETACHED", new String[] {
                                SignatureArgsHelper.Input.PDF_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.BES,
                                "-p=" + SignatureArgsHelper.Packaging.DETACHED,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // XML, BES, DETACHED
                        {"XML, BES, DETACHED", new String[] {
                                SignatureArgsHelper.Input.XML_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.BES,
                                "-p=" + SignatureArgsHelper.Packaging.DETACHED,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // DOC, BES, DETACHED
                        {"DOC, BES, DETACHED", new String[] {
                                SignatureArgsHelper.Input.DOC_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.BES,
                                "-p=" + SignatureArgsHelper.Packaging.DETACHED,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // XML, BES, ENVELOPED
                        {"XML, BES, ENVELOPED", new String[] {
                                SignatureArgsHelper.Input.XML_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.BES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPED,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // PDF, EPES, ENVELOPING
                        {"PDF, EPES, ENVELOPING", new String[] {
                                SignatureArgsHelper.Input.PDF_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.EPES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPING,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // XML, EPES, ENVELOPING
                        {"XML, EPES, ENVELOPING", new String[] {
                                SignatureArgsHelper.Input.XML_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.EPES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPING,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // DOC, EPES, ENVELOPING
                        {"DOC, EPES, ENVELOPING", new String[] {
                                SignatureArgsHelper.Input.DOC_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.EPES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPING,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // PDF, EPES, DETACHED
                        {"PDF, EPES, DETACHED", new String[] {
                                SignatureArgsHelper.Input.PDF_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.EPES,
                                "-p=" + SignatureArgsHelper.Packaging.DETACHED,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // XML, EPES, DETACHED
                        {"XML, EPES, DETACHED", new String[] {
                                SignatureArgsHelper.Input.XML_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.EPES,
                                "-p=" + SignatureArgsHelper.Packaging.DETACHED,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // DOC, EPES, DETACHED
                        {"DOC, EPES, DETACHED", new String[] {
                                SignatureArgsHelper.Input.DOC_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.EPES,
                                "-p=" + SignatureArgsHelper.Packaging.DETACHED,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                        // XML, EPES, ENVELOPED
                        {"XML, EPES, ENVELOPED", new String[] {
                                SignatureArgsHelper.Input.XML_FILE,
                                "-u=" + SignatureArgsHelper.Url.DSS,
                                "-f=" + SignatureArgsHelper.Format.XADES,
                                "-l=" + SignatureArgsHelper.Level.EPES,
                                "-p=" + SignatureArgsHelper.Packaging.ENVELOPED,
                                "-o=\"" + SignatureArgsHelper.Output.ROOT + "\"",
                                "-d=" + SignatureArgsHelper.DigestAlgorithm.SHA256
                        }},
                };

        return Arrays.asList(params);
    }
}
