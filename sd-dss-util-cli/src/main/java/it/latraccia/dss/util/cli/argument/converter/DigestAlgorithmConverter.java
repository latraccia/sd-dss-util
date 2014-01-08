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

package it.latraccia.dss.util.cli.argument.converter;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import it.latraccia.dss.util.util.AssertHelper;

public class DigestAlgorithmConverter implements IStringConverter<DigestAlgorithm> {
    @Override
    public DigestAlgorithm convert(String s) {
        if (!AssertHelper.stringMustBeInList("digest algorithm", s, new String[] {"SHA1", "SHA256", "SHA512"})) {
            throw new ParameterException(
                    String.format("Could not recognize %s as a valid digest algorithm.", s));
        } else if ("SHA1".equalsIgnoreCase(s)) {
            return DigestAlgorithm.SHA1;
        } else if ("SHA256".equalsIgnoreCase(s)) {
            return DigestAlgorithm.SHA256;
        } else {
            return DigestAlgorithm.SHA512;
        }
    }
}
