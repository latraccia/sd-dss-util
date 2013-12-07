package it.latraccia.dss.cli.main.entity.packaging;

import it.latraccia.dss.cli.main.entity.GeneralEnum;

/**
 * Date: 06/12/13
 * Time: 9.36
 *
 * @author Francesco Pontillo
 */
public class SignaturePackaging extends GeneralEnum {
    protected static String ENVELOPING = "ENVELOPING";
    protected static String ENVELOPED = "ENVELOPED";
    protected static String DETACHED = "DETACHED";

    public SignaturePackaging(String value) {
        super(value);
    }
}
