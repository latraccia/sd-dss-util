package it.latraccia.dss.cli.main.entity.level;

/**
 * Date: 06/12/13
 * Time: 10.23
 *
 * @author Francesco Pontillo
 */
public class SignatureXAdESLevel extends SignatureLevel {
    public static SignatureXAdESLevel BES = new SignatureXAdESLevel(SignatureLevel.BES);
    public static SignatureXAdESLevel EPES = new SignatureXAdESLevel(SignatureLevel.EPES);
    public static SignatureXAdESLevel T = new SignatureXAdESLevel(SignatureLevel.T);
    public static SignatureXAdESLevel C = new SignatureXAdESLevel(SignatureLevel.C);
    public static SignatureXAdESLevel X = new SignatureXAdESLevel(SignatureLevel.X);
    public static SignatureXAdESLevel XL = new SignatureXAdESLevel(SignatureLevel.XL);
    public static SignatureXAdESLevel A = new SignatureXAdESLevel(SignatureLevel.A);

    public SignatureXAdESLevel(String value) {
        super(value);
    }
}
