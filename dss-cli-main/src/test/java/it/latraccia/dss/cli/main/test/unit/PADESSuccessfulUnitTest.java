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

package it.latraccia.dss.cli.main.test.unit;

import it.latraccia.dss.cli.main.test.CADESSuccessfulGenericTest;
import it.latraccia.dss.cli.main.test.PADESSuccessfulGenericTest;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(value = Parameterized.class)
public class PADESSuccessfulUnitTest extends PADESSuccessfulGenericTest {

    public PADESSuccessfulUnitTest(String description, Object[] args) {
        super(description, args);
    }

    @Override
    public boolean getSimulate() {
        return true;
    }
}
