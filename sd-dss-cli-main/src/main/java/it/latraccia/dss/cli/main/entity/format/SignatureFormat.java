package it.latraccia.dss.cli.main.entity.format;

import it.latraccia.dss.cli.main.entity.GeneralEnum;

/**
 * Date: 06/12/13
 * Time: 9.32
 *
 * @author Francesco Pontillo
 */
public class SignatureFormat extends GeneralEnum {
    public static SignatureFormat XAdES = new SignatureXAdESFormat("XAdES");
    public static SignatureFormat CAdES = new SignatureCAdESFormat("CAdES");
    public static SignatureFormat PAdES = new SignaturePAdESFormat("PAdES");

    public SignatureFormat(String value) {
        super(value);
    }
}
