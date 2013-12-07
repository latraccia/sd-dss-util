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

import it.latraccia.dss.util.entity.GeneralEnum;

/**
 * Date: 06/12/13
 * Time: 9.32
 *
 * @author Francesco Pontillo
 */
public class SignatureLevel extends GeneralEnum {
    protected static String BES = "BES";
    protected static String EPES = "EPES";
    protected static String T = "T";
    protected static String C = "C";
    protected static String X = "X";
    protected static String XL = "XL";
    protected static String A = "A";
    protected static String LTV = "LTV";

    public SignatureLevel(String value) {
        super(value);
    }
}
