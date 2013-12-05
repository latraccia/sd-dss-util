/*
 * SD-DSS-CLI, a Command Line Interface for SD-DSS.
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

package it.latraccia.dss.cli.main;

import com.beust.jcommander.JCommander;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.applet.main.FileType;
import eu.europa.ec.markt.dss.applet.util.MOCCAAdapter;
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
import it.latraccia.dss.cli.main.argument.SignatureArgs;
import it.latraccia.dss.cli.main.exception.*;
import it.latraccia.dss.cli.main.model.ExplicitSignaturePolicyModel;
import it.latraccia.dss.cli.main.model.PKCSModel;
import it.latraccia.dss.cli.main.model.SignatureCLIModel;
import it.latraccia.dss.cli.main.util.AssertHelper;
import it.latraccia.dss.cli.main.util.Util;
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
import java.util.HashMap;
import java.util.List;

public class SignCLI {

    public static void main(String[] args)
            throws FileNotFoundException, SignatureException, KeyStoreException {
        // Read and parse the arguments
        SignatureArgs signatureArgs = new SignatureArgs();
        new JCommander(signatureArgs, args);

        execute(signatureArgs);
    }

    private static FileOutputStream execute(SignatureArgs signatureArgs)
            throws FileNotFoundException, SignatureException, KeyStoreException {
        // Create the signature wizard model
        SignatureCLIModel model = new SignatureCLIModel();

        // Set the parameters inside the SignatureModel, step by step
        setSourceFile(signatureArgs, model);
        setSignatureFormatLevelPackaging(signatureArgs, model);
        setDigestAlgorithm(signatureArgs, model);
        setTokenType(signatureArgs, model);
        setTokenParameters(signatureArgs, model);
        setPrivateKey(model);
        setClaimedRole(signatureArgs, model);
        setPolicy(signatureArgs, model);
        setOutputFile(signatureArgs, model);
        setServiceUrl(signatureArgs, model);

        // If simulate is off
        if (!signatureArgs.isSimulate()) {
            try {
                return signDocument(model);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Validate a source file.
     * Validation from {@link eu.europa.ec.markt.dss.applet.wizard.signature.FileStep#isValid()}
     *
     * @param model The signature model
     * @throws FileNotFoundException Thrown if the file does not exists or the path is not a file
     */
    protected static void validateSourceFile(SignatureCLIModel model) throws FileNotFoundException {
        if (!model.getSelectedFile().exists() || !model.getSelectedFile().isFile()) {
            throw new FileNotFoundException("The source file was not found or it is not a valid file.");
        }
    }

    /**
     * Validate signature format, level and packaging.
     *
     * @param model The signature model
     * @throws SignatureException If there is a mismatch of any kind between format, level and packaging
     */
    protected static void validateSignatureFormat(SignatureCLIModel model) throws SignatureException {
        String signatureFormat = model.getFormat();

        // Validate the simple format
        String[] allowedFormats = new String[]{"PAdES", "CAdES", "XAdES", "ASiC-S"};
        if (!AssertHelper.stringMustBeInList(
                "signature format",
                signatureFormat,
                allowedFormats)) {
            throw new SignatureFormatMismatchException();
        }

        // If the file is not a PDF, the PAdES cannot be selected
        if (model.getFileType() != FileType.PDF) {
            if (!AssertHelper.stringMustNotEqual("signature level", signatureFormat, "PAdES")) {
                throw new SignatureFormatMismatchException();
            }
        }
    }

    /**
     * Validate the signature level accordingly to the format.
     *
     * @param model The signature model
     * @throws SignatureException   Thrown if there is a level mismatch with the selected format
     */
    protected static void validateSignatureLevel(SignatureCLIModel model) throws SignatureException {
        // Validate the level
        String signatureFormat = model.getFormat();
        String signatureLevel = model.getSimpleLevel();

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
     * @param model The signature model
     * @throws SignatureException Thrown if there is a packaging mismatch
     */
    protected static void validateSignaturePackaging(SignatureCLIModel model) throws SignatureException {
        // Validate the packaging
        String signatureFormat = model.getFormat();
        SignaturePackaging signaturePackaging = model.getPackaging();

        // The map of allowed packaging for each simple format
        HashMap<String, SignaturePackaging[]> allowedPackagingMap = new HashMap<String, SignaturePackaging[]>();
        allowedPackagingMap.put("PAdES", new SignaturePackaging[]{SignaturePackaging.ENVELOPED});
        allowedPackagingMap.put("CAdES", new SignaturePackaging[]{SignaturePackaging.ENVELOPING, SignaturePackaging.DETACHED});
        allowedPackagingMap.put("ASiC-S", new SignaturePackaging[]{SignaturePackaging.DETACHED});

        // If the file is not an XML, the XAdES ENVELOPED can't be selected
        if (model.getFileType() != FileType.XML) {
            allowedPackagingMap.put("XAdES", new SignaturePackaging[]{SignaturePackaging.ENVELOPING, SignaturePackaging.DETACHED});
        } else {
            allowedPackagingMap.put("XAdES", new SignaturePackaging[]{SignaturePackaging.ENVELOPING, SignaturePackaging.DETACHED, SignaturePackaging.ENVELOPED});
        }

        // Validate the level for the format set
        if (!AssertHelper.packageMustBeInList(
                "packaging for " + signatureFormat,
                signaturePackaging,
                allowedPackagingMap.get(signatureFormat))) {
            throw new SignaturePackagingMismatchException();
        }
    }

    /**
     * Validate the MOCCA availability.
     * Some of the code has been taken from {@link eu.europa.ec.markt.dss.applet.view.signature.TokenView#doLayout()}.
     *
     * @param model The signature model
     * @throws SignatureException   Thrown if MOCCA is not available
     */
    protected static void validateMoccaAvailability(SignatureCLIModel model) throws SignatureException {
        boolean isMoccaSet = !Util.isNullOrEmpty(model.getMoccaSignatureAlgorithm());
        boolean isMoccaAvailable = new MOCCAAdapter().isMOCCAAvailable();
        if (isMoccaSet && !isMoccaAvailable) {
            System.err.println("MOCCA is not available, please choose another token provider.");
            throw new SignatureMoccaUnavailabilityException();
        }
    }

    /**
     * Validate the MOCCA algorithm if it is SHA1.
     *
     * @param model The signature model
     * @throws SignatureException Thrown if the MOCCA algorithm is not SHA1
     */
    protected static void validateMoccaAlgorithm(SignatureCLIModel model) throws SignatureException {
        if (!AssertHelper.stringMustBeInList(
                "MOCCA algorithm",
                model.getMoccaSignatureAlgorithm(),
                new String[]{"SHA1", "SHA"})) {
            throw new SignatureMoccaAlgorithmMismatchException();
        }
    }

    /**
     * Validate the policy by checking:
     * - signature policy algorithm, must be SHA1
     * - signature level, must be different than BES
     * Some of this code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.PersonalDataStep#init()}.
     *
     * @param model The signature model
     * @throws SignatureException Thrown if the policy algorithm or the signature level doesn't match
     */
    protected static void validatePolicy(SignatureCLIModel model) throws SignatureException {
        if (!Util.isNullOrEmpty(model.getSignaturePolicyAlgo())) {
            if (!AssertHelper.stringMustBeInList(
                    "explicit policy algorithm",
                    model.getSignaturePolicyAlgo(),
                    new String[]{"SHA1"})) {
                throw new SignaturePolicyAlgorithmMismatchException();
            }
        }

        boolean levelBES = model.getSimpleLevel().equalsIgnoreCase("-BES");
        if (model.isSignaturePolicyCheck() && levelBES) {
            throw new SignaturePolicyLevelMismatch();
        }
    }

    protected static void validateServiceUrl(SignatureCLIModel model) throws SignatureException {
        String serviceUrl = model.getServiceURL();
        if (Util.isNullOrEmpty(serviceUrl)) {
            // If the format is PAdES or the level is not one of the accepted
            if (model.getFormat().startsWith("PAdES")
                    || !AssertHelper.isStringInList(
                    model.getSimpleLevel(),
                    new String[] {"BES", "EPES"})) {
                throw new SignatureServiceUrlException();
            }
        }

    }
    /**
     * Set the source file to be signed.
     *
     * @param signatureArgs The input arguments
     * @param model         The signature model
     * @throws FileNotFoundException Thrown if the input file hasn't been found or it is not valid
     */
    protected static void setSourceFile(SignatureArgs signatureArgs, SignatureCLIModel model) throws FileNotFoundException {
        // Set the FileDocument from the user source path
        String sourceFile = signatureArgs.getSource().get(0);
        // Search in resources, then absolute path
        String foundFile = Util.getFileInResourcesOrAbsolutePath(sourceFile);
        model.setSelectedFile(new File(foundFile));
        validateSourceFile(model);
    }

    /**
     * Set the signature format, level and packaging.
     * Some of the code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.SignatureStep#init()}.
     *
     * @param signatureArgs The input arguments
     * @param model         The signature model
     * @throws SignatureException Thrown if there is some kind of signature parameter mismatch
     */
    protected static void setSignatureFormatLevelPackaging(SignatureArgs signatureArgs, SignatureCLIModel model)
            throws SignatureException {
        // Set the signature format
        String format = signatureArgs.getFormat();
        String level = signatureArgs.getLevel();
        SignaturePackaging packaging = signatureArgs.getPackaging();

        if (format != null) {
            model.setFormat(format);
            if (packaging != null) {
                model.setPackaging(packaging);
                if (StringUtils.isNotEmpty(level)) {
                    model.setSimpleLevel(level);
                }
            }
        }

        // Validate the parts of the model
        validateSignatureFormat(model);
        validateSignatureLevel(model);
        validateSignaturePackaging(model);
    }

    /**
     * Set the custom digest algorithm.
     *
     * @param signatureArgs The input arguments
     * @param model         The signature model
     */
    protected static void setDigestAlgorithm(SignatureArgs signatureArgs, SignatureCLIModel model) {
        // Set the digest algorithm, to SHA1 if null
        if (signatureArgs.getDigestAlgorithm() != null) {
            model.setDigestAlgorithm(signatureArgs.getDigestAlgorithm());
        } else {
            model.setDigestAlgorithm(DigestAlgorithm.SHA1);
        }
    }

    /**
     * Set the token (certificate) type; PKCS11, PKCS12, MOCCA, MSAPI.
     * Some of this code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.SignatureStep#getNextStep()}.
     *
     * @param signatureArgs The input arguments
     * @param model         The signature model
     * @throws SignatureException
     */
    protected static void setTokenType(SignatureArgs signatureArgs, SignatureCLIModel model) throws SignatureException {
        SignatureTokenType tokenType = null;
        // Get the parameters
        List<String> pkcs11 = signatureArgs.getPkcs11();
        List<String> pkcs12 = signatureArgs.getPkcs12();
        boolean mscapi = signatureArgs.isMscapi();
        String mocca = signatureArgs.getMocca();

        // Set the token type
        if (pkcs11 != null && pkcs11.size() == 2) {
            tokenType = SignatureTokenType.PKCS11;
        } else if (pkcs12 != null && pkcs12.size() == 2) {
            tokenType = SignatureTokenType.PKCS12;
        } else if (mscapi) {
            tokenType = SignatureTokenType.MSCAPI;
        } else if (!Util.isNullOrEmpty(mocca)) {
            tokenType = SignatureTokenType.MOCCA;
            validateMoccaAvailability(model);
        }
        model.setTokenType(tokenType);
    }

    /**
     * Set some token-specific parameters for getting the certificate:
     * - PKCS11: library file and password
     * - PKCS12: certificate file and password
     * - MOCCA: signature algorithm
     * <p/>
     * Some of the code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.PKCS11Step#isValid()}
     * and from {@link eu.europa.ec.markt.dss.applet.wizard.signature.PKCS12Step#isValid()}.
     *
     * @param signatureArgs The input arguments
     * @param model         The signature model
     * @throws FileNotFoundException If any of the files doesn't exist
     * @throws SignatureException    Thrown if the MOCCA algorithm is not valid
     */
    protected static void setTokenParameters(SignatureArgs signatureArgs, SignatureCLIModel model)
            throws FileNotFoundException, SignatureException {
        // Get the parameters
        List<String> pkcs11 = signatureArgs.getPkcs11();
        List<String> pkcs12 = signatureArgs.getPkcs12();
        SignatureTokenType tokenType = model.getTokenType();
        String mocca = signatureArgs.getMocca();

        // According to the token type, load related assets into the model
        File tokenAsset;
        switch (tokenType) {
            case PKCS11:
                PKCSModel pkcs11Model = new PKCSModel(pkcs11);
                // Search in resources, then absolute path
                tokenAsset = new File(Util.getFileInResourcesOrAbsolutePath(pkcs11Model.getFile()));
                if (tokenAsset.exists()) {
                    // Set the PKCS11 library file
                    model.setPkcs11File(new File(tokenAsset.getAbsolutePath()));
                    // Set the card encryption password
                    model.setPkcs11Password(pkcs11Model.getPassword());
                } else {
                    // Throw exception for non existing PKCS11 library
                    throw new FileNotFoundException("The PKCS11 library could not be found.");
                }
                break;
            case PKCS12:
                PKCSModel pkcs12Model = new PKCSModel(pkcs12);
                // Search in resources, then absolute path
                tokenAsset = new File(Util.getFileInResourcesOrAbsolutePath(pkcs12Model.getFile()));
                if (tokenAsset.exists()) {
                    // Set the PKCS12 file
                    model.setPkcs12File(new File(tokenAsset.getAbsolutePath()));
                    // Set the file encryption password
                    model.setPkcs12Password(pkcs12Model.getPassword());
                } else {
                    // Throw exception for non existing PKCS12 file
                    throw new FileNotFoundException("The PKCS12 private key could not be found.");
                }
                break;
            case MOCCA:
                model.setMoccaSignatureAlgorithm(mocca.toLowerCase());
                // Validate the MOCCA algorithm (sha1 or sha256)
                validateMoccaAlgorithm(model);
                break;
        }
    }

    /**
     * Open the connection to the keystore, read the keys and ask the user for a key.
     * This method executes code taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.CertificateStep#init()}.
     * If no exception is thrown, this method guarantees that a private key has been selected.
     *
     * @param model The signature model
     * @throws KeyStoreException If there are errors accessing the keystore
     */
    protected static void setPrivateKey(SignatureCLIModel model) throws KeyStoreException {
        SignatureTokenConnection connection;
        // Set the connection to the keystore provider
        connection = model.createTokenConnection();
        try {
            connection.getKeys();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // Get the found keys
        List<DSSPrivateKeyEntry> entries = null;
        try {
            entries = model.getTokenConnection().getKeys();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        DSSPrivateKeyEntry key = null;
        // Ask for the right key to be used, if more than one
        if (entries != null) {
            if (entries.size() > 1) {
                // Print the choices
                System.out.println("The following keys have been found:");
                int i = 1;
                for (DSSPrivateKeyEntry k : entries) {
                    String subject = Util.getSubjectDN(k.getCertificate());
                    System.out.println(
                            String.format("[%d] %s", i++, subject));
                }

                // Ask for a choice
                int keyIndex;
                System.out.println("Select the number of the certificate you wish to use:");

                // Read the integer until we get a valid number within the entries' bounds
                keyIndex = Util.readInt(-1, 1, entries.size());
                // Get the key and print a summary
                key = entries.get(keyIndex - 1);
                System.out.println(
                        String.format("Certificate selected: %s",
                                Util.getSubjectDN(key.getCertificate())));
            } else {
                // Use the first one
                key = entries.get(0);
            }
        }

        // Set the key to be used for the signing process
        model.setSelectedPrivateKey(key);
    }

    /**
     * Set the claimed role of the user.
     *
     * @param signatureArgs The input arguments
     * @param model         The signature model
     */
    protected static void setClaimedRole(SignatureArgs signatureArgs, SignatureCLIModel model) {
        model.setClaimedRole(signatureArgs.getClaimedRole());
    }

    /**
     * Set the user personal data.
     * Part of the code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.PersonalDataStep#init()}.
     *
     * @param signatureArgs The input arguments
     * @param model         The signature model
     * @throws SignatureException Thrown if any of the policy parameters are invalid
     */
    protected static void setPolicy(SignatureArgs signatureArgs, SignatureCLIModel model)
            throws SignatureException {

        // Default to no policy
        model.setSignaturePolicyCheck(false);

        // Convert explicit policy data
        ExplicitSignaturePolicyModel explicitPolicy =
                new ExplicitSignaturePolicyModel(signatureArgs.getExplicitPolicy());

        // If the explicit policy data are valid
        if (explicitPolicy.getOID() != null
                && explicitPolicy.getOID().trim().length() > 0
                && explicitPolicy.getHashAlgo() != null) {
            // Set the explicit policy
            model.setSignaturePolicyId(explicitPolicy.getOID());
            model.setSignaturePolicyAlgo(explicitPolicy.getHashAlgo());
            model.setSignaturePolicyValue(explicitPolicy.getHash());

            model.setSignaturePolicyCheck(true);

            // Validate explicit policy parameters
            validatePolicy(model);
        }
    }

    /**
     * Sets the output file path, starting from the original file name and path, the signature format, level and packaging.
     * Some of the called code has been taken from
     * {@link eu.europa.ec.markt.dss.applet.wizard.signature.SaveStep#prepareTargetFileName(java.io.File, eu.europa.ec.markt.dss.signature.SignaturePackaging, String)}.
     *
     * @param signatureArgs The input arguments
     * @param model         The signature model
     */
    protected static void setOutputFile(SignatureArgs signatureArgs, SignatureCLIModel model) {
        // The output path if and as requested by the user
        String destination = signatureArgs.getOutput();

        File outputDir;
        String outputFile;
        File destinationFile;

        // If the destination wasn't set
        if (Util.isNullOrEmpty(destination)) {
            // Use the parent dir of the original file
            outputDir = model.getSelectedFile().getParentFile();
            // Use the suggested file name
            outputFile = getSuggestedFileName(model);
            destinationFile = new File(outputDir, outputFile);
        } else {
            // Tries to understand what destination is
            File outFileOrDir = new File(destination);

            // If the user requested a directory
            if (outFileOrDir.isDirectory()) {
                // Store the file in the outFileOrDir directory
                outputDir = outFileOrDir;
                // Use the suggested file name
                outputFile = getSuggestedFileName(model);
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
     * Set the service URL.
     *
     * @param signatureArgs The input arguments
     * @param model         The signature model
     */
    protected static void setServiceUrl(SignatureArgs signatureArgs, SignatureCLIModel model) throws SignatureException {
        model.setServiceURL(signatureArgs.getUrl());
        validateServiceUrl(model);
    }

    /**
     * Sign the document after all of the parameters have been loaded into the {@link SignatureCLIModel}.
     * Some of the code has been taken from {@link eu.europa.ec.markt.dss.applet.wizard.signature.SignatureWizardController#signDocument()}.
     *
     * @param model The signature model
     * @return The {@link FileOutputStream} of the signed document
     * @throws IOException              Thrown if there is an input/output exception while/reading a file or a
     *                                  network stream
     * @throws NoSuchAlgorithmException Thrown if a specified algorithm isn't available
     */
    private static FileOutputStream signDocument(SignatureCLIModel model)
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
            parameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        } else {
            // Set the custom digest algorithm
            parameters.setDigestAlgorithm(model.getDigestAlgorithm());
        }

        if (model.isSignaturePolicyCheck()) {
            final byte[] hashValue = Base64.decodeBase64(model.getSignaturePolicyValue());
            final SignatureParameters.Policy policy = parameters.getSignaturePolicy();
            policy.setHashValue(hashValue);
            policy.setId(model.getSignaturePolicyId());
            DigestAlgorithm digestAlgo = DigestAlgorithm.forName(model.getSignaturePolicyAlgo());
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
        return output;
    }

    /**
     * Get the suggested target file name.
     *
     * @param model The signature model
     * @return The suggested target File
     */
    private static String getSuggestedFileName(SignatureCLIModel model) {
        return prepareTargetFileName(
                model.getSelectedFile(),
                model.getPackaging(),
                model.getLevel()).getName();
    }

    /**
     * Prepare the target file name.
     * Original code in {@link eu.europa.ec.markt.dss.applet.wizard.signature.SaveStep#prepareTargetFileName(java.io.File, eu.europa.ec.markt.dss.signature.SignaturePackaging, String)}
     *
     * @param file               The selected file to sign
     * @param signaturePackaging The selected packaging
     * @param signatureLevel     The complete signature level (e.g. "CAdES-BES")
     * @return The suggested target File
     */
    private static File prepareTargetFileName(final File file,
                                              final SignaturePackaging signaturePackaging,
                                              final String signatureLevel) {

        final File parentDir = file.getParentFile();
        final String originalName = StringUtils.substringBeforeLast(file.getName(), ".");
        final String originalExtension = "." + StringUtils.substringAfterLast(file.getName(), ".");
        final String format = signatureLevel.toUpperCase();

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
}
