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

package it.latraccia.dss.util.cli;

import com.beust.jcommander.JCommander;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import it.latraccia.dss.util.cli.argument.SignatureArgs;
import it.latraccia.dss.util.builder.SignatureBuilder;
import it.latraccia.dss.util.builder.format.CAdESFormatBuilder;
import it.latraccia.dss.util.builder.format.FormatBuilder;
import it.latraccia.dss.util.builder.format.PAdESFormatBuilder;
import it.latraccia.dss.util.builder.format.XAdESFormatBuilder;
import it.latraccia.dss.util.builder.policy.PolicyBuilder;
import it.latraccia.dss.util.builder.token.*;
import it.latraccia.dss.util.entity.MoccaAlgorithm;
import it.latraccia.dss.util.entity.PolicyAlgorithm;
import it.latraccia.dss.util.entity.level.SignatureCAdESLevel;
import it.latraccia.dss.util.entity.level.SignaturePAdESLevel;
import it.latraccia.dss.util.entity.level.SignatureXAdESLevel;
import it.latraccia.dss.util.entity.packaging.SignatureCAdESPackaging;
import it.latraccia.dss.util.entity.packaging.SignaturePAdESPackaging;
import it.latraccia.dss.util.entity.packaging.SignatureXAdESPackaging;
import it.latraccia.dss.util.exception.SignatureException;
import it.latraccia.dss.util.model.ExplicitSignaturePolicyModel;
import it.latraccia.dss.util.model.PKCSModel;
import it.latraccia.dss.util.util.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class SignCLI {

    public static void main(String[] args)
            throws IOException, SignatureException, KeyStoreException, NoSuchAlgorithmException {
        // Read and parse the arguments
        SignatureArgs signatureArgs = new SignatureArgs();
        new JCommander(signatureArgs, args);

        execute(signatureArgs);
    }

    private static FileOutputStream execute(SignatureArgs signatureArgs)
            throws IOException, SignatureException, KeyStoreException, NoSuchAlgorithmException {
        // Create the signature wizard builder
        SignatureBuilder signatureBuilder = new SignatureBuilder();

        // Set the parameters inside the SignatureBuilder, step by step
        setSourceFile(signatureArgs, signatureBuilder);
        setSignatureFormatLevelPackagingServiceUrl(signatureArgs, signatureBuilder);
        setDigestAlgorithm(signatureArgs, signatureBuilder);
        setToken(signatureArgs, signatureBuilder);
        setPrivateKeyChooser(signatureBuilder);
        setClaimedRole(signatureArgs, signatureBuilder);
        setPolicy(signatureArgs, signatureBuilder);
        setOutputFile(signatureArgs, signatureBuilder);

        signatureBuilder.build(signatureArgs.isSimulate());

        return null;
    }

    /**
     * Set the source file to be signed.
     *
     * @param signatureArgs     The input arguments
     * @param builder           The signature builder
     */
    protected static void setSourceFile(SignatureArgs signatureArgs, SignatureBuilder builder) {
        // Set the FileDocument from the user source path
        String sourceFile = signatureArgs.getSource().get(0);
        builder.setSource(sourceFile);
    }

    /**
     * Set the signature format, level and packaging.
     *
     * @param signatureArgs     The input arguments
     * @param builder           The signature builder
     */
    protected static void setSignatureFormatLevelPackagingServiceUrl(SignatureArgs signatureArgs, SignatureBuilder builder) {
        // Set the signature format
        String format = signatureArgs.getFormat();
        String level = signatureArgs.getLevel();
        SignaturePackaging packaging = signatureArgs.getPackaging();

        if (format != null) {
            FormatBuilder formatBuilder = null;
            if (format.equalsIgnoreCase(it.latraccia.dss.util.entity.format.SignatureFormat.CAdES_NAME)) {
                formatBuilder = new CAdESFormatBuilder();
                formatBuilder.setLevel(new SignatureCAdESLevel(level.toUpperCase()));
                formatBuilder.setPackaging(new SignatureCAdESPackaging(packaging.name()));
            } else if (format.equalsIgnoreCase(it.latraccia.dss.util.entity.format.SignatureFormat.PAdES_NAME)) {
                formatBuilder = new PAdESFormatBuilder();
                formatBuilder.setLevel(new SignaturePAdESLevel(level.toUpperCase()));
                formatBuilder.setPackaging(new SignaturePAdESPackaging(packaging.name()));
            } else if (format.equalsIgnoreCase(it.latraccia.dss.util.entity.format.SignatureFormat.XAdES_NAME)) {
                formatBuilder = new XAdESFormatBuilder();
                formatBuilder.setLevel(new SignatureXAdESLevel(level.toUpperCase()));
                formatBuilder.setPackaging(new SignatureXAdESPackaging(packaging.name()));
            }
            if (formatBuilder != null) {
                formatBuilder.setServiceUrl(signatureArgs.getUrl());
            }
            builder.setFormatBuilder(formatBuilder);
        }
    }

    /**
     * Set the custom digest algorithm.
     *
     * @param signatureArgs     The input arguments
     * @param builder           The signature builder
     */
    protected static void setDigestAlgorithm(SignatureArgs signatureArgs, SignatureBuilder builder) {
        if (signatureArgs.getDigestAlgorithm() != null) {
            builder.setDigestAlgorithm(
                    new it.latraccia.dss.util.entity.DigestAlgorithm(signatureArgs.getDigestAlgorithm().name()));
        }
    }

    /**
     * Set the key token.
     *
     * @param signatureArgs     The input arguments
     * @param builder           The signature builder
     */
    protected static void setToken(SignatureArgs signatureArgs, SignatureBuilder builder) {
        // Get the parameters
        List<String> pkcs11 = signatureArgs.getPkcs11();
        List<String> pkcs12 = signatureArgs.getPkcs12();
        boolean mscapi = signatureArgs.isMscapi();
        String mocca = signatureArgs.getMocca();

        TokenBuilder tokenBuilder = null;

        // Set the token type
        if (pkcs11 != null && pkcs11.size() == 2) {
            PKCSModel pkcs11Model = new PKCSModel(pkcs11);
            tokenBuilder = new PKCS11TokenBuilder()
                    .setFile(pkcs11Model.getFile())
                    .setPassword(pkcs11Model.getPassword());
        } else if (pkcs12 != null && pkcs12.size() == 2) {
            PKCSModel pkcs12Model = new PKCSModel(pkcs12);
            tokenBuilder = new PKCS12TokenBuilder()
                    .setFile(pkcs12Model.getFile())
                    .setPassword(pkcs12Model.getPassword());
        } else if (mscapi) {
            tokenBuilder = new MSCAPITokenBuilder();
        } else if (!Util.isNullOrEmpty(mocca)) {
            tokenBuilder = new MoccaTokenBuilder()
                    .setMoccaAlgorithm(new MoccaAlgorithm(signatureArgs.getMocca()));
        }

        builder.setTokenBuilder(tokenBuilder);
    }

    /**
     * Set the default chooser object for the private key.
     *
     * @param builder The signature builder
     */
    protected static void setPrivateKeyChooser(SignatureBuilder builder) {
        builder.setDSSPrivateKeyChooser(new InputDSSPrivateKeyChooser());
    }

    /**
     * Set the claimed role of the user.
     *
     * @param signatureArgs     The input arguments
     * @param builder           The signature builder
     */
    protected static void setClaimedRole(SignatureArgs signatureArgs, SignatureBuilder builder) {
        builder.setClaimedRole(signatureArgs.getClaimedRole());
    }

    /**
     * Set the user personal data.
     *
     * @param signatureArgs     The input arguments
     * @param builder           The signature builder
     */
    protected static void setPolicy(SignatureArgs signatureArgs, SignatureBuilder builder) {
        // Convert explicit policy data
        ExplicitSignaturePolicyModel explicitPolicy =
                new ExplicitSignaturePolicyModel(signatureArgs.getExplicitPolicy());

        builder.setPolicyBuilder(new PolicyBuilder()
                .setOid(explicitPolicy.getOID())
                .setPolicyAlgorithm(new PolicyAlgorithm(explicitPolicy.getHashAlgo()))
                .setHash(explicitPolicy.getHash()));
    }

    /**
     * Sets the output file path, starting from the original file name and path, the signature format, level and
     * packaging.
     *
     * @param signatureArgs     The input arguments
     * @param builder           The signature builder
     */
    protected static void setOutputFile(SignatureArgs signatureArgs, SignatureBuilder builder) {
        // Set the user-selected destination path or file
        builder.setTarget(signatureArgs.getOutput());
    }

    /**
     * Simple key chooser handler, asks the user for a selection.
     */
    public static class InputDSSPrivateKeyChooser implements SignatureBuilder.IDSSPrivateKeyChooser {
        @Override
        public DSSPrivateKeyEntry getDSSPrivateKey(List<DSSPrivateKeyEntry> keys) {
            DSSPrivateKeyEntry key = null;
            if (keys != null) {
                if (keys.size() > 1) {
                    // Print the choices
                    System.out.println("The following keys have been found:");
                    int i = 1;
                    for (DSSPrivateKeyEntry k : keys) {
                        String subject = Util.getSubjectDN(k.getCertificate());
                        System.out.println(
                                String.format("[%d] %s", i++, subject));
                    }

                    // Ask for a choice
                    int keyIndex;
                    System.out.println("Select the number of the certificate you wish to use:");

                    // Read the integer until we get a valid number within the entries' bounds
                    keyIndex = Util.readInt(-1, 1, keys.size());
                    // Get the key and print a summary
                    key = keys.get(keyIndex - 1);
                    System.out.println(
                            String.format("Certificate selected: %s",
                                          Util.getSubjectDN(key.getCertificate())));
                } else {
                    // Use the first one
                    key = keys.get(0);
                }
            }
            return key;
        }
    }
}
