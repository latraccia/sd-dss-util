package it.latraccia.dss.cli.main.exception;

public class SignatureServiceUrlException extends SignatureException {
    @Override
    public String getMessage() {
        return "The service URL was required but not specified!";
    }
}
