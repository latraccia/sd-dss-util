package it.latraccia.dss.cli.main.entity.level;

/**
 * Date: 06/12/13
 * Time: 10.23
 *
 * @author Francesco Pontillo
 */
public class SignaturePAdESLevel extends SignatureLevel {
    public static SignaturePAdESLevel BES = new SignaturePAdESLevel(SignatureLevel.BES);
    public static SignaturePAdESLevel EPES = new SignaturePAdESLevel(SignatureLevel.EPES);
    public static SignaturePAdESLevel LTV = new SignaturePAdESLevel(SignatureLevel.LTV);

    public SignaturePAdESLevel(String value) {
        super(value);
    }
}
