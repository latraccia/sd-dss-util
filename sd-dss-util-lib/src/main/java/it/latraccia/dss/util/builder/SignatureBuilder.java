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

package it.latraccia.dss.util.builder;

import eu.europa.ec.markt.dss.applet.main.FileType;
import eu.europa.ec.markt.dss.applet.util.SigningUtils;
import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.SignatureFormat;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.SignatureParameters;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import eu.europa.ec.markt.dss.validation.TrustedListCertificateVerifier;
import it.latraccia.dss.util.builder.format.CAdESFormatBuilder;
import it.latraccia.dss.util.builder.format.FormatBuilder;
import it.latraccia.dss.util.builder.format.XAdESFormatBuilder;
import it.latraccia.dss.util.builder.policy.PolicyBuilder;
import it.latraccia.dss.util.builder.token.MoccaTokenBuilder;
import it.latraccia.dss.util.builder.token.PKCS11TokenBuilder;
import it.latraccia.dss.util.builder.token.PKCS12TokenBuilder;
import it.latraccia.dss.util.builder.token.TokenBuilder;
import it.latraccia.dss.util.entity.DigestAlgorithm;
import it.latraccia.dss.util.entity.MoccaAlgorithm;
import it.latraccia.dss.util.entity.level.SignaturePAdESLevel;
import it.latraccia.dss.util.exception.*;
import it.latraccia.dss.util.model.SignatureCLIModel;
import it.latraccia.dss.util.util.AssertHelper;
import it.latraccia.dss.util.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * Custom builder for signing a document with given parameters.
 * When finished setting all of the parameters, call {@link SignatureBuilder#build()}.
 *
 * Date: 06/12/13
 * Time: 9.37
 *
 * @author Francesco Pontillo
 */
public class SignatureBuilder implements IBuilder<File> {

    static {
        try {
            new SignatureBuilder().setSource("inputfile")
                    .setFormatBuilder(
                            new CAdESFormatBuilder().BES().detached()
                    )
                    .setFormatBuilder(
                            new XAdESFormatBuilder().C("http://localhost:8080").enveloping()
                    )
                    .setDigestAlgorithm(DigestAlgorithm.SHA256)
                    .setTokenBuilder(
                            new PKCS11TokenBuilder().setFile("path/to/library").setPassword("12345")
                    )
                    .setTokenBuilder(
                            new PKCS11TokenBuilder().setFile("path/to/library").setPasswordCallback(new PKCS11TokenBuilder.PKCS11TokenPasswordCallback() {
                                @Override
                                public String getPassword() {
                                    return "87878";
                                }
                            })
                    )
                    .setTokenBuilder(
                            new PKCS12TokenBuilder().setFile("path/to/file").setPassword("12345")
                    )
                    .setTokenBuilder(
                            new MoccaTokenBuilder().setMoccaAlgorithm(MoccaAlgorithm.SHA1)
                    );
        } catch (SignatureServiceUrlException e) {
            e.printStackTrace();
        }
    }

    // All of the signature parameters
    private String source;
    private String target;
    private FormatBuilder formatBuilder;                // This is a builder
    private FormatBuilder.SignatureFormatLevelPackaging signatureFormatLevelPackaging;
    private TokenBuilder tokenBuilder;                  // This is a builder
    private TokenBuilder.SignatureToken token;
    private PolicyBuilder policyBuilder;                // This is a builder
    private PolicyBuilder.SignaturePolicy policy;
    private IDSSPrivateKeyChooser privateKeyChooser;
    private DigestAlgorithm digestAlgorithm;
    private String claimedRole;

    // This model will be built and signed on build()
    private SignatureCLIModel model;

    public SignatureBuilder() {
        model = new SignatureCLIModel();
    }

    public SignatureBuilder setSource(String file) {
        this.source = file;
        return this;
    }

    public SignatureBuilder setSource(File file) {
        this.source = file.getAbsolutePath();
        return this;
    }

    public SignatureBuilder setTarget(String file) {
        this.target = file;
        return this;
    }

    public SignatureBuilder setTarget(File file) {
        this.target = file.getAbsolutePath();
        return this;
    }

    public SignatureBuilder setFormatBuilder(FormatBuilder formatBuilder) {
        this.formatBuilder = formatBuilder;
        return this;
    }

    public SignatureBuilder setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
        return this;
    }

    public SignatureBuilder setTokenBuilder(TokenBuilder tokenBuilder) {
        this.tokenBuilder = tokenBuilder;
        return this;
    }

    public SignatureBuilder setDSSPrivateKeyChooser(IDSSPrivateKeyChooser chooser) {
        this.privateKeyChooser = chooser;
        return this;
    }

    public SignatureBuilder setClaimedRole(String claimedRole) {
        this.claimedRole = claimedRole;
        return this;
    }

    public SignatureBuilder setPolicyBuilder(PolicyBuilder policyBuilder) {
        this.policyBuilder = policyBuilder;
        return this;
    }

    private File simulate() throws IOException, SignatureException, KeyStoreException, NoSuchAlgorithmException {
        return build(true);
    }

    public File build() throws IOException, SignatureException, KeyStoreException, NoSuchAlgorithmException {
        return build(false);
    }

    public File build(boolean simulate) throws IOException, SignatureException, KeyStoreException, NoSuchAlgorithmException {
        InnerSignatureBuilder innerBuilder = new InnerSignatureBuilder();

        // Set the source file
        innerBuilder.setSourceFile(source, model);
        innerBuilder.validateSourceFile(model);

        // Set the signature format, level and packaging
        innerBuilder.setSignatureFormatLevelPackaging(formatBuilder, model);
        // The format must match the file type
        innerBuilder.validateSignatureFormatFileTypeMatch(model);
        // The packaging must match the file type
        innerBuilder.validateSignaturePackagingFileTypeMatch(model);
        // The service URL must be set if some parameters are
        innerBuilder.validateServiceUrl(model);

        // Set the digest algorithm
        innerBuilder.setDigestAlgorithm(digestAlgorithm, model);

        // Set the token and token parameters
        innerBuilder.setToken(tokenBuilder, model);

        // Ensure that a key chooser has been selected
        innerBuilder.validatePrivateKeyChooser();
        // Open the connection to the selected keystore and set the chosen key
        innerBuilder.setPrivateKey(model);

        // Set the claimed role into the model
        innerBuilder.setClaimedRole(claimedRole, model);
        // Set the explicit policy
        innerBuilder.setPolicy(policyBuilder, model);
        // Validate explicit policy parameters
        innerBuilder.validatePolicy(model);

        // Set the target file
        innerBuilder.setTargetFile(target, model);

        File signedFile = null;
        if (!simulate) {
            signedFile = innerBuilder.signDocument(model);
        }

        return signedFile;
    }

    /**
     * Get the suggested target file name.
     * This method is a wrapper around the original {@link it.latraccia.dss.util.builder.SignatureBuilder#prepareTargetFileName(java.io.File, eu.europa.ec.markt.dss.signature.SignaturePackaging, String)}.
     *
     * @return The suggested target file name
     */
    private String getSuggestedFileName() {
        return prepareTargetFileName(
                new File(source),
                SignaturePackaging.valueOf(signatureFormatLevelPackaging.getPackaging().getValue()),
                signatureFormatLevelPackaging.getFormat().getValue()).getName();
    }

    /**
     * Suggest the target file name.
     * Original code in {@link eu.europa.ec.markt.dss.applet.wizard.signature.SaveStep#prepareTargetFileName(java.io.File,
     * eu.europa.ec.markt.dss.signature.SignaturePackaging, String)}
     *
     * @param file               The selected file to sign
     * @param signaturePackaging The selected packaging
     * @param signatureFormat    The complete signature format (e.g. "CAdES")
     * @return The suggested target File
     */
    private File prepareTargetFileName(final File file,
                                              final SignaturePackaging signaturePackaging,
                                              final String signatureFormat) {

        final File parentDir = file.getParentFile();
        final String originalName = StringUtils.substringBeforeLast(file.getName(), ".");
        final String originalExtension = "." + StringUtils.substringAfterLast(file.getName(), ".");
        final String format = signatureFormat.toUpperCase();

        if ((SignaturePackaging.ENVELOPING == signaturePackaging || SignaturePackaging.DETACHED == signaturePackaging) && format.startsWith("XADES")) {
            return new File(parentDir, originalName + "-signed" + ".xml");
        }

        if (format.startsWith("CADES") && !originalExtension.toLowerCase().equals(".p7m")) {
            return new File(parentDir, originalName + originalExtension + ".p7m");
        }

        if (format.startsWith("ASIC")) {
            return new File(parentDir, originalName + originalExtension + ".asics");
        }

        return new File(parentDir, originalName + "-signed" + originalExtension);

    }

    public interface IDSSPrivateKeyChooser {
        public DSSPrivateKeyEntry getDSSPrivateKey(List<DSSPrivateKeyEntry> keys);
    }

    protected class InnerSignatureBuilder {
        /**
         * Set the source file to be signed.
         *
         * @param model         The signature model
         * @throws java.io.FileNotFoundException Thrown if the input file hasn't been found or it is not valid
         */
        protected void setSourceFile(String sourceFile, SignatureCLIModel model) throws FileNotFoundException {
            // Search in resources, then absolute path
            String foundFile = Util.getFileInResourcesOrAbsolutePath(sourceFile);
            model.setSelectedFile(new File(foundFile));
        }

        /**
         * Validate a source file.
         * Validation from {@link eu.europa.ec.markt.dss.applet.wizard.signature.FileStep#isValid()}
         *
         * @param model The signature model
         * @throws FileNotFoundException Thrown if the file does not exists or the path is not a file
         */
        protected void validateSourceFile(SignatureCLIModel model) throws FileNotFoundException {
            if (!model.getSelectedFile().exists() || !model.getSelectedFile().isFile()) {
                throw new FileNotFoundException("The source file was not found or it is not a valid file.");
            }
        }

        /**
         * Set the signature format, level and packaging.
         * Some of the code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.SignatureStep#init()}.
         *
         * @param model         The signature model
         * @throws it.latraccia.dss.util.exception.SignatureException Thrown if there is some kind of signature parameter mismatch
         */
        protected void setSignatureFormatLevelPackaging(FormatBuilder formatBuilder, SignatureCLIModel model)
                throws SignatureException {

            // Build the object
            signatureFormatLevelPackaging = formatBuilder.build();

            // Set the signature format
            String format = signatureFormatLevelPackaging.getFormat().getValue();
            String level = signatureFormatLevelPackaging.getLevel().getValue();
            String packaging = signatureFormatLevelPackaging.getPackaging().getValue();

            if (format != null) {
                model.setFormat(format);
                if (packaging != null) {
                    model.setPackaging(packaging);
                    if (StringUtils.isNotEmpty(level)) {
                        model.setSimpleLevel(level);
                    }
                }
            }

            // Set the service URL and validate it
            model.setServiceURL(signatureFormatLevelPackaging.getServiceUrl());
        }

        /**
         * Validate the signature format according to the signature format.
         *
         * @param model The signature model
         * @throws SignatureFormatMismatchException Thrown if there is a format mismatch
         */
        protected void validateSignatureFormatFileTypeMatch(SignatureCLIModel model) throws SignatureFormatMismatchException {
            // If the file is not a PDF, the PAdES cannot be selected
            if (model.getFileType() != FileType.PDF) {
                if (!AssertHelper.stringMustNotEqual("signature level", model.getFormat(), "PAdES")) {
                    throw new SignatureFormatMismatchException();
                }
            }
        }

        /**
         * Validate the signature packaging according to the signature format.
         *
         * @param model The signature model
         * @throws SignaturePackagingMismatchException Thrown if there is a packaging mismatch
         */
        protected void validateSignaturePackagingFileTypeMatch(SignatureCLIModel model) throws SignaturePackagingMismatchException {
            // Validate the packaging
            String signatureFormat = model.getFormat();
            SignaturePackaging signaturePackaging = model.getPackaging();

            if (signatureFormat.equals(it.latraccia.dss.util.entity.format.SignatureFormat.XAdES)
                    && (model.getFileType() != FileType.XML)
                    && (signaturePackaging.equals(signaturePackaging))) {
                throw new SignaturePackagingMismatchException();
            }
        }

        /**
         * Validate the service URL by requiring it if PAdES is selected as format or
         * the level is any different then BES, EPES.
         *
         * @param model The signature model
         * @throws SignatureServiceUrlException Thrown if the service URL is required but was not specified
         */
        protected void validateServiceUrl(SignatureCLIModel model) throws SignatureServiceUrlException {
            String serviceUrl = model.getServiceURL();
            if (Util.isNullOrEmpty(serviceUrl)) {
                // If the format is PAdES or the level is not one of the accepted
                if (model.getFormat().startsWith(it.latraccia.dss.util.entity.format.SignatureFormat.PAdES.getValue())
                        || !AssertHelper.isStringInList(
                        model.getSimpleLevel(),
                        new String[]{
                                SignaturePAdESLevel.BES.getValue(),
                                SignaturePAdESLevel.EPES.getValue()
                        })) {
                    throw new SignatureServiceUrlException();
                }
            }

        }

        /**
         * Set the custom digest algorithm.
         *
         * @param model         The signature model
         */
        protected void setDigestAlgorithm(DigestAlgorithm digestAlgorithm, SignatureCLIModel model) {
            // Set the digest algorithm, to SHA1 if null
            if (digestAlgorithm != null) {
                model.setDigestAlgorithm(digestAlgorithm.getValue());
            } else {
                model.setDigestAlgorithm(eu.europa.ec.markt.dss.DigestAlgorithm.SHA1);
            }
        }

        /**
         * Set the token (certificate) type; PKCS11, PKCS12, MOCCA, MSAPI.
         * Set some token-specific parameters for getting the certificate:
         * - PKCS11: library file and password
         * - PKCS12: certificate file and password
         * - MOCCA: signature algorithm
         *
         * Some of this code has been taken from:
         *  - {@link eu.europa.ec.markt.dss.applet.wizard.signature.SignatureStep#getNextStep()}
         *  - {@link eu.europa.ec.markt.dss.applet.wizard.signature.PKCS11Step#isValid()}
         *  - {@link eu.europa.ec.markt.dss.applet.wizard.signature.PKCS12Step#isValid()}
         *
         * @param model         The signature model
         * @throws FileNotFoundException If any of the files doesn't exist
         * @throws SignatureException    Thrown if the MOCCA algorithm is not valid
         */
        protected void setToken(TokenBuilder tokenBuilder, SignatureCLIModel model) throws SignatureMoccaAlgorithmMismatchException, SignatureMoccaUnavailabilityException, FileNotFoundException {
            // Set the token file, password, do some pre-validation
            token = tokenBuilder.build();

            SignatureTokenType tokenType = SignatureTokenType.valueOf(token.getTokenType().getValue());
            // Set the token type
            model.setTokenType(tokenType);

            // If the algorithm is MOCCA, set the model MOCCA algorithm
            if (token.getTokenType().equals(it.latraccia.dss.util.entity.token.SignatureTokenType.MOCCA)) {
                model.setMoccaSignatureAlgorithm(token.getMoccaAlgorithm().getValue());
            }

            switch (tokenType) {
                case PKCS11:
                    // Set the PKCS11 library file
                    model.setPkcs11File(token.getTokenAsset());
                    // Set the card encryption password
                    model.setPkcs11Password(token.getPassword());
                    break;
                case PKCS12:
                    // Set the PKCS12 file
                    model.setPkcs12File(token.getTokenAsset());
                    // Set the file encryption password
                    model.setPkcs12Password(token.getPassword());
                    break;
                case MOCCA:
                    model.setMoccaSignatureAlgorithm(token.getMoccaAlgorithm().getValue().toLowerCase());
                    break;
            }
        }

        /**
         * Ensure that the private key chooser has been set.
         * @throws java.lang.NullPointerException if the chooser was not set
         */
        protected void validatePrivateKeyChooser() throws NullPointerException {
            if (privateKeyChooser == null) {
                throw new NullPointerException("No private key chooser has been set, can't proceed!");
            }
        }

        /**
         * Open the connection to the keystore, read all of the keys and selects one.
         * This method executes code taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.CertificateStep#init()}.
         * If no exception is thrown, this method guarantees that a private key has been selected.
         *
         * @param model The signature model
         * @throws java.security.KeyStoreException If there are errors accessing the keystore
         */
        protected void setPrivateKey(SignatureCLIModel model) throws KeyStoreException {
            // Create the connection to the keystore provider
            SignatureTokenConnection connection = model.createTokenConnection();
            // Get the keys
            List<DSSPrivateKeyEntry> entries = connection.getKeys();

            DSSPrivateKeyEntry key = null;
            // Get the selected key to be used, if more than one
            if (entries.size() > 1) {
                key = privateKeyChooser.getDSSPrivateKey(entries);
            } else {
                key = entries.get(0);
            }

            // Set the key to be used for the signing process
            model.setSelectedPrivateKey(key);
        }

        /**
         * Set the claimed role of the user.
         *
         * @param claimedRole   The claimed role
         * @param model         The signature model
         */
        protected void setClaimedRole(String claimedRole, SignatureCLIModel model) {
            model.setClaimedRole(claimedRole);
        }

        /**
         * Set the user personal data.
         * Part of the code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.PersonalDataStep#init()}.
         *
         * @param policyBuilder The {@link it.latraccia.dss.util.builder.policy.PolicyBuilder} that will be built to get the set values
         * @param model         The signature model
         * @throws SignatureException Thrown if any of the policy parameters are invalid
         */
        protected void setPolicy(PolicyBuilder policyBuilder, SignatureCLIModel model)
                throws SignatureException {

            // Default to no policy
            model.setSignaturePolicyCheck(false);

            policy = policyBuilder.build();

            // If the explicit policy data are valid
            if (policy.getOid() != null
                    && policy.getOid().trim().length() > 0
                    && policy.getPolicyAlgorithm() != null) {
                // Set the explicit policy
                model.setSignaturePolicyId(policy.getOid());
                model.setSignaturePolicyAlgo(policy.getPolicyAlgorithm().getValue());
                model.setSignaturePolicyValue(policy.getHash());

                model.setSignaturePolicyCheck(true);
            }
        }

        /**
         * Validate the policy by checking that the signature level is different than BES.
         * Some of this code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.PersonalDataStep#init()}.
         *
         * @param model The signature model
         * @throws SignaturePolicyLevelMismatch              Thrown if the signature level doesn't match
         */
        protected void validatePolicy(SignatureCLIModel model) throws SignaturePolicyLevelMismatch {
            boolean levelBES = model.getSimpleLevel().equalsIgnoreCase("BES");
            if (model.isSignaturePolicyCheck() && levelBES) {
                throw new SignaturePolicyLevelMismatch();
            }
        }

        /**
         * Sets the output file path, starting from the original file name and path, the signature format, level and
         * packaging.
         * Some of the called code has been taken from
         * {@link eu.europa.ec.markt.dss.applet.wizard.signature.SaveStep#prepareTargetFileName(java.io.File,
         * eu.europa.ec.markt.dss.signature.SignaturePackaging, String)}.
         *
         * @param targetFile    The target file to set
         * @param model         The signature model
         */
        protected void setTargetFile(String targetFile, SignatureCLIModel model) {
            // The output path if and as requested by the user
            String destination = targetFile;

            File outputDir;
            String outputFile;
            File destinationFile;

            // If the destination wasn't set
            if (Util.isNullOrEmpty(destination)) {
                // Use the parent dir of the original file
                outputDir = model.getSelectedFile().getParentFile();
                // Use the suggested file name
                outputFile = getSuggestedFileName();
                destinationFile = new File(outputDir, outputFile);
            } else {
                // Tries to understand what destination is
                File outFileOrDir = new File(destination);

                // If the user requested a directory
                if (outFileOrDir.isDirectory()) {
                    // Store the file in the outFileOrDir directory
                    outputDir = outFileOrDir;
                    // Use the suggested file name
                    outputFile = getSuggestedFileName();
                    destinationFile = new File(outputDir, outputFile);
                } else {
                    // If the user explicitly requested a file name and directory, proceed regularly
                    destinationFile = outFileOrDir;
                }
            }

            // Set the user-selected destination path or file
            model.setTargetFile(destinationFile);
        }

        /**
         * Sign the document after all of the parameters have been loaded into the {@link SignatureCLIModel}.
         * Some of the code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.SignatureWizardController#signDocument()}.
         *
         * @param model The signature model
         * @return The {@link java.io.FileOutputStream} of the signed document
         * @throws java.io.IOException              Thrown if there is an input/output exception while/reading a file or a
         *                                  network stream
         * @throws java.security.NoSuchAlgorithmException Thrown if a specified algorithm isn't available
         */
        private File signDocument(SignatureCLIModel model)
                throws IOException, NoSuchAlgorithmException {
            final File fileToSign = model.getSelectedFile();
            final SignatureTokenConnection tokenConnection = model.getTokenConnection();
            final DSSPrivateKeyEntry privateKey = model.getSelectedPrivateKey();

            final SignatureParameters parameters = new SignatureParameters();
            parameters.setSigningDate(new Date());
            parameters.setPrivateKeyEntry(privateKey);
            parameters.setSignatureFormat(SignatureFormat.valueByName(model.getLevel()));
            parameters.setSignaturePackaging(model.getPackaging());

            if (model.isClaimedCheck()) {
                parameters.setClaimedSignerRole(model.getClaimedRole());
            }

            String moccaSignatureAlgorithm = model.getMoccaSignatureAlgorithm();
            // If a MOCCA algorithm is selected, ensure it is SHA256
            if ("SHA256".equalsIgnoreCase(moccaSignatureAlgorithm)) {
                parameters.setDigestAlgorithm(eu.europa.ec.markt.dss.DigestAlgorithm.SHA256);
            } else {
                // Set the custom digest algorithm
                parameters.setDigestAlgorithm(model.getDigestAlgorithm());
            }

            if (model.isSignaturePolicyCheck()) {
                final byte[] hashValue = Base64.decodeBase64(model.getSignaturePolicyValue());
                final SignatureParameters.Policy policy = parameters.getSignaturePolicy();
                policy.setHashValue(hashValue);
                policy.setId(model.getSignaturePolicyId());
                eu.europa.ec.markt.dss.DigestAlgorithm digestAlgo = eu.europa.ec.markt.dss.DigestAlgorithm.forName(model.getSignaturePolicyAlgo());
                policy.setDigestAlgo(digestAlgo);
            }

            FileOutputStream output = new FileOutputStream(model.getTargetFile());

            final TrustedListCertificateVerifier certificateVerifier = (TrustedListCertificateVerifier) model.getCertificateVerifier(
                    model.getCRLSource(), model.getOSCPSource(), model.getCertificateSource(model.getCertificateSource102853())
            );

            final DSSDocument signedDocument;
            try {
                signedDocument = SigningUtils
                        .signDocument(fileToSign, parameters, model.getTSPSource(), certificateVerifier, tokenConnection, privateKey);
            } catch (DSSException exception) {
                System.err.println(exception.getMessage());
                throw exception;
            }

            IOUtils.copy(signedDocument.openStream(), output);
            output.close();
            System.out.println("SUCCESS.");
            System.out.println(String.format("Signed file: %s", model.getTargetFile().getAbsoluteFile()));
            return fileToSign;
        }
    }
}
