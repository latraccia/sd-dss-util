package it.latraccia.dss.cli.main.builder.format;

import it.latraccia.dss.cli.main.entity.format.SignatureFormat;
import it.latraccia.dss.cli.main.entity.format.SignaturePAdESFormat;
import it.latraccia.dss.cli.main.entity.level.SignaturePAdESLevel;
import it.latraccia.dss.cli.main.entity.packaging.SignaturePAdESPackaging;
import it.latraccia.dss.cli.main.exception.SignatureServiceUrlException;

/**
 * Date: 06/12/13
 * Time: 11.10
 *
 * @author Francesco Pontillo
 */
public class PAdESFormatBuilder extends FormatBuilder<SignaturePAdESFormat> {

    // FORMAT

    public PAdESFormatBuilder() {
        setFormat(SignatureFormat.PAdES);
    }

    // LEVEL

    public PAdESFormatBuilder BES(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignaturePAdESLevel.BES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public PAdESFormatBuilder EPES(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignaturePAdESLevel.EPES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public PAdESFormatBuilder LTV(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignaturePAdESLevel.LTV);
        setServiceUrl(serviceUrl);
        return this;
    }

    // PACKAGING

    public PAdESFormatBuilder enveloped() {
        setPackaging(SignaturePAdESPackaging.ENVELOPED);
        return this;
    }
}
