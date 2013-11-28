package it.latraccia.dss.cli.main.argument;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import it.latraccia.dss.cli.main.argument.converter.PackagingConverter;

import java.util.List;

/**
 * @author Francesco Pontillo
 *         <p/>
 *         Date: 27/11/13
 *         Time: 10.25
 */
@Parameters(separators = "=")
public class SignatureArgs {
    /* MAIN PARAMETERS */
    @Parameter(required = true,
            description = "Source filenames of the files to be signed")
    private List<String> source;

    // TODO: destination as a filename or a folder?
    @Parameter(names = {"-d", "--destination"},
            description = "Destination filename of the signed file")
    private String destination;
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

    @Parameter(names = {"-w", "--wrap"}, hidden = true,
            description = "Wrap in ASiC-S, if existing, create a specially signed zip file for packaging the unaltered " +
                    "document and  its CAdES signature file in one container. " +
                    "Available only with \"-f=CAdES -p=DETACHED\"")
    private boolean wrapAsics;
    /* END OF SIGNATURE FORMAT PARAMETERS */

    /* SIGNATURE TOKEN PARAMETERS */
    @Parameter(names = {"-p11", "--pkcs11"},
            description = "Specified if signature token is PKCS#11, evaluated with a previously installed driver " +
                    "path for the tokens and/or the reader hardware")
    private String pkcs11;

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
    @Parameter(names = {"-spi", "--signature-policy-implicit"},
            description = "If there is a signature policy to be implicitly inferred from the certificate/token")
    private boolean implicitPolicy;

    @Parameter(names = {"-spe", "--signature-policy-explicit"}, arity = 3,
            description = "Specify the policy with, in order, an OID, the base64 hash value of OID and the algorithm " +
                    "used to calculate the hash (only \"SHA1\" is allowed for now)")
    private List<String> explicitPolicy;
    /* END OF POLICY */

    /* GETTERS / SETTERS */
    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public boolean isWrapAsics() {
        return wrapAsics;
    }

    public void setWrapAsics(boolean wrapAsics) {
        this.wrapAsics = wrapAsics;
    }

    public String getPkcs11() {
        return pkcs11;
    }

    public void setPkcs11(String pkcs11) {
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

    public boolean isImplicitPolicy() {
        return implicitPolicy;
    }

    public void setImplicitPolicy(boolean implicitPolicy) {
        this.implicitPolicy = implicitPolicy;
    }

    public List<String> getExplicitPolicy() {
        return explicitPolicy;
    }

    public void setExplicitPolicy(List<String> explicitPolicy) {
        this.explicitPolicy = explicitPolicy;
    }

    /* END OF GETTERS / SETTERS */
}
