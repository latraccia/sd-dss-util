package it.latraccia.dss.cli.main.entity;

/**
 * Date: 07/12/13
 * Time: 9.07
 *
 * @author Francesco Pontillo
 */
public class MoccaAlgorithm extends GeneralEnum {
    public static MoccaAlgorithm SHA1 = new MoccaAlgorithm("SHA1");

    public MoccaAlgorithm(String value) {
        super(value);
    }
}
