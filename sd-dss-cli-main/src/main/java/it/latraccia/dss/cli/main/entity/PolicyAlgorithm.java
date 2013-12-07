package it.latraccia.dss.cli.main.entity;

/**
 * Date: 07/12/13
 * Time: 9.24
 *
 * @author Francesco Pontillo
 */
public class PolicyAlgorithm extends GeneralEnum {
    public static PolicyAlgorithm SHA1 = new PolicyAlgorithm("SHA1");

    public PolicyAlgorithm(String value) {
        super(value);
    }
}
