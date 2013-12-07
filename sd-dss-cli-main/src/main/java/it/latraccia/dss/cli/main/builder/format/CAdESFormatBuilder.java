package it.latraccia.dss.cli.main.builder.format;

import it.latraccia.dss.cli.main.entity.format.SignatureCAdESFormat;
import it.latraccia.dss.cli.main.entity.format.SignatureFormat;
import it.latraccia.dss.cli.main.entity.level.SignatureCAdESLevel;
import it.latraccia.dss.cli.main.entity.packaging.SignatureCAdESPackaging;
import it.latraccia.dss.cli.main.exception.SignatureServiceUrlException;

/**
 * Date: 06/12/13
 * Time: 11.10
 *
 * @author Francesco Pontillo
 */
public class CAdESFormatBuilder extends FormatBuilder<SignatureCAdESFormat> {

    // FORMAT

    public CAdESFormatBuilder() {
        setFormat(SignatureFormat.CAdES);
    }

    // LEVEL

    public CAdESFormatBuilder BES() {
        setLevel(SignatureCAdESLevel.BES);
        return this;
    }

    public CAdESFormatBuilder EPES() {
        setLevel(SignatureCAdESLevel.EPES);
        return this;
    }

    public CAdESFormatBuilder T(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.T);
        setServiceUrl(serviceUrl);
        return this;
    }

    public CAdESFormatBuilder C(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.C);
        setServiceUrl(serviceUrl);
        return this;
    }

    public CAdESFormatBuilder X(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.BES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public CAdESFormatBuilder XL(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.EPES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public CAdESFormatBuilder A(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.A);
        setServiceUrl(serviceUrl);
        return this;
    }

    // PACKAGING

    public CAdESFormatBuilder enveloping() {
        setPackaging(SignatureCAdESPackaging.ENVELOPING);
        return this;
    }

    public CAdESFormatBuilder detached() {
        setPackaging(SignatureCAdESPackaging.DETACHED);
        return this;
    }
}
