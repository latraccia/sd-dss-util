package it.latraccia.dss.cli.main.builder.token;
import eu.europa.ec.markt.dss.applet.util.MOCCAAdapter;
import it.latraccia.dss.cli.main.entity.MoccaAlgorithm;
import it.latraccia.dss.cli.main.entity.token.SignatureTokenType;
import it.latraccia.dss.cli.main.exception.SignatureMoccaAlgorithmMismatchException;
import it.latraccia.dss.cli.main.exception.SignatureMoccaUnavailabilityException;
import it.latraccia.dss.cli.main.util.AssertHelper;
import it.latraccia.dss.cli.main.util.Util;

/**
 * Date: 07/12/13
 * Time: 9.11
 *
 * @author Francesco Pontillo
 */
public class MoccaTokenBuilder extends TokenBuilder {
    protected MoccaAlgorithm moccaAlgorithm;

    public MoccaTokenBuilder() {
        setTokenType(SignatureTokenType.MOCCA);
    }

    public MoccaTokenBuilder setMoccaAlgorithm(MoccaAlgorithm moccaAlgorithm) {
        this.moccaAlgorithm = moccaAlgorithm;
        return this;
    }

    @Override
    public SignatureToken build() throws SignatureMoccaAlgorithmMismatchException, SignatureMoccaUnavailabilityException {
        SignatureToken token = super.build();
        token.setMoccaAlgorithm(this.moccaAlgorithm);

        validateMoccaAlgorithm();
        validateMoccaAvailability();

        return token;
    }


    /**
     * Validate the MOCCA availability.
     * Some of the code has been taken from {@link eu.europa.ec.markt.dss.applet.view.signature.TokenView#doLayout()}.
     *
     * @throws it.latraccia.dss.cli.main.exception.SignatureMoccaUnavailabilityException Thrown if MOCCA is not available
     */
    protected void validateMoccaAvailability() throws SignatureMoccaUnavailabilityException {
        boolean isMoccaSet = !Util.isNullOrEmpty(moccaAlgorithm.getValue());
        boolean isMoccaAvailable = new MOCCAAdapter().isMOCCAAvailable();
        if (isMoccaSet && !isMoccaAvailable) {
            System.err.println("MOCCA is not available, please choose another token provider.");
            throw new SignatureMoccaUnavailabilityException();
        }
    }

    /**
     * Validate the MOCCA algorithm if it is SHA1.
     *
     * @throws it.latraccia.dss.cli.main.exception.SignatureMoccaAlgorithmMismatchException Thrown if the MOCCA algorithm is not SHA1
     */
    protected void validateMoccaAlgorithm() throws SignatureMoccaAlgorithmMismatchException {
        if (!AssertHelper.stringMustBeInList(
                "MOCCA algorithm",
                moccaAlgorithm.getValue(),
                new String[]{"SHA1", "SHA"})) {
            throw new SignatureMoccaAlgorithmMismatchException();
        }
    }

}
