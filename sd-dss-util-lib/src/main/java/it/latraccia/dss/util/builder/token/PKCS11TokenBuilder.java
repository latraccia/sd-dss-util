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
public class PKCS11TokenBuilder extends PKCSTokenBuilder {
    protected PKCS11TokenPasswordCallback pkcs11TokenPasswordCallback;

    public PKCS11TokenBuilder() {
        setTokenType(SignatureTokenType.PKCS11);
    }

    @Override
    public PKCS11TokenBuilder setPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public PKCS11TokenBuilder setFile(String file) {
        super.setFile(file);
        return this;
    }

    public PKCS11TokenBuilder setPasswordCallback(PKCS11TokenPasswordCallback callback) {
        this.pkcs11TokenPasswordCallback = callback;
        return this;
    }

    @Override
    public SignatureToken build() throws SignatureMoccaAlgorithmMismatchException, SignatureMoccaUnavailabilityException, FileNotFoundException {
        if (pkcs11TokenPasswordCallback != null) {
            String password = pkcs11TokenPasswordCallback.getPassword();
            setPassword(password);
        }

        SignatureToken token = super.build();

        // Get the token asset
        File tokenAsset = new File(Util.getFileInAbsolutePathOrResources(getFile()));
        if (tokenAsset.exists()) {
            // Save the token asset file
            token.setTokenAsset(new File(tokenAsset.getAbsolutePath()));
            // Set the PKCS11 library file
            setFile(tokenAsset.getAbsolutePath());
            // Set the card encryption password
            setPassword(password);
        } else {
            // Throw exception for non existing PKCS11 library
            throw new FileNotFoundException("The PKCS11 library could not be found.");
        }

        return token;
    }

    public interface PKCS11TokenPasswordCallback {
        public String getPassword();
    }
}
