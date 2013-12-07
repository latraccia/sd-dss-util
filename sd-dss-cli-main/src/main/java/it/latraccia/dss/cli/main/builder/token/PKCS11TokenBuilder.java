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
        String password = pkcs11TokenPasswordCallback.getPassword();
        setPassword(password);

        SignatureToken token = super.build();

        // Get the token asset
        File tokenAsset = new File(Util.getFileInResourcesOrAbsolutePath(getFile()));
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
