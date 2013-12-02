/*
 * DSS-CLI, a Command Line Interface for SD-DSS.
 * Copyright (C) 2013 La Traccia
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
import eu.europa.ec.markt.dss.applet.MOCCAAdapter;
import eu.europa.ec.markt.dss.applet.model.Filetype;
import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ec.markt.dss.signature.*;
import eu.europa.ec.markt.dss.signature.cades.CAdESService;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import it.latraccia.dss.cli.main.argument.SignatureArgs;
import it.latraccia.dss.cli.main.argument.converter.DigestAlgorithmConverter;
import it.latraccia.dss.cli.main.exception.*;
import it.latraccia.dss.cli.main.model.ExplicitSignaturePolicyModel;
import it.latraccia.dss.cli.main.model.PKCS12Model;
import it.latraccia.dss.cli.main.model.SignatureCLIModel;
import it.latraccia.dss.cli.main.util.AssertHelper;
import it.latraccia.dss.cli.main.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;

import java.io.*;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * CLI for signing documents by using the DSS server and libraries.
 * Call this CLI with
 * java SignCLI [parameters]
 * See README.md for the complete documentation.
 *
 * Date: 26/11/13
 * Time: 11.30
 *
 * @author Francesco Pontillo
 */
public class SignCLI {

    public static void main(String[] args) throws FileNotFoundException, SignatureException {
        // Read and parse the arguments
        SignatureArgs signatureArgs = new SignatureArgs();
        new JCommander(signatureArgs, args);

        execute(signatureArgs);
    }

    private static FileOutputStream execute(SignatureArgs signatureArgs)
            throws FileNotFoundException, SignatureException {
        // Create the signature wizard model
        SignatureCLIModel model = new SignatureCLIModel("http://localhost:9090/service");

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

        // If simulate is off
        if (!signatureArgs.isSimulate()) {
            try {
                return signAndSaveFile(model);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    protected static void validateSignatureFormat(SignatureCLIModel model) throws SignatureException {
        String signatureFormat = model.getSignatureSimpleFormat();

        // Validate the simple format
        String[] allowedFormats = new String[]{"PAdES", "CAdES", "XAdES", "ASiC-S"};
        if (!AssertHelper.stringMustBeInList(
                "signature format",
                signatureFormat,
                allowedFormats)) {
            throw new SignatureFormatMismatchException();
        }

        // If the file is not a PDF, the PAdES cannot be selected
        if (model.getOriginalFiletype() != Filetype.PDF) {
            if (!AssertHelper.stringMustNotEqual("signature level", signatureFormat, "PAdES")) {
                throw new SignatureFormatMismatchException();
            }
        }
    }

    protected static void validateSignatureLevel(SignatureCLIModel model) throws SignatureException {
        // Validate the level
        String signatureFormat = model.getSignatureSimpleFormat();
        String signatureLevel = model.getSignatureLevel();

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

    protected static void validateSignaturePackaging(SignatureCLIModel model) throws SignatureException {
        // Validate the packaging
        String signatureFormat = model.getSignatureSimpleFormat();
        SignaturePackaging signaturePackaging = model.getPackaging();

        // The map of allowed packaging for each simple format
        HashMap<String, SignaturePackaging[]> allowedPackagingMap = new HashMap<String, SignaturePackaging[]>();
        allowedPackagingMap.put("PAdES", new SignaturePackaging[]{SignaturePackaging.ENVELOPED});
        allowedPackagingMap.put("CAdES", new SignaturePackaging[]{SignaturePackaging.ENVELOPING, SignaturePackaging.DETACHED});
        allowedPackagingMap.put("ASiC-S", new SignaturePackaging[]{SignaturePackaging.DETACHED});

        // If the file is not an XML, the XAdES ENVELOPED can't be selected
        if (model.getOriginalFiletype() != Filetype.XML) {
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

    protected static void validateMoccaAvailability(SignatureCLIModel model) throws SignatureException {
        boolean isMoccaSet = !Util.isNullOrEmpty(model.getMoccaSignatureAlgorithm());
        boolean isMoccaAvailable = new MOCCAAdapter().isMOCCAAvailable();
        if (isMoccaSet && !isMoccaAvailable) {
            System.err.println("MOCCA is not available, please choose another token provider.");
            throw new SignatureMoccaUnavailabilityException();
        }
    }

    protected static void validateMoccaAlgorithm(SignatureCLIModel model) throws SignatureException {
        if (!AssertHelper.stringMustBeInList(
                "MOCCA algorithm",
                model.getMoccaSignatureAlgorithm(),
                new String[]{"SHA1", "SHA"})) {
            throw new SignatureMoccaAlgorithmMismatchException();
        }
    }

    protected static void validatePolicy(SignatureCLIModel model) throws SignatureException {
        if (model.getSignaturePolicyType() == SignaturePolicy.EXPLICIT) {
            if (!AssertHelper.stringMustBeInList(
                    "explicit policy algorithm",
                    model.getSignaturePolicyAlgo(),
                    new String[]{"SHA1"})) {
                throw new SignaturePolicyAlgorithmMismatchException();
            }
        }
    }

    protected static void setSourceFile(SignatureArgs signatureArgs, SignatureCLIModel model) {
        // Set the FileDocument from the user source path
        String sourceFile = signatureArgs.getSource().get(0);
        model.setOriginalFile(new FileDocument(sourceFile));
    }

    protected static void setSignatureFormatLevelPackaging(SignatureArgs signatureArgs, SignatureCLIModel model)
            throws SignatureException {
        // Set the signature format
        String format = signatureArgs.getFormat();
        String level = signatureArgs.getLevel();
        SignaturePackaging packaging = signatureArgs.getPackaging();
        model.setSignatureSimpleFormat(format);
        model.setSignatureLevel(level);
        model.setPackaging(packaging);

        // Validate the parts of the model
        validateSignatureFormat(model);
        validateSignatureLevel(model);
        validateSignaturePackaging(model);
    }

    protected static void setDigestAlgorithm(SignatureArgs signatureArgs, SignatureCLIModel model) {
        // Set the digest algorithm, to SHA1 if null
        if (signatureArgs.getDigestAlgorithm() != null) {
            model.setDigestAlgorithm(signatureArgs.getDigestAlgorithm());
        } else {
            model.setDigestAlgorithm(DigestAlgorithm.SHA1);
        }
    }

    protected static void setTokenType(SignatureArgs signatureArgs, SignatureCLIModel model) throws SignatureException {
        SignatureTokenType tokenType = null;
        // Get the parameters
        String pkcs11 = signatureArgs.getPkcs11();
        List<String> pkcs12 = signatureArgs.getPkcs12();
        boolean mscapi = signatureArgs.isMscapi();
        String mocca = signatureArgs.getMocca();

        // Set the token type
        if (!Util.isNullOrEmpty(pkcs11)) {
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

    protected static void setTokenParameters(SignatureArgs signatureArgs, SignatureCLIModel model)
            throws FileNotFoundException, SignatureException {
        // Get the parameters
        String pkcs11 = signatureArgs.getPkcs11();
        List<String> pkcs12 = signatureArgs.getPkcs12();
        SignatureTokenType tokenType = model.getTokenType();
        String mocca = signatureArgs.getMocca();

        // According to the token type, load related assets into the model
        File tokenAsset;
        switch (tokenType) {
            case PKCS11:
                tokenAsset = new File(pkcs11);
                if (tokenAsset.exists()) {
                    model.setPkcs11LibraryPath(tokenAsset.getAbsolutePath());
                } else {
                    // Throw exception for non existing PKCS11 library
                    throw new FileNotFoundException("The PKCS11 library could not be found.");
                }
                break;
            case PKCS12:
                PKCS12Model pkcs12Model = new PKCS12Model(pkcs12);
                tokenAsset = new File(pkcs12Model.getFile());
                if (tokenAsset.exists()) {
                    // Set the PKCS12 file
                    model.setPkcs12FilePath(tokenAsset.getAbsolutePath());
                    // Set the file encryption password
                    model.setPkcs12Password(pkcs12Model.getPassword().toCharArray());
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

    protected static void setPrivateKey(SignatureCLIModel model) {
        // Set the connection to the keystore provider
        SignatureTokenConnection connection;
        connection = model.createTokenConnection();
        try {
            connection.getKeys();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // Get the found keys
        List<DSSPrivateKeyEntry> entries = null;
        try {
            entries = model.getSignatureTokenConnection().getKeys();
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
        model.setPrivateKey(key);
    }

    protected static void setClaimedRole(SignatureArgs signatureArgs, SignatureCLIModel model) {
        model.setClaimedRole(signatureArgs.getClaimedRole());
    }

    protected static void setPolicy(SignatureArgs signatureArgs, SignatureCLIModel model) throws SignatureException {
        // Default to no policy
        model.setSignaturePolicyType(SignaturePolicy.NO_POLICY);

        if (signatureArgs.isImplicitPolicy()) {
            // Set the implicit policy
            model.setSignaturePolicyType(SignaturePolicy.IMPLICIT);
        } else {
            // Convert explicit policy data
            ExplicitSignaturePolicyModel explicitPolicy =
                    new ExplicitSignaturePolicyModel(signatureArgs.getExplicitPolicy());
            // If the explicit policy data are valid
            if (explicitPolicy.getOID() != null
                    && explicitPolicy.getOID().trim().length() > 0
                    && explicitPolicy.getHashAlgo() != null) {
                // Set the explicit policy
                model.setSignaturePolicy(explicitPolicy.getOID());
                model.setSignaturePolicyAlgo(explicitPolicy.getHashAlgo());
                model.setSignaturePolicyValue(Base64.decodeBase64(explicitPolicy.getHash()));
                model.setSignaturePolicyType(SignaturePolicy.EXPLICIT);
                // Validate explicit policy parameters
                validatePolicy(model);
            }
        }
    }

    protected static void setOutputFile(SignatureArgs signatureArgs, SignatureCLIModel model) {
        // The output path if and as requested by the user
        String destination = signatureArgs.getOutput();

        File outputDir;
        String outputFile;
        File destinationFile;

        // If the destination wasn't set
        if (Util.isNullOrEmpty(destination)) {
            // Use the parent dir of the original file
            outputDir = model.getOriginalFile().getParentFile();
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
        model.setSignedFile(destinationFile);
    }

    private static FileOutputStream signAndSaveFile(SignatureCLIModel model)
            throws NoSuchAlgorithmException, IOException {
        SignatureTokenConnection connection;

        DocumentSignatureService service = model.createDocumentSignatureService();

        Document document = model.getOriginalFile();

        SignatureParameters parameters = new SignatureParameters();
        parameters.setSigningDate(new Date());
        parameters.setSigningCertificate(model.getPrivateKey().getCertificate());
        if (model.getPrivateKey().getCertificateChain() != null) {
            parameters.setCertificateChain(Arrays.asList((X509Certificate[]) model.getPrivateKey().getCertificateChain()));
        }

        //noinspection deprecation
        parameters.setSignatureFormat(model.getSignatureFormat());
        parameters.setSignaturePackaging(model.getPackaging());

        // BEGIN: Added to 2.0.2

        String moccaSignatureAlgorithm = model.getMoccaSignatureAlgorithm();
        if (moccaSignatureAlgorithm != null && "sha256".equalsIgnoreCase(moccaSignatureAlgorithm)) {
            parameters.setDigestAlgorithm(new DigestAlgorithmConverter().convert(model.getMoccaSignatureAlgorithm()));
        } else {
            parameters.setDigestAlgorithm(model.getDigestAlgorithm());
        }
        parameters.setSignatureAlgorithm(model.getPrivateKey().getSignatureAlgorithm());

        // END:
        parameters.setClaimedSignerRole(model.getClaimedRole());
        parameters.setSignaturePolicy(model.getSignaturePolicyType());
        parameters.setSignaturePolicyId(model.getSignaturePolicy());
        parameters.setSignaturePolicyHashValue(model.getSignaturePolicyValue());
        parameters.setSignaturePolicyHashAlgo(model.getSignaturePolicyAlgo());

        connection = model.createTokenConnection();

        Document contentInCMS = null;
        if (service instanceof CAdESService && parameters.getSignaturePackaging() == SignaturePackaging.ENVELOPING) {

            FileInputStream original = null;
            try {
                CMSSignedData cmsData = new CMSSignedData(model.getOriginalFile().openStream());
                if (cmsData.getSignedContent() != null && cmsData.getSignedContent().getContent() != null) {
                    ByteArrayOutputStream buf = new ByteArrayOutputStream();
                    cmsData.getSignedContent().write(buf);
                    contentInCMS = new InMemoryDocument(buf.toByteArray());
                }
            } catch (CMSException ex) {
                // Do nothing
            } finally {
                //noinspection ConstantConditions
                if (original != null) {
                    original.close();
                }
            }
        }

        Document signedDocument;
        if (contentInCMS != null) {
            byte[] signatureValue = connection
                    .sign(service.toBeSigned(contentInCMS, parameters), parameters.getDigestAlgorithm(), model.getPrivateKey());
            CAdESService cadesService = (CAdESService) service;
            signedDocument = cadesService.addASignatureToDocument(document, parameters, signatureValue);
        } else {
            byte[] signatureValue = connection
                    .sign(service.toBeSigned(document, parameters), parameters.getDigestAlgorithm(), model.getPrivateKey());
            signedDocument = service.signDocument(document, parameters, signatureValue);
        }

        FileOutputStream output = new FileOutputStream(model.getSignedFile());
        IOUtils.copy(signedDocument.openStream(), output);
        output.close();
        System.out.println("SUCCESS.");
        System.out.println(String.format("Signed file: %s", model.getSignedFile().getAbsoluteFile()));
        return output;
    }

    private static String getSuggestedFileName(SignatureCLIModel model) {
        // The original filename
        String originalName = model.getOriginalFile().getName();

        // Delete the extension from the original name
        originalName = Util.getFileNameWithoutExtension(originalName);

        // Build the new suggested name
        String newName;
        if (model.getPackaging() == SignaturePackaging.ENVELOPING
                && model.getSignatureFormat().startsWith("XAdES")) {
            newName = originalName + "-signed" + ".xml";
        } else if (model.getPackaging() == SignaturePackaging.DETACHED
                && model.getSignatureFormat().startsWith("XAdES")) {
            newName = originalName + "-signed" + ".xml";
        } else if (model.getSignatureFormat().startsWith("CAdES-")
                && !model.getOriginalFile().getName().endsWith(".p7m")) {
            newName = model.getOriginalFile().getName() + ".p7m";
        } else if (model.getSignatureFormat().startsWith("ASiC-")) {
            newName = model.getOriginalFile().getName() + ".asics";
        } else {
            int i = model.getOriginalFile().getName().lastIndexOf(".");
            if (i > 0) {
                newName = model.getOriginalFile().getName().substring(0, i) + "-signed"
                        + model.getOriginalFile().getName().substring(i);
            } else {
                newName = model.getOriginalFile().getName() + "-signed";
            }
        }
        return newName;
    }
}
