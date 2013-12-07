package it.latraccia.dss.cli.main.entity.level;

import eu.europa.ec.markt.dss.validation.report.SignatureLevelXL;

/**
 * Date: 06/12/13
 * Time: 10.23
 *
 * @author Francesco Pontillo
 */
public class SignatureCAdESLevel extends SignatureLevel {
    public static SignatureCAdESLevel BES = new SignatureCAdESLevel(SignatureLevel.BES);
    public static SignatureCAdESLevel EPES = new SignatureCAdESLevel(SignatureLevel.EPES);
    public static SignatureCAdESLevel T = new SignatureCAdESLevel(SignatureLevel.T);
    public static SignatureCAdESLevel C = new SignatureCAdESLevel(SignatureLevel.C);
    public static SignatureCAdESLevel X = new SignatureCAdESLevel(SignatureLevel.X);
    public static SignatureCAdESLevel XL = new SignatureCAdESLevel(SignatureLevel.XL);
    public static SignatureCAdESLevel A = new SignatureCAdESLevel(SignatureLevel.A);

    public SignatureCAdESLevel(String value) {
        super(value);
    }
}
