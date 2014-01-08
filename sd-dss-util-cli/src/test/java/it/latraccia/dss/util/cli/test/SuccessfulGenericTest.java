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

import it.latraccia.dss.util.cli.SignCLI;
import it.latraccia.dss.util.exception.SignatureException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(value = Parameterized.class)
public abstract class SuccessfulGenericTest extends GenericTest {
    String[] args;
    String description;

    public abstract String[] getClassArgs();

    public SuccessfulGenericTest(String description, Object[] args) {
        this.args = (String[]) args;
        this.description = description;
    }

    @Test
    public void successfulTest()
            throws IOException, SignatureException, KeyStoreException, NoSuchAlgorithmException {
        // Eventually add the simulation parameter
        List<String> arguments = new ArrayList<String>();
        Collections.addAll(arguments, getClassArgs());
        Collections.addAll(arguments, args);
        handleSimulate(arguments);

        SignCLI.main(listToArray(arguments));
    }
}
