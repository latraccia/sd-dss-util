package it.latraccia.dss.cli.main.entity.packaging;

/**
 * Date: 06/12/13
 * Time: 10.54
 *
 * @author Francesco Pontillo
 */
public class SignaturePAdESPackaging extends SignaturePackaging {
    public static SignaturePAdESPackaging ENVELOPED = new SignaturePAdESPackaging(SignaturePackaging.ENVELOPED);

    public SignaturePAdESPackaging(String value) {
        super(value);
    }
}
