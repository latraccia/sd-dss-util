package it.latraccia.dss.cli.main.model;

import java.util.List;

/**
 * @author Francesco Pontillo
 *         <p/>
 *         Date: 27/11/13
 *         Time: 13.22
 */
public class PKCS12Model {
    private String file;
    private String password;

    public PKCS12Model(List<String> list) {
        setFile(list.get(0));
        setPassword(list.get(1));
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
