package it.latraccia.dss.cli.main.builder;

import it.latraccia.dss.cli.main.exception.SignatureException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Date: 07/12/13
 * Time: 8.34
 *
 * @author Francesco Pontillo
 */
public interface IBuilder<T> {
    public T build() throws IOException, SignatureException, KeyStoreException, NoSuchAlgorithmException;
}
