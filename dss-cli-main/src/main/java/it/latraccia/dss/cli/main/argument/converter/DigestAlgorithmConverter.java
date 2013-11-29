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

package it.latraccia.dss.cli.main.argument.converter;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import eu.europa.ec.markt.dss.DigestAlgorithm;

/**
 * Converter class from {@link String} to {@link eu.europa.ec.markt.dss.DigestAlgorithm}.
 * Accepts any case form of "SHA1", "SHA256", "SHA512".
 *
 * @author Francesco Pontillo
 *
 * Date: 29/11/13
 * Time: 12.01
 */
public class DigestAlgorithmConverter implements IStringConverter<DigestAlgorithm> {
    @Override
    public DigestAlgorithm convert(String s) {
        if ("SHA1".equalsIgnoreCase(s)) {
            return DigestAlgorithm.SHA1;
        } else if ("SHA256".equalsIgnoreCase(s)) {
            return DigestAlgorithm.SHA256;
        } else if ("SHA512".equalsIgnoreCase(s)) {
            return DigestAlgorithm.SHA512;
        } else throw new ParameterException(
                String.format("Could not recognize %s as a valid digest algorithm.", s));
    }
}
