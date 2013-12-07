package it.latraccia.dss.cli.main.builder.token;

import it.latraccia.dss.cli.main.builder.IBuilder;
import it.latraccia.dss.cli.main.entity.MoccaAlgorithm;
import it.latraccia.dss.cli.main.entity.token.SignatureTokenType;
import it.latraccia.dss.cli.main.exception.SignatureMoccaAlgorithmMismatchException;
import it.latraccia.dss.cli.main.exception.SignatureMoccaUnavailabilityException;

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
