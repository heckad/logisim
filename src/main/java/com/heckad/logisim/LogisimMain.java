/*
 * Copyright (c) 2012, Carl Burch.
 *
 * This file is part of the Logisim source code. The latest
 * version is available at http://www.cburch.com/logisim/.
 *
 * Logisim is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Logisim is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Logisim.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.heckad.logisim;

import com.heckad.logisim.gui.start.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogisimMain entry point into Logisim.
 *
 * @author Carl Burch, Kazantcev Andrey (heckad)
 * @version 2.7.2
 */
public class LogisimMain {
    public static final LogisimVersion VERSION        = LogisimVersion.get(2, 7, 2);
    public static final String         VERSION_NAME   = VERSION.toString();
    public static final int            COPYRIGHT_YEAR = 2019;

    /**
     * Logger
     */
    public static final Logger logger = LoggerFactory.getLogger(LogisimMain.class);


    /**
     * Entry point like method. Needed for tests
     *
     * @param args command line arguments
     * @return Exit code
     */
    public void run_logisim(String[] args) {
        logger.info("Starting Logisim");
        Startup.parseArgs(args).run();
    }

    /**
     * Entry point
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new LogisimMain().run_logisim(args);
    }
}
