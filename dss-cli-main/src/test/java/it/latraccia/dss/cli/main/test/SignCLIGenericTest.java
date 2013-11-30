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

import it.latraccia.dss.cli.main.SignCLI;
import it.latraccia.dss.cli.main.exception.SignatureException;
import it.latraccia.dss.cli.main.test.util.FileHelper;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date: 30/11/13
 * Time: 12.31
 *
 * @author Francesco Pontillo
 */
public abstract class SignCLIGenericTest {

    public abstract boolean getSimulate();

    private void handleSimulate(List<String> strings) {
        if (getSimulate()) {
            strings.add("-s");
        }
    }

    private String[] listToArray(List<String> list) {
        String[] array = new String[0];
        array = list.toArray(array);
        return array;
    }

    @Test
    public void testPKCS12() throws FileNotFoundException, SignatureException {
        File inputFile = FileHelper.getFileFromResource("test.pdf");
        File pkcs12Key = FileHelper.getFileFromResource("0F.p12");

        List<String> arguments = new ArrayList<String>();
        Collections.addAll(arguments,
                inputFile.getAbsolutePath(),
                "-f=CAdES",
                "-l=BES",
                "-p=ENVELOPING",
                "-p12=\"" + pkcs12Key.getAbsolutePath() + "\"",
                "\"password\"",
                "-o=\"" + inputFile.getParent() + "\"",
                "-d=SHA256"
        );
        handleSimulate(arguments);

        SignCLI.main(listToArray(arguments));
    }
}
