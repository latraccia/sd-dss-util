package it.latraccia.dss.cli.main.entity;

/**
 * Date: 06/12/13
 * Time: 10.30
 *
 * @author Francesco Pontillo
 */
public class GeneralEnum {
    private String value;

    public GeneralEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object otherGeneralEnum) {
        return getValue().equals(((GeneralEnum)otherGeneralEnum).getValue());
    }
}
