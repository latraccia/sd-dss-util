package it.latraccia.dss.cli.main.entity;

/**
 * Date: 06/12/13
 * Time: 13.51
 *
 * @author Francesco Pontillo
 */
public class DigestAlgorithm extends GeneralEnum {
    public static DigestAlgorithm SHA1 = new DigestAlgorithm("SHA1");
    public static DigestAlgorithm SHA256 = new DigestAlgorithm("SHA256");

    public DigestAlgorithm(String value) {
        super(value);
    }
}
