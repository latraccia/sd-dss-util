package it.latraccia.dss.cli.main.argument.converter;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;

/**
 * Converter class from {@link String} to {@link SignaturePackaging}.
 * Accepts any case form of "ENVELOPED", "ENVELOPING", "DETACHED".
 *
 * @author Francesco Pontillo
 *
 * Date: 27/11/13
 * Time: 11.26
 */
public class PackagingConverter implements IStringConverter<SignaturePackaging> {
    @Override
    public SignaturePackaging convert(String s) {
        String upperedPackage = s.toUpperCase();
        if ("ENVELOPED".equals(upperedPackage)) {
            return SignaturePackaging.ENVELOPED;
        } else if ("ENVELOPING".equals(upperedPackage)) {
            return SignaturePackaging.ENVELOPING;
        } else if ("DETACHED".equals(upperedPackage)) {
            return SignaturePackaging.DETACHED;
        } else throw new ParameterException(
                String.format("Could not recognize %s as a valid signature packaging.", s));
    }
}
