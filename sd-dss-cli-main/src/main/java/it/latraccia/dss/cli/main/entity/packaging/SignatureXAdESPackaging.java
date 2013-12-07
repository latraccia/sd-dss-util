package it.latraccia.dss.cli.main.entity.packaging;

/**
 * Date: 06/12/13
 * Time: 10.54
 *
 * @author Francesco Pontillo
 */
public class SignatureXAdESPackaging extends SignaturePackaging {
    public static SignatureXAdESPackaging ENVELOPING = new SignatureXAdESPackaging(SignaturePackaging.ENVELOPING);
    public static SignatureXAdESPackaging ENVELOPED = new SignatureXAdESPackaging(SignaturePackaging.ENVELOPED);
    public static SignatureXAdESPackaging DETACHED = new SignatureXAdESPackaging(SignaturePackaging.DETACHED);

    public SignatureXAdESPackaging(String value) {
        super(value);
    }
}
