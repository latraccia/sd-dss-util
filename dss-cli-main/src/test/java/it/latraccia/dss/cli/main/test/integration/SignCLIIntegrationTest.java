package it.latraccia.dss.cli.main.test.integration;/*
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

import it.latraccia.dss.cli.main.test.SignCLIGenericTest;

/**
 * Date: 30/11/13
 * Time: 11.40
 *
 * @author Francesco Pontillo
 */
public class SignCLIIntegrationTest extends SignCLIGenericTest {
    @Override
    public boolean getSimulate() {
        return false;
    }
}
