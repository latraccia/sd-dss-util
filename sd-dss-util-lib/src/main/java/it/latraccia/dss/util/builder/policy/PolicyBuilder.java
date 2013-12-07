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

package it.latraccia.dss.util.builder.policy;

import it.latraccia.dss.util.builder.IBuilder;
import it.latraccia.dss.util.entity.PolicyAlgorithm;
import it.latraccia.dss.util.exception.SignaturePolicyAlgorithmMismatchException;
import it.latraccia.dss.util.util.AssertHelper;
import it.latraccia.dss.util.util.Util;

/**
 * Date: 07/12/13
 * Time: 9.25
 *
 * @author Francesco Pontillo
 */
public class PolicyBuilder implements IBuilder<PolicyBuilder.SignaturePolicy> {
    protected PolicyAlgorithm policyAlgorithm;
    protected String oid;
    protected String hash;

    public PolicyAlgorithm getPolicyAlgorithm() {
        return policyAlgorithm;
    }

    public PolicyBuilder setPolicyAlgorithm(PolicyAlgorithm policyAlgorithm) {
        this.policyAlgorithm = policyAlgorithm;
        return this;
    }

    public String getOid() {
        return oid;
    }

    public PolicyBuilder setOid(String oid) {
        this.oid = oid;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public PolicyBuilder setHash(String hash) {
        this.hash = hash;
        return this;
    }

    @Override
    public SignaturePolicy build() throws SignaturePolicyAlgorithmMismatchException {
        SignaturePolicy signaturePolicy = new SignaturePolicy();
        signaturePolicy.setPolicyAlgorithm(policyAlgorithm);
        signaturePolicy.setOid(oid);
        signaturePolicy.setHash(hash);

        validate();

        return signaturePolicy;
    }

    private void validate() throws SignaturePolicyAlgorithmMismatchException {
        if (!Util.isNullOrEmpty(getPolicyAlgorithm().getValue())) {
            if (!AssertHelper.stringMustBeInList(
                    "explicit policy algorithm",
                    getPolicyAlgorithm().getValue(),
                    new String[]{"SHA1"})) {
                throw new SignaturePolicyAlgorithmMismatchException();
            }
        }
    }

    public class SignaturePolicy {
        protected PolicyAlgorithm policyAlgorithm;
        protected String oid;
        protected String hash;

        public PolicyAlgorithm getPolicyAlgorithm() {
            return policyAlgorithm;
        }

        public void setPolicyAlgorithm(PolicyAlgorithm policyAlgorithm) {
            this.policyAlgorithm = policyAlgorithm;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }
}
