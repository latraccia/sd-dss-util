package it.latraccia.dss.cli.main.model;

import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import it.latraccia.dss.cli.main.util.Util;

import java.io.IOException;

/**
 * @author Francesco Pontillo
 *
 * Date: 28/11/13
 * Time: 11.07
 */
public class SignatureCLIModel extends SignatureModel {
    protected SignatureTokenConnection signatureTokenConnection;

    public SignatureTokenConnection createTokenConnection() {
        CLIPasswordInputCallback passwordInput = new CLIPasswordInputCallback();
        SignatureTokenConnection connection = super.createTokenConnection(passwordInput);
        setSignatureTokenConnection(connection);
        return connection;
    }

    public SignatureTokenConnection getSignatureTokenConnection() {
        return signatureTokenConnection;
    }

    public void setSignatureTokenConnection(SignatureTokenConnection signatureTokenConnection) {
        this.signatureTokenConnection = signatureTokenConnection;
    }

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
