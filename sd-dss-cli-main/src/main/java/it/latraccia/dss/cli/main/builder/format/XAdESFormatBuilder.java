package it.latraccia.dss.cli.main.builder.format;

import it.latraccia.dss.cli.main.entity.format.SignatureFormat;
import it.latraccia.dss.cli.main.entity.format.SignatureXAdESFormat;
import it.latraccia.dss.cli.main.entity.level.SignatureXAdESLevel;
import it.latraccia.dss.cli.main.entity.packaging.SignatureXAdESPackaging;
import it.latraccia.dss.cli.main.exception.SignatureServiceUrlException;

/**
 * Date: 06/12/13
 * Time: 11.10
 *
 * @author Francesco Pontillo
 */
public class XAdESFormatBuilder extends FormatBuilder<SignatureXAdESFormat> {

    // FORMAT

    public XAdESFormatBuilder() {
        setFormat(SignatureFormat.XAdES);
    }

    // LEVEL

    public XAdESFormatBuilder BES() {
        setLevel(SignatureXAdESLevel.BES);
        return this;
    }

    public XAdESFormatBuilder EPES() {
        setLevel(SignatureXAdESLevel.EPES);
        return this;
    }

    public XAdESFormatBuilder T(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.T);
        setServiceUrl(serviceUrl);
        return this;
    }

    public XAdESFormatBuilder C(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.C);
        setServiceUrl(serviceUrl);
        return this;
    }

    public XAdESFormatBuilder X(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.BES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public XAdESFormatBuilder XL(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.EPES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public XAdESFormatBuilder A(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.A);
        setServiceUrl(serviceUrl);
        return this;
    }

    // PACKAGING

    public XAdESFormatBuilder enveloping() {
        setPackaging(SignatureXAdESPackaging.ENVELOPING);
        return this;
    }

    public XAdESFormatBuilder enveloped() {
        setPackaging(SignatureXAdESPackaging.ENVELOPED);
        return this;
    }

    public XAdESFormatBuilder detached() {
        setPackaging(SignatureXAdESPackaging.DETACHED);
        return this;
    }

}
