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
import eu.europa.ec.markt.dss.signature.SignaturePackaging;

/**
 * Converter class from {@link String} to {@link SignaturePackaging}.
 * Accepts any case form of "ENVELOPED", "ENVELOPING", "DETACHED".
 *
 * @author Francesco Pontillo
 *
 * Date: 27/11/13
 * Time: 11.26
 */
public class PackagingConverter implements IStringConverter<SignaturePackaging> {
    @Override
    public SignaturePackaging convert(String s) {
        String upperedPackage = s.toUpperCase();
        if ("ENVELOPED".equals(upperedPackage)) {
            return SignaturePackaging.ENVELOPED;
        } else if ("ENVELOPING".equals(upperedPackage)) {
            return SignaturePackaging.ENVELOPING;
        } else if ("DETACHED".equals(upperedPackage)) {
            return SignaturePackaging.DETACHED;
        } else throw new ParameterException(
                String.format("Could not recognize %s as a valid signature packaging.", s));
    }
}
