package it.latraccia.dss.cli.main.builder.policy;

import it.latraccia.dss.cli.main.builder.IBuilder;
import it.latraccia.dss.cli.main.entity.PolicyAlgorithm;
import it.latraccia.dss.cli.main.exception.SignaturePolicyAlgorithmMismatchException;
import it.latraccia.dss.cli.main.util.AssertHelper;
import it.latraccia.dss.cli.main.util.Util;

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
