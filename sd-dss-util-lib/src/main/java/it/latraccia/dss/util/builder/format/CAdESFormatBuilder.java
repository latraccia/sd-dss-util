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

package it.latraccia.dss.util.builder.format;

import it.latraccia.dss.util.entity.format.SignatureCAdESFormat;
import it.latraccia.dss.util.entity.format.SignatureFormat;
import it.latraccia.dss.util.entity.level.SignatureCAdESLevel;
import it.latraccia.dss.util.entity.packaging.SignatureCAdESPackaging;
import it.latraccia.dss.util.exception.SignatureServiceUrlException;

/**
 * Date: 06/12/13
 * Time: 11.10
 *
 * @author Francesco Pontillo
 */
public class CAdESFormatBuilder extends FormatBuilder<SignatureCAdESFormat> {

    // FORMAT

    public CAdESFormatBuilder() {
        setFormat(SignatureFormat.CAdES);
    }

    // LEVEL

    public CAdESFormatBuilder BES() {
        setLevel(SignatureCAdESLevel.BES);
        return this;
    }

    public CAdESFormatBuilder EPES() {
        setLevel(SignatureCAdESLevel.EPES);
        return this;
    }

    public CAdESFormatBuilder T(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.T);
        setServiceUrl(serviceUrl);
        return this;
    }

    public CAdESFormatBuilder C(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.C);
        setServiceUrl(serviceUrl);
        return this;
    }

    public CAdESFormatBuilder X(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.BES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public CAdESFormatBuilder XL(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.EPES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public CAdESFormatBuilder A(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureCAdESLevel.A);
        setServiceUrl(serviceUrl);
        return this;
    }

    // PACKAGING

    public CAdESFormatBuilder enveloping() {
        setPackaging(SignatureCAdESPackaging.ENVELOPING);
        return this;
    }

    public CAdESFormatBuilder detached() {
        setPackaging(SignatureCAdESPackaging.DETACHED);
        return this;
    }
}
