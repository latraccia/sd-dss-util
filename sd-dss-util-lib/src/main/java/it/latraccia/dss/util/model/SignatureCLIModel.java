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

package it.latraccia.dss.util.model;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.applet.io.*;
import eu.europa.ec.markt.dss.applet.util.MOCCAAdapter;
import eu.europa.ec.markt.dss.exception.BadPasswordException;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.token.*;
import eu.europa.ec.markt.dss.validation.CertificateVerifier;
import eu.europa.ec.markt.dss.validation.TrustedListCertificateVerifier;
import eu.europa.ec.markt.dss.validation.certificate.CertificateSource;
import eu.europa.ec.markt.dss.validation.certificate.RemoteCertificateSource;
import eu.europa.ec.markt.dss.validation.crl.CRLSource;
import eu.europa.ec.markt.dss.validation.ocsp.OCSPSource;
import eu.europa.ec.markt.dss.validation.tsp.TSPSource;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.TrustedCertificateSource;
import eu.europa.ec.markt.dss.validation102853.ValidationResourceManager;
import it.latraccia.dss.util.util.Util;

import java.io.File;
import java.net.URL;
import java.security.KeyStoreException;

public class SignatureCLIModel extends SignatureModel {
    protected DigestAlgorithm digestAlgorithm;
    protected String serviceURL;
    protected boolean strictRFC3370;
    protected URL defaultPolicyUrl;
    protected String simpleLevel;

    private static final String TSP_CONTEXT = "/tsp";
    private static final String OCSP_CONTEXT = "/ocsp";
    private static final String CRL_CONTEXT = "/crl";
    private static final String CERTIFICATE_CONTEXT = "/certificate";

    public SignatureCLIModel() {
        super();
    }

    public SignatureCLIModel(String serviceUrl) {
        super();
        setServiceURL(serviceUrl);
    }

    public String getSimpleLevel() {
        return simpleLevel;
    }

    public void setSimpleLevel(String simpleLevel) {
        this.simpleLevel = simpleLevel;
        super.setLevel(getFormat() + "_" + getSimpleLevel());
    }

    public String getServiceURL() {
        return serviceURL;
    }

    /**
     * @param serviceURL the serviceURL to set
     */
    public void setServiceURL(final String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public boolean isStrictRFC3370() {
        return strictRFC3370;
    }

    /**
     * @param strictRFC3370 the strictRFC3370 to set
     */
    public void setStrictRFC3370(final boolean strictRFC3370) {
        this.strictRFC3370 = strictRFC3370;
    }

    /**
     * @return the defaultPolicyUrl for validation. Can be null.
     */
    public URL getDefaultPolicyUrl() {
        if (defaultPolicyUrl == null) {
            return getClass().getResource(ValidationResourceManager.defaultPolicyConstraintsLocation);
        } else {
            return defaultPolicyUrl;
        }
    }

    /**
     * Set the default policy URL for validation. Can be null.
     *
     * @param defaultPolicyUrl the default policy URL to be used
     */
    public void setDefaultPolicyUrl(URL defaultPolicyUrl) {
        this.defaultPolicyUrl = defaultPolicyUrl;
    }

    public boolean hasPkcs11File() {
        final File file = getPkcs11File();
        return file != null && file.exists() && file.isFile();
    }

    public boolean hasPkcs12File() {
        final File file = getPkcs12File();
        return file != null && file.exists() && file.isFile();
    }

    public boolean hasSignaturePolicyAlgo() {
        return !Util.isNullOrEmpty(getSignaturePolicyAlgo());
    }

    public boolean hasSignaturePolicyValue() {
        return !Util.isNullOrEmpty(getSignaturePolicyValue());
    }

    public boolean hasSignatureTokenType() {
        return getTokenType() != null;
    }

    /**
     * Create the token connection.
     * Part of this code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.CertificateStep#init()}.
     * @return the {@link SignatureTokenConnection} created, if any
     * @throws KeyStoreException In case of errors accessing the keystore
     * @throws eu.europa.ec.markt.dss.exception.BadPasswordException If a bad password was given for the token
     */
    public SignatureTokenConnection createTokenConnection() throws KeyStoreException, BadPasswordException {
        PasswordInputCallback passwordInput = new CLIPasswordStoredCallback();
        SignatureTokenConnection connection;

        switch (getTokenType()) {
            case MSCAPI: {
                connection = new MSCAPISignatureToken();
                break;
            }
            case MOCCA: {
                connection = new MOCCAAdapter().createSignatureToken(passwordInput);
                break;
            }
            case PKCS11:
                final File file = getPkcs11File();
                connection = new Pkcs11SignatureToken(file.getAbsolutePath(), getPkcs11Password().toCharArray());
                break;
            case PKCS12:
                connection = new Pkcs12SignatureToken(getPkcs12Password(), getPkcs12File());
                break;
            default:
                throw new RuntimeException("No token connection selected");
        }

        setTokenConnection(connection);
        setPrivateKeys(connection.getKeys());
        setTokenConnection(connection);
        return connection;
    }

    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public void setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public void setDigestAlgorithm(String digestAlgorithm) {
        setDigestAlgorithm(DigestAlgorithm.valueOf(digestAlgorithm));
    }

    public void setPackaging(String packaging) {
        setPackaging(SignaturePackaging.valueOf(packaging));
    }

    public CRLSource getCRLSource() {
        RemoteCRLSource crlSource = new RemoteCRLSource();
        crlSource.setDataLoader(new NativeHTTPDataLoader());
        crlSource.setUrl(getServiceURL() + CRL_CONTEXT);
        return crlSource;
    }

    public OCSPSource getOSCPSource() {
        RemoteOCSPSource ocspSource = new RemoteOCSPSource();
        ocspSource.setUrl(getServiceURL() + OCSP_CONTEXT);
        ocspSource.setDataLoader(new NativeHTTPDataLoader());
        return ocspSource;
    }

    public CertificateSource getCertificateSource(TrustedCertificateSource certificateSource) {
        final RemoteCertificateSource tslCertSource = new RemoteCertificateSource();
        tslCertSource.setDelegate(certificateSource);
        return tslCertSource;
    }

    public CertificateVerifier getCertificateVerifier(CRLSource crlSource, OCSPSource ocspSource,
                                               CertificateSource certificatesSource) {
        eu.europa.ec.markt.dss.validation.TrustedListCertificateVerifier certificateVerifier = new TrustedListCertificateVerifier();
        certificateVerifier.setCrlSource(crlSource);
        certificateVerifier.setOcspSource(ocspSource);
        certificateVerifier.setTrustedListCertificatesSource(certificatesSource);
        return certificateVerifier;
    }

    public TSPSource getTSPSource() {
        final RemoteTSPSource remoteTSPSource = new RemoteTSPSource();
        remoteTSPSource.setUrl(getServiceURL() + TSP_CONTEXT);
        remoteTSPSource.setDataLoader(new NativeHTTPDataLoader());
        return remoteTSPSource;
    }

    public TrustedCertificateSource getCertificateSource102853() {
        final RemoteAppletTSLCertificateSource trustedListsCertificateSource = new RemoteAppletTSLCertificateSource();
        trustedListsCertificateSource.setDataLoader(new NativeHTTPDataLoader());
        trustedListsCertificateSource.setServiceUrl(getServiceURL() + CERTIFICATE_CONTEXT);
        return trustedListsCertificateSource;
    }

    public CommonCertificateVerifier getTrustedListCertificateVerifier102853(CRLSource crlSource, OCSPSource ocspSource,
                                                                      TrustedCertificateSource certificateSource) {
        final CommonCertificateVerifier trustedListCertificateVerifier = new CommonCertificateVerifier();
        trustedListCertificateVerifier.setCrlSource(crlSource);
        trustedListCertificateVerifier.setOcspSource(ocspSource);
        trustedListCertificateVerifier.setTrustedCertSource(certificateSource);
        return trustedListCertificateVerifier;
    }

    /**
     * Return a previously-read password already stored in the model.
     */
    private class CLIPasswordStoredCallback implements PasswordInputCallback {
        public char[] getPassword() {
            return getPkcs11Password().toCharArray();
        }
    }
}
