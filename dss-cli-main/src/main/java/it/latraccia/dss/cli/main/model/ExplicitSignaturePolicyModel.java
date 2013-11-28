package it.latraccia.dss.cli.main.model;

import java.util.List;

/**
 * @author Francesco Pontillo
 *
 * Date: 28/11/13
 * Time: 13.21
 */
public class ExplicitSignaturePolicyModel {
    private String OID;
    private String hash;
    private String hashAlgo;

    public ExplicitSignaturePolicyModel(List<String> list) {
        setOID(list.get(0));
        setHash(list.get(1));
        setHashAlgo(list.get(2));
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashAlgo() {
        return hashAlgo;
    }

    public void setHashAlgo(String hashAlgo) {
        this.hashAlgo = hashAlgo;
    }
}
