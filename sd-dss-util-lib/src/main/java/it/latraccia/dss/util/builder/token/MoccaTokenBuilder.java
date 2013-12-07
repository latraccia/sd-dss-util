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

package it.latraccia.dss.util.builder.token;
import eu.europa.ec.markt.dss.applet.util.MOCCAAdapter;
import it.latraccia.dss.util.entity.MoccaAlgorithm;
import it.latraccia.dss.util.entity.token.SignatureTokenType;
import it.latraccia.dss.util.exception.SignatureMoccaAlgorithmMismatchException;
import it.latraccia.dss.util.exception.SignatureMoccaUnavailabilityException;
import it.latraccia.dss.util.util.AssertHelper;
import it.latraccia.dss.util.util.Util;

import java.io.FileNotFoundException;

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
    public SignatureToken build() throws SignatureMoccaAlgorithmMismatchException, SignatureMoccaUnavailabilityException, FileNotFoundException {
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
     * @throws it.latraccia.dss.util.exception.SignatureMoccaUnavailabilityException Thrown if MOCCA is not available
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
     * @throws it.latraccia.dss.util.exception.SignatureMoccaAlgorithmMismatchException Thrown if the MOCCA algorithm is not SHA1
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
