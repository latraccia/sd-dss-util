/*
 * DSS-CLI, a Command Line Interface for SD-DSS.
 * Copyright (C) 2013 La Traccia
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

import java.util.List;

/**
 * @author Francesco Pontillo
 *
 * Date: 28/11/13
 * Time: 13.21
 */
public class ExplicitSignaturePolicyModel {
    private String OID;
    private String hash;
    private String hashAlgo;

    public ExplicitSignaturePolicyModel(List<String> list) {
        if (list != null) {
            setOID(list.get(0));
            setHash(list.get(1));
            setHashAlgo(list.get(2));
        }
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashAlgo() {
        return hashAlgo;
    }

    public void setHashAlgo(String hashAlgo) {
        this.hashAlgo = hashAlgo;
    }
}
