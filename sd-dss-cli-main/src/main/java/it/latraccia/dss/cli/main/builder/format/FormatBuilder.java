package it.latraccia.dss.cli.main.builder.format;

import eu.europa.ec.markt.dss.applet.main.FileType;
import it.latraccia.dss.cli.main.builder.IBuilder;
import it.latraccia.dss.cli.main.entity.format.SignatureFormat;
import it.latraccia.dss.cli.main.entity.level.SignatureLevel;
import it.latraccia.dss.cli.main.entity.packaging.SignaturePackaging;
import it.latraccia.dss.cli.main.exception.SignatureFormatMismatchException;
import it.latraccia.dss.cli.main.exception.SignatureLevelMismatchException;
import it.latraccia.dss.cli.main.exception.SignaturePackagingMismatchException;
import it.latraccia.dss.cli.main.exception.SignatureServiceUrlException;
import it.latraccia.dss.cli.main.model.SignatureCLIModel;
import it.latraccia.dss.cli.main.util.AssertHelper;
import it.latraccia.dss.cli.main.util.Util;

import java.util.HashMap;

/**
 * Date: 06/12/13
 * Time: 10.44
 *
 * @author Francesco Pontillo
 */
public abstract class FormatBuilder<T
        extends SignatureFormat>
        implements IBuilder<FormatBuilder.SignatureFormatLevelPackaging> {

    protected SignatureFormat format;
    protected SignatureLevel level;
    protected SignaturePackaging packaging;
    protected String serviceUrl;

    public SignatureFormatLevelPackaging build() throws SignatureFormatMismatchException, SignaturePackagingMismatchException, SignatureLevelMismatchException {
        SignatureFormatLevelPackaging signatureFormatLevelPackaging = new SignatureFormatLevelPackaging();
        signatureFormatLevelPackaging.setFormat(format);
        signatureFormatLevelPackaging.setLevel(level);
        signatureFormatLevelPackaging.setPackaging(packaging);

        validateSignatureFormat();
        validateSignatureLevel();
        validateSignaturePackaging();

        return signatureFormatLevelPackaging;
    }

    protected void setFormat(SignatureFormat format) {
        this.format = format;
    }

    protected void setLevel(SignatureLevel level) {
        this.level = level;
    }

    protected void setPackaging(SignaturePackaging packaging) {
        this.packaging = packaging;
    }

    protected void setServiceUrl(String serviceUrl) throws SignatureServiceUrlException {
        if (Util.isNullOrEmpty(serviceUrl)) {
            throw new SignatureServiceUrlException();
        }
        this.serviceUrl = serviceUrl;
    }

    public SignatureFormat getFormat() {
        return format;
    }

    public SignatureLevel getLevel() {
        return level;
    }

    public SignaturePackaging getPackaging() {
        return packaging;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * Validate signature format, level and packaging.
     *
     * @throws it.latraccia.dss.cli.main.exception.SignatureFormatMismatchException If there is a mismatch of any kind
     *                                                                              between format, level and packaging
     */
    protected void validateSignatureFormat() throws SignatureFormatMismatchException {
        String signatureFormat = getFormat().getValue();

        // Validate the simple format
        String[] allowedFormats = new String[]{"PAdES", "CAdES", "XAdES", "ASiC-S"};
        if (!AssertHelper.stringMustBeInList(
                "signature format",
                signatureFormat,
                allowedFormats)) {
            throw new SignatureFormatMismatchException();
        }
    }

    /**
     * Validate the signature level accordingly to the format.
     *
     * @throws it.latraccia.dss.cli.main.exception.SignatureLevelMismatchException Thrown if there is a level mismatch
     *                                                                             with the selected format
     */
    protected void validateSignatureLevel() throws SignatureLevelMismatchException {
        // Validate the level
        String signatureFormat = getFormat().getValue();
        String signatureLevel = getLevel().getValue();

        // The map of allowed levels for each simple format
        HashMap<String, String[]> allowedLevelsMap = new HashMap<String, String[]>();
        allowedLevelsMap.put("PAdES", new String[]{"BES", "EPES", "LTV"});
        allowedLevelsMap.put("CAdES", new String[]{"BES", "EPES", "T", "C", "X", "XL", "A"});
        allowedLevelsMap.put("XAdES", new String[]{"BES", "EPES", "T", "C", "X", "XL", "A"});
        allowedLevelsMap.put("ASiC-S", new String[]{"BES", "EPES", "T"});

        // Validate the level for the format set
        if (!AssertHelper.stringMustBeInList(
                "signature level for " + signatureFormat,
                signatureLevel,
                allowedLevelsMap.get(signatureFormat))) {
            throw new SignatureLevelMismatchException();
        }
    }

    /**
     * Validate the signature packaging according to the signature format.
     *
     * @throws it.latraccia.dss.cli.main.exception.SignaturePackagingMismatchException Thrown if there is a packaging
     *                                                                                 mismatch
     */
    protected void validateSignaturePackaging() throws SignaturePackagingMismatchException {
        // Validate the packaging
        String signatureFormat = getFormat().getValue();
        eu.europa.ec.markt.dss.signature.SignaturePackaging signaturePackaging =
                eu.europa.ec.markt.dss.signature.SignaturePackaging.valueOf(getPackaging().getValue());

        // The map of allowed packaging for each simple format
        HashMap<String, eu.europa.ec.markt.dss.signature.SignaturePackaging[]> allowedPackagingMap = new HashMap<String, eu.europa.ec.markt.dss.signature.SignaturePackaging[]>();
        allowedPackagingMap.put("PAdES", new eu.europa.ec.markt.dss.signature.SignaturePackaging[]{eu.europa.ec.markt.dss.signature.SignaturePackaging.ENVELOPED});
        allowedPackagingMap.put("CAdES", new eu.europa.ec.markt.dss.signature.SignaturePackaging[]{eu.europa.ec.markt.dss.signature.SignaturePackaging.ENVELOPING, eu.europa.ec.markt.dss.signature.SignaturePackaging.DETACHED});
        allowedPackagingMap.put("ASiC-S", new eu.europa.ec.markt.dss.signature.SignaturePackaging[]{eu.europa.ec.markt.dss.signature.SignaturePackaging.DETACHED});
        allowedPackagingMap.put("XAdES", new eu.europa.ec.markt.dss.signature.SignaturePackaging[]{eu.europa.ec.markt.dss.signature.SignaturePackaging.ENVELOPING, eu.europa.ec.markt.dss.signature.SignaturePackaging.DETACHED, eu.europa.ec.markt.dss.signature.SignaturePackaging.ENVELOPED});

        // Validate the level for the format set
        if (!AssertHelper.packageMustBeInList(
                "packaging for " + signatureFormat,
                signaturePackaging,
                allowedPackagingMap.get(signatureFormat))) {
            throw new SignaturePackagingMismatchException();
        }
    }

    public class SignatureFormatLevelPackaging {
        protected SignatureFormat format;
        protected SignatureLevel level;
        protected SignaturePackaging packaging;
        protected String serviceUrl;

        public SignatureFormat getFormat() {
            return format;
        }

        public void setFormat(SignatureFormat format) {
            this.format = format;
        }

        public SignatureLevel getLevel() {
            return level;
        }

        public void setLevel(SignatureLevel level) {
            this.level = level;
        }

        public SignaturePackaging getPackaging() {
            return packaging;
        }

        public void setPackaging(SignaturePackaging packaging) {
            this.packaging = packaging;
        }

        public String getServiceUrl() {
            return serviceUrl;
        }

        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }
    }
}
