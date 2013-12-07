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

import it.latraccia.dss.util.entity.format.SignatureFormat;
import it.latraccia.dss.util.entity.format.SignatureXAdESFormat;
import it.latraccia.dss.util.entity.level.SignatureXAdESLevel;
import it.latraccia.dss.util.entity.packaging.SignatureXAdESPackaging;
import it.latraccia.dss.util.exception.SignatureServiceUrlException;

/**
 * Date: 06/12/13
 * Time: 11.10
 *
 * @author Francesco Pontillo
 */
public class XAdESFormatBuilder extends FormatBuilder<SignatureXAdESFormat> {

    // FORMAT

    public XAdESFormatBuilder() {
        setFormat(SignatureFormat.XAdES);
    }

    // LEVEL

    public XAdESFormatBuilder BES() {
        setLevel(SignatureXAdESLevel.BES);
        return this;
    }

    public XAdESFormatBuilder EPES() {
        setLevel(SignatureXAdESLevel.EPES);
        return this;
    }

    public XAdESFormatBuilder T(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.T);
        setServiceUrl(serviceUrl);
        return this;
    }

    public XAdESFormatBuilder C(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.C);
        setServiceUrl(serviceUrl);
        return this;
    }

    public XAdESFormatBuilder X(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.BES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public XAdESFormatBuilder XL(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.EPES);
        setServiceUrl(serviceUrl);
        return this;
    }

    public XAdESFormatBuilder A(String serviceUrl) throws SignatureServiceUrlException {
        setLevel(SignatureXAdESLevel.A);
        setServiceUrl(serviceUrl);
        return this;
    }

    // PACKAGING

    public XAdESFormatBuilder enveloping() {
        setPackaging(SignatureXAdESPackaging.ENVELOPING);
        return this;
    }

    public XAdESFormatBuilder enveloped() {
        setPackaging(SignatureXAdESPackaging.ENVELOPED);
        return this;
    }

    public XAdESFormatBuilder detached() {
        setPackaging(SignatureXAdESPackaging.DETACHED);
        return this;
    }

}
