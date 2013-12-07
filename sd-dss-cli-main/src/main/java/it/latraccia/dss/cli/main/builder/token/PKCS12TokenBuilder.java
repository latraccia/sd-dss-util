package it.latraccia.dss.cli.main.builder.token;

import it.latraccia.dss.cli.main.entity.token.SignatureTokenType;
import it.latraccia.dss.cli.main.exception.SignatureMoccaAlgorithmMismatchException;
import it.latraccia.dss.cli.main.exception.SignatureMoccaUnavailabilityException;
import it.latraccia.dss.cli.main.util.Util;

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

        File tokenAsset = new File(Util.getFileInResourcesOrAbsolutePath(getFile()));
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
