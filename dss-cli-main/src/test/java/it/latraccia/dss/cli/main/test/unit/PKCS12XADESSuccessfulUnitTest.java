/*
 * SD-DSS-CLI, a Command Line Interface for SD-DSS.
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

package it.latraccia.dss.cli.main.test.unit;

import it.latraccia.dss.cli.main.test.XADESSuccessfulGenericTest;
import it.latraccia.dss.cli.main.test.util.SignatureArgsHelper;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(value = Parameterized.class)
public class PKCS12XADESSuccessfulUnitTest extends XADESSuccessfulGenericTest {

    public PKCS12XADESSuccessfulUnitTest(String description, Object[] args) {
        super(description, args);
    }

    @Override
    public boolean getSimulate() {
        return true;
    }

    @Override
    public String[] getClassArgs() {
        return new String[] {
                "-p12=\"" + SignatureArgsHelper.PKCS12.KEY + "\"",
                "\"" + SignatureArgsHelper.PKCS12.PASSWORD + "\"",
        };
    }
}
