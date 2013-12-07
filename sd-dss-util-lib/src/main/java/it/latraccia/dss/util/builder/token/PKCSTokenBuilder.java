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

import it.latraccia.dss.util.exception.SignatureMoccaAlgorithmMismatchException;
import it.latraccia.dss.util.exception.SignatureMoccaUnavailabilityException;

import java.io.FileNotFoundException;

/**
 * Date: 07/12/13
 * Time: 9.08
 *
 * @author Francesco Pontillo
 */
public abstract class PKCSTokenBuilder extends TokenBuilder {
    protected String file;
    protected String password;

    @Override
    public SignatureToken build() throws SignatureMoccaAlgorithmMismatchException, SignatureMoccaUnavailabilityException, FileNotFoundException {
        SignatureToken signatureToken = super.build();
        signatureToken.setFile(file);
        signatureToken.setPassword(password);
        return signatureToken;
    }

    public String getFile() {
        return file;
    }

    public PKCSTokenBuilder setFile(String file) {
        this.file = file;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PKCSTokenBuilder setPassword(String password) {
        this.password = password;
        return this;
    }
}
