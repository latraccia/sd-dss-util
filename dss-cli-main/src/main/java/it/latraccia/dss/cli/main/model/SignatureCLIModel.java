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

package it.latraccia.dss.cli.main.model;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import it.latraccia.dss.cli.main.util.Util;

import java.io.IOException;

/**
 * Model for containing and pre-processing the signature parameters.
 *
 * Date: 28/11/13
 * Time: 11.07
 *
 * @see SignatureModel
 *
 * @author Francesco Pontillo
 */
public class SignatureCLIModel extends SignatureModel {
    protected String signatureSimpleFormat;
    protected String signatureLevel;
    protected SignatureTokenConnection signatureTokenConnection;
    protected DigestAlgorithm digestAlgorithm;

    protected char[] pkcs11Password;

    public SignatureCLIModel() {
        super();
    }

    public SignatureCLIModel(String serviceUrl) {
        super();
        this.setServiceUrl(serviceUrl);
    }

    public SignatureTokenConnection createTokenConnection() {
        PasswordInputCallback passwordInput = new CLIPasswordStoredCallback();
        SignatureTokenConnection connection = super.createTokenConnection(passwordInput);
        setSignatureTokenConnection(connection);
        return connection;
    }

    public String getSignatureSimpleFormat() {
        return signatureSimpleFormat;
    }

    public void setSignatureSimpleFormat(String signatureSimpleFormat) {
        this.signatureSimpleFormat = signatureSimpleFormat;
        setSignatureFormat(signatureSimpleFormat + "-" + signatureLevel);
    }

    public String getSignatureLevel() {
        return signatureLevel;
    }

    public void setSignatureLevel(String signatureLevel) {
        this.signatureLevel = signatureLevel;
        setSignatureFormat(signatureSimpleFormat + "-" + signatureLevel);
    }

    public SignatureTokenConnection getSignatureTokenConnection() {
        return signatureTokenConnection;
    }

    public void setSignatureTokenConnection(SignatureTokenConnection signatureTokenConnection) {
        this.signatureTokenConnection = signatureTokenConnection;
    }

    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public void setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public char[] getPkcs11Password() {
        return pkcs11Password;
    }

    public void setPkcs11Password(char[] pkcs11Password) {
        this.pkcs11Password = pkcs11Password;
    }

    /**
     * Return a previously-read password already stored in the model.
     */
    private class CLIPasswordStoredCallback implements PasswordInputCallback {
        @Override
        public char[] getPassword() {
            return getPkcs11Password();
        }
    }

    /**
     * Ask for a password when the token connection is initialized.
     *
     * @deprecated asking for a password is redundant
     * @see {@link CLIPasswordStoredCallback} as new implementation
     */
    private class CLIPasswordInputCallback implements PasswordInputCallback {
        @Override
        public char[] getPassword() {
            String password;
            System.out.print("Password: ");
            try {
                password = Util.readln();
            } catch (IOException e) {
                e.printStackTrace();
                password = "";
            }
            return password.toCharArray();
        }
    }
}
