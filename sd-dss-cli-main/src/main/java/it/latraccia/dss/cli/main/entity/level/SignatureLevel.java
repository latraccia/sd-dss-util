package it.latraccia.dss.cli.main.entity.level;

import it.latraccia.dss.cli.main.entity.GeneralEnum;

/**
 * Date: 06/12/13
 * Time: 9.32
 *
 * @author Francesco Pontillo
 */
public class SignatureLevel extends GeneralEnum {
    protected static String BES = "BES";
    protected static String EPES = "EPES";
    protected static String T = "T";
    protected static String C = "C";
    protected static String X = "X";
    protected static String XL = "XL";
    protected static String A = "A";
    protected static String LTV = "LTV";

    public SignatureLevel(String value) {
        super(value);
    }
}
