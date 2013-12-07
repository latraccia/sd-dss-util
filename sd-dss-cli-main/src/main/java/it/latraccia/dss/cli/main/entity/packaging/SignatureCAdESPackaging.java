package it.latraccia.dss.cli.main.entity.packaging;

/**
 * Date: 06/12/13
 * Time: 10.54
 *
 * @author Francesco Pontillo
 */
public class SignatureCAdESPackaging extends SignaturePackaging {
    public static SignatureCAdESPackaging ENVELOPING = new SignatureCAdESPackaging(SignaturePackaging.ENVELOPING);
    public static SignatureCAdESPackaging DETACHED = new SignatureCAdESPackaging(SignaturePackaging.DETACHED);

    public SignatureCAdESPackaging(String value) {
        super(value);
    }
}
