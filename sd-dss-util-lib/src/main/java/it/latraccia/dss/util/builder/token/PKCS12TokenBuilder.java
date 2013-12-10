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

package it.latraccia.dss.util.builder.token;

import it.latraccia.dss.util.entity.token.SignatureTokenType;
import it.latraccia.dss.util.exception.SignatureMoccaAlgorithmMismatchException;
import it.latraccia.dss.util.exception.SignatureMoccaUnavailabilityException;
import it.latraccia.dss.util.util.Util;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Date: 07/12/13
 * Time: 8.53
 *
 * @author Francesco Pontillo
 */
public class PKCS12TokenBuilder extends PKCSTokenBuilder {
    public PKCS12TokenBuilder() {
        setTokenType(SignatureTokenType.PKCS12);
    }

    @Override
    public PKCS12TokenBuilder setPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public PKCS12TokenBuilder setFile(String file) {
        super.setFile(file);
        return this;
    }

    @Override
    public SignatureToken build() throws SignatureMoccaAlgorithmMismatchException, SignatureMoccaUnavailabilityException, FileNotFoundException {
        SignatureToken token = super.build();

        File tokenAsset = new File(Util.getFileInAbsolutePathOrResources(getFile()));
        if (tokenAsset.exists()) {
            // Save the token asset file
            token.setTokenAsset(new File(tokenAsset.getAbsolutePath()));
            // Set the PKCS12 file path
            setFile(tokenAsset.getAbsolutePath());
            // Set the file encryption password
            setPassword(password);
        } else {
            // Throw exception for non existing PKCS12 file
            throw new FileNotFoundException("The PKCS12 private key could not be found.");
        }

        return token;
    }
}
