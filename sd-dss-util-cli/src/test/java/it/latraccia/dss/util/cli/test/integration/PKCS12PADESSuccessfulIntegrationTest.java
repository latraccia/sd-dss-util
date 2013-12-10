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

package it.latraccia.dss.util.cli.test.integration;

import it.latraccia.dss.util.cli.test.PADESSuccessfulGenericTest;
import it.latraccia.dss.util.cli.test.util.SignatureArgsHelper;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(value = Parameterized.class)
@Category(value = it.latraccia.dss.util.cli.test.integration.IntegrationTest.class)
public class PKCS12PADESSuccessfulIntegrationTest extends PADESSuccessfulGenericTest {

    public PKCS12PADESSuccessfulIntegrationTest(String description, Object[] args) {
        super(description, args);
    }

    @Override
    public boolean getSimulate() {
        return false;
    }

    @Override
    public String[] getClassArgs() {
        return new String[] {
                "-p12=\"" + SignatureArgsHelper.PKCS12.KEY + "\"",
                "\"" + SignatureArgsHelper.PKCS12.PASSWORD + "\"",
        };
    }
}
