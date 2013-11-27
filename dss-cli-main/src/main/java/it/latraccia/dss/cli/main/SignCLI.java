package it.latraccia.dss.cli.main;

import com.beust.jcommander.JCommander;
import eu.europa.ec.markt.dss.applet.model.SignatureWizardModel;
import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ec.markt.dss.signature.FileDocument;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import it.latraccia.dss.cli.main.argument.SignatureArgs;
import it.latraccia.dss.cli.main.model.PKCS12Model;
import it.latraccia.dss.cli.main.util.StringUtil;

import java.io.File;
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
        SignatureWizardModel model = new SignatureWizardModel();
        // Read and parse the arguments
        SignatureArgs signatureArgs = new SignatureArgs();
        new JCommander(signatureArgs, args);

        // Validate and set the parameters inside the SignatureWizardModel, step by step
        setSourceFile(signatureArgs, model);
        setSignatureFormatLevelPackaging(signatureArgs, model);

        setTokenType(signatureArgs, model);
        setTokenParameters(signatureArgs, model);

        setOutputFile(signatureArgs, model);

        return;
    }

    private static void setSourceFile(SignatureArgs signatureArgs, SignatureWizardModel model) {
        // Set the FileDocument from the user source path
        String sourceFile = signatureArgs.getSource().get(0);
        model.setOriginalFile(new FileDocument(sourceFile));
    }

    private static void setSignatureFormatLevelPackaging(SignatureArgs signatureArgs, SignatureWizardModel model) {
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

    private static void setTokenType(SignatureArgs signatureArgs, SignatureWizardModel model) {
        SignatureTokenType tokenType = null;
        // Get the parameters
        String pkcs11 = signatureArgs.getPkcs11();
        List<String> pkcs12 = signatureArgs.getPkcs12();
        boolean mscapi = signatureArgs.isMscapi();
        String mocca = signatureArgs.getMocca();

        // Set the token type
        if (!StringUtil.isNullOrEmpty(pkcs11)) {
            tokenType = SignatureTokenType.PKCS11;
        } else if (pkcs12 != null && pkcs12.size() == 2) {
            tokenType = SignatureTokenType.PKCS12;
        } else if (mscapi) {
            tokenType = SignatureTokenType.MSCAPI;
        } else if (!StringUtil.isNullOrEmpty(mocca)) {
            tokenType = SignatureTokenType.MOCCA;
        }
        // TODO: handle MOCCA token only if it is available (SignatureTokenAPIPanel.aboutToDisplayPanel)
        model.setTokenType(tokenType);
    }

    private static void setTokenParameters(SignatureArgs signatureArgs, SignatureWizardModel model) {
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

    private static void setOutputFile(SignatureArgs signatureArgs, SignatureWizardModel model) {
        String outFile = signatureArgs.getDestination();
        // TODO: Set the user-selected destination path or file
    }
}
