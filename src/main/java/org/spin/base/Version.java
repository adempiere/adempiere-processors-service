/************************************************************************************
 * Copyright (C) 2018-present E.R.P. Consultores y Asociados, C.A.                  *
 * Contributor(s): Edwin Betancourt, EdwinBetanc0urt@outlook.com                    *
 * This program is free software: you can redistribute it and/or modify             *
 * it under the terms of the GNU General Public License as published by             *
 * the Free Software Foundation, either version 2 of the License, or                *
 * (at your option) any later version.                                              *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                     *
 * GNU General Public License for more details.                                     *
 * You should have received a copy of the GNU General Public License                *
 * along with this program. If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/

package org.spin.base;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.compiere.util.CLogger;

/**
 * @author Edwin Betancourt, EdwinBetanc0urt@outlook.com, https://github.com/EdwinBetanc0urt
 * Service for backend of Version
 */
public final class Version
{

	private static CLogger log = CLogger.getCLogger(Version.class);

	/** Main Version String         */
	// Conventions for naming second number is even for stable, and odd for unstable
	// the releases will have a suffix (a) for alpha - (b) for beta - (t) for trunk - (s) for stable - and (LTS) for long term support
	public static String MAIN_VERSION = "1.0.0-dev";

	/** Detail Version as date      Used for Client/Server		*/
	public static String DATE_VERSION = (new SimpleDateFormat("yyyy-MM-dd")).format(
		new Date(
			System.currentTimeMillis()
		)
	);

	public static String IMPLEMENTATION_VERSION = "1.0.0-dev";

	static {
		ClassLoader loader = Version.class.getClassLoader();
		InputStream inputStream = loader.getResourceAsStream("org/spin/base/version.properties");
		if (inputStream != null) {
			Properties properties = new Properties();
			try {
				properties.load(inputStream);

				if (properties.containsKey("MAIN_VERSION")) {
					MAIN_VERSION = properties.getProperty("MAIN_VERSION");
				}
				if (properties.containsKey("DATE_VERSION")) {
					DATE_VERSION = properties.getProperty("DATE_VERSION");
				}
				if (properties.containsKey("IMPLEMENTATION_VERSION")) {
					IMPLEMENTATION_VERSION = properties.getProperty("IMPLEMENTATION_VERSION");
				}
			} catch (IOException e) {
				// file does not exists
				log.warning("File version.properties does no exists");
			}
		}
	}

	/**
	 *  Get Product Version
	 *  @return Application Version
	 */
	public static String getVersion()
	{
		return MAIN_VERSION + " @ " + DATE_VERSION;
	} // getVersion

	public static String getMainVersion() {
		return MAIN_VERSION;
	}

	public static String getDateVersion() {
		return DATE_VERSION;
	}

	public static String getImplementationVersion() {
		return IMPLEMENTATION_VERSION;
	}

}	//	Adempiere
