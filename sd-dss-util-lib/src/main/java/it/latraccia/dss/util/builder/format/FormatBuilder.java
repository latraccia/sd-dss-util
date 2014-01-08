/*
 * SD-DSS-Util, a Utility Library and a Command Line Interface for SD-DSS.
 * Copyright (C) 2013 La Traccia http://www.latraccia.it/en/
 * Developed by Francesco Pontillo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package it.latraccia.dss.util.builder.format;

import it.latraccia.dss.util.builder.IBuilder;
import it.latraccia.dss.util.entity.format.SignatureFormat;
import it.latraccia.dss.util.entity.level.SignatureLevel;
import it.latraccia.dss.util.entity.packaging.SignaturePackaging;
import it.latraccia.dss.util.exception.SignatureFormatMismatchException;
import it.latraccia.dss.util.exception.SignatureLevelMismatchException;
import it.latraccia.dss.util.exception.SignaturePackagingMismatchException;
import it.latraccia.dss.util.util.AssertHelper;

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
        signatureFormatLevelPackaging.setServiceUrl(serviceUrl);

        validateSignatureFormat();
        validateSignatureLevel();
        validateSignaturePackaging();

        return signatureFormatLevelPackaging;
    }

    protected FormatBuilder setFormat(SignatureFormat format) {
        this.format = format;
        return this;
    }

    public FormatBuilder setLevel(SignatureLevel level) {
        this.level = level;
        return this;
    }

    public FormatBuilder setPackaging(SignaturePackaging packaging) {
        this.packaging = packaging;
        return this;
    }

    public FormatBuilder setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
        return this;
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
     * @throws it.latraccia.dss.util.exception.SignatureFormatMismatchException If there is a mismatch of any kind
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
     * @throws it.latraccia.dss.util.exception.SignatureLevelMismatchException Thrown if there is a level mismatch
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
     * @throws it.latraccia.dss.util.exception.SignaturePackagingMismatchException Thrown if there is a packaging
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
