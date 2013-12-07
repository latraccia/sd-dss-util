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

package it.latraccia.dss.util.entity.token;

import it.latraccia.dss.util.entity.GeneralEnum;

/**
 * Date: 07/12/13
 * Time: 8.46
 *
 * @author Francesco Pontillo
 */
public class SignatureTokenType extends GeneralEnum {
    public static SignatureTokenType PKCS11 = new SignatureTokenType("PKCS11");
    public static SignatureTokenType PKCS12 = new SignatureTokenType("PKCS12");
    public static SignatureTokenType MOCCA = new SignatureTokenType("MOCCA");
    public static SignatureTokenType MSCAPI = new SignatureTokenType("MSCAPI");

    public SignatureTokenType(String value) {
        super(value);
    }
}
