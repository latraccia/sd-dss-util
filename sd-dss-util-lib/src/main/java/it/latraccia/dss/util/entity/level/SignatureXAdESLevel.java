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

package it.latraccia.dss.util.entity.level;

/**
 * Date: 06/12/13
 * Time: 10.23
 *
 * @author Francesco Pontillo
 */
public class SignatureXAdESLevel extends SignatureLevel {
    public static SignatureXAdESLevel BES = new SignatureXAdESLevel(SignatureLevel.BES);
    public static SignatureXAdESLevel EPES = new SignatureXAdESLevel(SignatureLevel.EPES);
    public static SignatureXAdESLevel T = new SignatureXAdESLevel(SignatureLevel.T);
    public static SignatureXAdESLevel C = new SignatureXAdESLevel(SignatureLevel.C);
    public static SignatureXAdESLevel X = new SignatureXAdESLevel(SignatureLevel.X);
    public static SignatureXAdESLevel XL = new SignatureXAdESLevel(SignatureLevel.XL);
    public static SignatureXAdESLevel A = new SignatureXAdESLevel(SignatureLevel.A);

    public SignatureXAdESLevel(String value) {
        super(value);
    }
}
