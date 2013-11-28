package it.latraccia.dss.cli.main;

import com.beust.jcommander.JCommander;
import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ec.markt.dss.signature.FileDocument;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.SignaturePolicy;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;
import it.latraccia.dss.cli.main.argument.SignatureArgs;
import it.latraccia.dss.cli.main.model.ExplicitSignaturePolicyModel;
import it.latraccia.dss.cli.main.model.PKCS12Model;
import it.latraccia.dss.cli.main.model.SignatureCLIModel;
import it.latraccia.dss.cli.main.util.Util;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.security.KeyStoreException;
import java.util.List;

/**
 * CLI for signing documents by using the DSS server and libraries.
 * Call this CLI with
 *      java SignCLI "the/file/to/be/signed" -d="the/signed/file/destination/"
 *
 * @author Francesco Pontillo
 * Date: 26/11/13
 * Time: 11.30
 */
public class SignCLI {

    public static void main(String[] args) {
        // Create the signature wizard model
        SignatureCLIModel model = new SignatureCLIModel();
        // Read and parse the arguments
        SignatureArgs signatureArgs = new SignatureArgs();
        new JCommander(signatureArgs, args);

        // Validate and set the parameters inside the SignatureModel, step by step
        setSourceFile(signatureArgs, model);
        setSignatureFormatLevelPackaging(signatureArgs, model);

        setTokenType(signatureArgs, model);
        setTokenParameters(signatureArgs, model);
        setPrivateKey(signatureArgs, model);

        setClaimedRole(signatureArgs, model);
        setPolicy(signatureArgs, model);

        setOutputFile(signatureArgs, model);
    }

    private static void setSourceFile(SignatureArgs signatureArgs, SignatureCLIModel model) {
        // Set the FileDocument from the user source path
        String sourceFile = signatureArgs.getSource().get(0);
        model.setOriginalFile(new FileDocument(sourceFile));
    }

    private static void setSignatureFormatLevelPackaging(SignatureArgs signatureArgs, SignatureCLIModel model) {
        // Set the signature format
        String format = signatureArgs.getFormat();
        String level = signatureArgs.getLevel();
        SignaturePackaging packaging = signatureArgs.getPackaging();
        // TODO: validate the signature level (ChooseSignaturePanel.LevelComboBoxModel.getElements)
        // TODO: validate the signature format for non-PDF files (ChooseSignaturePanel.aboutToDisplayPanel)
        // TODO: validate the packaging according to the signature format (ChooseSignaturePanel.*RadioActionPerformed)
        model.setSignatureFormat(format + "-" + level);
        model.setPackaging(packaging);
    }

    private static void setTokenType(SignatureArgs signatureArgs, SignatureCLIModel model) {
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
        }
        // TODO: handle MOCCA token only if it is available (SignatureTokenAPIPanel.aboutToDisplayPanel)
        model.setTokenType(tokenType);
    }

    private static void setTokenParameters(SignatureArgs signatureArgs, SignatureCLIModel model) {
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
                    // TODO: throw exception for non existing PKCS11 library
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
                    // TODO: throw exception for non existing PKCS11 library
                }
                break;
            case MOCCA:
                // TODO: validate the MOCCA algorithm (sha1 or sha256)
                model.setMoccaSignatureAlgorithm(mocca.toLowerCase());
                break;
        }
    }

    private static void setPrivateKey(SignatureArgs signatureArgs, SignatureCLIModel model) {
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
                int keyIndex = -1;
                System.out.println("Select the number of the certificate you wish to use:");

                // Read the integer until we get a valid number within the entries' bounds
                keyIndex = Util.readInt(-1, 1, entries.size());
                // Get the key and print a summary
                key = entries.get(keyIndex-1);
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

    private static void setClaimedRole(SignatureArgs signatureArgs, SignatureCLIModel model) {
        model.setClaimedRole(signatureArgs.getClaimedRole());
    }

    private static void setPolicy(SignatureArgs signatureArgs, SignatureCLIModel model) {
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
            } else {
                // TODO: validate explicit policy parameters
            }
        }
    }

    private static void setOutputFile(SignatureArgs signatureArgs, SignatureCLIModel model) {
        String outFile = signatureArgs.getDestination();
        // TODO: Set the user-selected destination path or file
    }
}
