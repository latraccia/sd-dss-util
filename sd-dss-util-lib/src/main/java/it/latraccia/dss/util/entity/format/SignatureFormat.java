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

package it.latraccia.dss.util.entity.format;

import it.latraccia.dss.util.entity.GeneralEnum;

/**
 * Date: 06/12/13
 * Time: 9.32
 *
 * @author Francesco Pontillo
 */
public class SignatureFormat extends GeneralEnum {
    public static final String XAdES_NAME = "XAdES";
    public static final String CAdES_NAME = "CAdES";
    public static final String PAdES_NAME = "PAdES";

    public static final SignatureFormat XAdES = new SignatureXAdESFormat(XAdES_NAME);
    public static final SignatureFormat CAdES = new SignatureCAdESFormat(CAdES_NAME);
    public static final SignatureFormat PAdES = new SignaturePAdESFormat(PAdES_NAME);

    public SignatureFormat(String value) {
        super(value);
    }
}
