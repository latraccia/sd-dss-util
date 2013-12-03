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

import it.latraccia.dss.cli.main.SignCLI;

import java.io.File;

/**
 * Date: 30/11/13
 * Time: 12.01
 *
 * @author Francesco Pontillo
 */
public class FileHelper {
    /**
     * Load a file from a given resource name.
     *
     * @param resource The resource name of the file
     * @return A fully qualified {@link File}
     */
    public static File getFileFromResource(String resource) {
        return new File(SignatureArgsHelper.class.getClassLoader().getResource(resource).getFile());
    }

    /**
     * Load a file path from a given resource name.
     *
     * @param resource The resource name of the file
     * @return A {@link String} of an absolute path
     */
    public static String getResource(String resource) {
        File inputFile = getFileFromResource(resource);
        return inputFile.getAbsolutePath();
    }
}
