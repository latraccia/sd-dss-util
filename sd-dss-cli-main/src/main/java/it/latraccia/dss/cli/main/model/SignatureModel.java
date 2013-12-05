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

package it.latraccia.dss.cli.main.model;

import eu.europa.ec.markt.dss.applet.main.FileType;
import eu.europa.ec.markt.dss.applet.util.FileTypeDetectorUtils;
import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.SignatureTokenConnection;

import java.io.File;
import java.util.List;

import static java.util.Collections.emptyList;

@SuppressWarnings("serial")
public class SignatureModel {

    private File selectedFile;
    private File targetFile;
    private File pkcs11File;
    private File pkcs12File;
    private String pkcs11Password;
    private String pkcs12Password;
    private SignatureTokenType tokenType;
    private String format;
    private SignaturePackaging packaging;
    private String level;
    private SignatureTokenConnection tokenConnection;
    private List<DSSPrivateKeyEntry> privateKeys;
    private DSSPrivateKeyEntry selectedPrivateKey;
    private String claimedRole;
    private boolean claimedCheck;
    private boolean signaturePolicyCheck;
    public boolean signaturePolicyVisible;
    private String signaturePolicyId;
    private String signaturePolicyValue;
    private String signaturePolicyAlgo;
    private String moccaSignatureAlgorithm;

    /**
     * @return the claimedRole
     */
    public String getClaimedRole() {
        return claimedRole;
    }

    public String getMoccaSignatureAlgorithm() {
        return moccaSignatureAlgorithm;
    }

    public void setMoccaSignatureAlgorithm(String moccaSignatureAlgorithm) {
        this.moccaSignatureAlgorithm = moccaSignatureAlgorithm;
    }

    /**
     * @return the fileType
     */
    public FileType getFileType() {
        return FileTypeDetectorUtils.resolveFiletype(getSelectedFile());
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @return the packaging
     */
    public SignaturePackaging getPackaging() {
        return packaging;
    }

    /**
     * @return the pkcs11File
     */
    public File getPkcs11File() {
        return pkcs11File;
    }

    /**
     * @return the pkcs11password
     */
    public String getPkcs11Password() {
        return pkcs11Password;
    }

    /**
     * @return the pkcs12File
     */
    public File getPkcs12File() {
        return pkcs12File;
    }

    /**
     * @return the pkcs12Password
     */
    public String getPkcs12Password() {
        return pkcs12Password;
    }

    /**
     * @return the privateKeys
     */
    public List<DSSPrivateKeyEntry> getPrivateKeys() {
        if (tokenConnection == null || privateKeys == null) {
            return emptyList();
        }
        return privateKeys;
    }

    /**
     * @return the selectedFile
     */
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * @return the selectedPrivateKey
     */
    public DSSPrivateKeyEntry getSelectedPrivateKey() {
        return selectedPrivateKey;
    }

    /**
     * @return the signaturePolicyAlgo
     */
    public String getSignaturePolicyAlgo() {
        return signaturePolicyAlgo;
    }

    /**
     * @return the signaturePolicyId
     */
    public String getSignaturePolicyId() {
        return signaturePolicyId;
    }

    /**
     * @return the signaturePolicyValue
     */
    public String getSignaturePolicyValue() {
        return signaturePolicyValue;
    }

    /**
     * @return the targetFile
     */
    public File getTargetFile() {
        return targetFile;
    }

    /**
     * @return the tokenConnection
     */
    public SignatureTokenConnection getTokenConnection() {
        return tokenConnection;
    }

    /**
     * @return the tokenType
     */
    public SignatureTokenType getTokenType() {
        return tokenType;
    }

    /**
     * @return the claimedCheck
     */
    public boolean isClaimedCheck() {
        return claimedCheck;
    }

    /**
     * @return the signaturePolicyCheck
     */
    public boolean isSignaturePolicyCheck() {
        return signaturePolicyCheck;
    }

    public boolean isSignaturePolicyVisible() {
        return signaturePolicyVisible;
    }

    /**
     * @param claimedCheck the claimedCheck to set
     */
    public void setClaimedCheck(final boolean claimedCheck) {
        this.claimedCheck = claimedCheck;
    }

    /**
     * @param claimedRole the claimedRole to set
     */
    public void setClaimedRole(final String claimedRole) {
        this.claimedRole = claimedRole;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(final String format) {
        this.format = format;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(final String level) {
        this.level = level;
    }

    /**
     * @param packaging the packaging to set
     */
    public void setPackaging(final SignaturePackaging packaging) {
        this.packaging = packaging;
    }

    /**
     * @param pkcs11File the pkcs11File to set
     */
    public void setPkcs11File(final File pkcs11File) {
        this.pkcs11File = pkcs11File;
    }

    /**
     * 
     * @param pkcs11Password the pkcs11password to set
     */
    public void setPkcs11Password(final String pkcs11Password) {
        this.pkcs11Password = pkcs11Password;
    }

    /**
     * @param pkcs12File the pkcs12File to set
     */
    public void setPkcs12File(final File pkcs12File) {
        this.pkcs12File = pkcs12File;
    }

    /**
     * @param pkcs12Password the pkcs12Password to set
     */
    public void setPkcs12Password(final String pkcs12Password) {
        this.pkcs12Password = pkcs12Password;
    }

    /**
     * @param privateKeys the privateKeys to set
     */
    public void setPrivateKeys(final List<DSSPrivateKeyEntry> privateKeys) {
        this.privateKeys = privateKeys;
    }

    /**
     * @param selectedFile the selectedFile to set
     */
    public void setSelectedFile(final File selectedFile) {
        this.selectedFile = selectedFile;
    }

    /**
     * @param selectedPrivateKey the selectedPrivateKey to set
     */
    public void setSelectedPrivateKey(final DSSPrivateKeyEntry selectedPrivateKey) {
        this.selectedPrivateKey = selectedPrivateKey;
    }

    /**
     * @param signaturePolicyAlgo the signaturePolicyAlgo to set
     */
    public void setSignaturePolicyAlgo(final String signaturePolicyAlgo) {
        this.signaturePolicyAlgo = signaturePolicyAlgo;
    }

    /**
     * @param signaturePolicyCheck the signaturePolicyCheck to set
     */
    public void setSignaturePolicyCheck(final boolean signaturePolicyCheck) {
        this.signaturePolicyCheck = signaturePolicyCheck;
    }

    /**
     * @param signaturePolicyId the signaturePolicyId to set
     */
    public void setSignaturePolicyId(final String signaturePolicyId) {
        this.signaturePolicyId = signaturePolicyId;
    }

    /**
     * @param signaturePolicyValue the signaturePolicyValue to set
     */
    public void setSignaturePolicyValue(final String signaturePolicyValue) {
        this.signaturePolicyValue = signaturePolicyValue;
    }

    public void setSignaturePolicyVisible(boolean signaturePolicyVisible) {
        this.signaturePolicyVisible = signaturePolicyVisible;
    }

    /**
     * @param targetFile the targetFile to set
     */
    public void setTargetFile(final File targetFile) {
        this.targetFile = targetFile;
    }

    /**
     * @param tokenConnection the tokenConnection to set
     */
    public void setTokenConnection(final SignatureTokenConnection tokenConnection) {
        this.tokenConnection = tokenConnection;
    }

    /**
     * @param tokenType the tokenType to set
     */
    public void setTokenType(final SignatureTokenType tokenType) {
        this.tokenType = tokenType;
    }

}
