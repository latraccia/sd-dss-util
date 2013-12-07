package it.latraccia.dss.cli.main.builder.token;

import it.latraccia.dss.cli.main.exception.SignatureMoccaAlgorithmMismatchException;
import it.latraccia.dss.cli.main.exception.SignatureMoccaUnavailabilityException;

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
