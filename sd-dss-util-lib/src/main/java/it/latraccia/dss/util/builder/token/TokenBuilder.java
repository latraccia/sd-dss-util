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

import it.latraccia.dss.util.builder.IBuilder;
import it.latraccia.dss.util.entity.MoccaAlgorithm;
import it.latraccia.dss.util.entity.token.SignatureTokenType;
import it.latraccia.dss.util.exception.SignatureMoccaAlgorithmMismatchException;
import it.latraccia.dss.util.exception.SignatureMoccaUnavailabilityException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Date: 07/12/13
 * Time: 8.45
 *
 * @author Francesco Pontillo
 */
public abstract class TokenBuilder implements IBuilder<TokenBuilder.SignatureToken> {
    protected SignatureTokenType tokenType;

    public SignatureTokenType getTokenType() {
        return tokenType;
    }

    @Override
    public SignatureToken build() throws SignatureMoccaAlgorithmMismatchException, SignatureMoccaUnavailabilityException, FileNotFoundException {
        SignatureToken signatureToken = new SignatureToken();
        signatureToken.setTokenType(tokenType);
        return signatureToken;
    }

    protected void setTokenType(SignatureTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public class SignatureToken {
        protected File tokenAsset;
        protected SignatureTokenType tokenType;
        protected String file;
        protected String password;
        protected MoccaAlgorithm moccaAlgorithm;

        public File getTokenAsset() {
            return tokenAsset;
        }

        public void setTokenAsset(File tokenAsset) {
            this.tokenAsset = tokenAsset;
        }

        public SignatureTokenType getTokenType() {
            return tokenType;
        }

        public void setTokenType(SignatureTokenType tokenType) {
            this.tokenType = tokenType;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public MoccaAlgorithm getMoccaAlgorithm() {
            return moccaAlgorithm;
        }

        public void setMoccaAlgorithm(MoccaAlgorithm moccaAlgorithm) {
            this.moccaAlgorithm = moccaAlgorithm;
        }
    }
}
