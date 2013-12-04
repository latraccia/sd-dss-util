/*
 * DSS-CLI, a Command Line Interface for SD-DSS.
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

package it.latraccia.dss.cli.main.argument;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import it.latraccia.dss.cli.main.argument.converter.DigestAlgorithmConverter;
import it.latraccia.dss.cli.main.argument.converter.PackagingConverter;

import java.util.List;

/**
 * Signature arguments, to be processed by JCommander.
 *
 * Date: 27/11/13
 * Time: 10.25
 *
 * @author Francesco Pontillo
 */
@Parameters(separators = "=")
public class SignatureArgs {
    /* MAIN PARAMETERS */
    @Parameter(required = true,
            description = "Source filenames of the files to be signed")
    private List<String> source;

    @Parameter(names = {"-o", "--output"},
            description = "Destination path or file name for the signed document")
    private String output;

    @Parameter(names = {"-u", "--url"}, required = true,
            description = "URL of the DSS Web Service")
    private String url;
    /* END OF MAIN PARAMETERS */

    /* SIGNATURE FORMAT PARAMETERS */
    @Parameter(names = {"-f", "--format"}, required = true,
            description = "Signature format, depending on the file type can be \"CAdES\", \"XAdES\", \"PAdES\" " +
                    "(for PDF files only), \"ASiC-S\"")
    private String format;

    @Parameter(names = {"-l", "--level"}, required = true,
            description = "Signature level, depending on the format can be \"BES\", \"EPES\", \"LTV\", \"T\", \"C\", " +
                    "\"X\", \"XL\", \"A\"")
    private String level;

    @Parameter(names = {"-p", "--packaging"}, required = true,
            description = "Signature packaging, the way it is related with the content being signed. " +
                    "Can be \"ENVELOPED\", \"ENVELOPING\", \"DETACHED\"",
            converter = PackagingConverter.class)
    private SignaturePackaging packaging;

    @Parameter(names = {"-d", "--digest-algorithm"}, converter = DigestAlgorithmConverter.class,
            description = "The algorithm used for digesting. It can be \"SHA1\" (default), \"SHA256\", \"SHA512\"")
    private DigestAlgorithm digestAlgorithm;

    @Parameter(names = {"-w", "--wrap"}, hidden = true,
            description = "Wrap in ASiC-S, if existing, create a specially signed zip file for packaging the unaltered " +
                    "document and  its CAdES signature file in one container. " +
                    "Available only with \"-f=CAdES -p=DETACHED\"")
    private boolean wrapAsics;
    /* END OF SIGNATURE FORMAT PARAMETERS */

    /* SIGNATURE TOKEN PARAMETERS */
    @Parameter(names = {"-p11", "--pkcs11"}, arity = 2,
            description = "Specified if signature token is PKCS#11, evaluated with a previously installed card driver " +
                    "and the card encryption password")
    private List<String> pkcs11;

    @Parameter(names = {"-p12", "--pkcs12"}, arity = 2,
            description = "Specified if signature token is PKCS#12, evaluated with a the file containing the desired " +
                    "signature token and the file encryption password")
    private List<String> pkcs12;

    @Parameter(names = {"-ms", "--mscapi"},
            description = "Specified if signature token is MS CAPI. The list of digital IDs will be loaded from the " +
                    "user Personal Store handled by Windows")
    private boolean mscapi;

    @Parameter(names = {"-m", "--mocca"},
            description = "Specified if signature token is loaded via MOCCA. Can be one of \"SHA1\" or \"SHA256\"")
    private String mocca;
    /* END OF SIGNATURE TOKEN PARAMETERS */

    /* SIGNER CLAIMED ROLE */
    @Parameter(names = {"-sr", "--signer-role"},
            description = "The claimed role of the signer")
    private String claimedRole;
    /* END OF SIGNER CLAIMED ROLE */

    /* POLICY */
    /**
     * @deprecated Implicit policy does not exist as of DSS 3.0.2
     */
    @Parameter(names = {"-spi", "--signature-policy-implicit"},
            description = "If there is a signature policy to be implicitly inferred from the certificate/token")
    private boolean implicitPolicy;

    @Parameter(names = {"-spe", "--signature-policy-explicit"}, arity = 3,
            description = "Specify the policy with, in order, an OID, the base64 hash value of OID and the algorithm " +
                    "used to calculate the hash (only \"SHA1\" is allowed for now)")
    private List<String> explicitPolicy;
    /* END OF POLICY */

    @Parameter(names = {"-s", "--simulate"},
            description = "Loads the parameters and validates them, but does not start the actual signing process")
    private boolean simulate;

    /* GETTERS / SETTERS */
    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public SignaturePackaging getPackaging() {
        return packaging;
    }

    public void setPackaging(SignaturePackaging packaging) {
        this.packaging = packaging;
    }

    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public void setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public boolean isWrapAsics() {
        return wrapAsics;
    }

    public void setWrapAsics(boolean wrapAsics) {
        this.wrapAsics = wrapAsics;
    }

    public List<String> getPkcs11() {
        return pkcs11;
    }

    public void setPkcs11(List<String> pkcs11) {
        this.pkcs11 = pkcs11;
    }

    public List<String> getPkcs12() {
        return pkcs12;
    }

    public void setPkcs12(List<String> pkcs12) {
        this.pkcs12 = pkcs12;
    }

    public boolean isMscapi() {
        return mscapi;
    }

    public void setMscapi(boolean mscapi) {
        this.mscapi = mscapi;
    }

    public String getMocca() {
        return mocca;
    }

    public void setMocca(String mocca) {
        this.mocca = mocca;
    }

    public String getClaimedRole() {
        return claimedRole;
    }

    public void setClaimedRole(String claimedRole) {
        this.claimedRole = claimedRole;
    }

    /**
     * @deprecated Implicit policy does not exist as of DSS 3.0.2
     */
    public boolean isImplicitPolicy() {
        return implicitPolicy;
    }

    /**
     * @deprecated Implicit policy does not exist as of DSS 3.0.2
     */
    public void setImplicitPolicy(boolean implicitPolicy) {
        this.implicitPolicy = implicitPolicy;
    }

    public List<String> getExplicitPolicy() {
        return explicitPolicy;
    }

    public void setExplicitPolicy(List<String> explicitPolicy) {
        this.explicitPolicy = explicitPolicy;
    }

    public boolean isSimulate() {
        return simulate;
    }

    public void setSimulate(boolean simulate) {
        this.simulate = simulate;
    }

    /* END OF GETTERS / SETTERS */
}
