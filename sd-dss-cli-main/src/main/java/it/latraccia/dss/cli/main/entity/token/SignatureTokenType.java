package it.latraccia.dss.cli.main.entity.token;

import it.latraccia.dss.cli.main.entity.GeneralEnum;

/**
 * Date: 07/12/13
 * Time: 8.46
 *
 * @author Francesco Pontillo
 */
public class SignatureTokenType extends GeneralEnum {
    public static SignatureTokenType PKCS11 = new SignatureTokenType("PKCS11");
    public static SignatureTokenType PKCS12 = new SignatureTokenType("PKCS12");
    public static SignatureTokenType MOCCA = new SignatureTokenType("MOCCA");

    public SignatureTokenType(String value) {
        super(value);
    }
}
