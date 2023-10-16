/*************************************************************************************
 * Product: ADempiere Bot                                                            *
 * Copyright (C) 2012-2019 E.R.P. Consultores y Asociados, C.A.                      *
 * Contributor(s): Yamel Senih ysenih@erpya.com                                      *
 * This program is free software: you can redistribute it and/or modify              *
 * it under the terms of the GNU General Public License as published by              *
 * the Free Software Foundation, either version 3 of the License, or                 *
 * (at your option) any later version.                                               *
 * This program is distributed in the hope that it will be useful,                   *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                      *
 * GNU General Public License for more details.                                      *
 * You should have received a copy of the GNU General Public License                 *
 * along with this program. If not, see <https://www.gnu.org/licenses/>.             *
 ************************************************************************************/
package org.spin.template.setup;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.compiere.db.CConnection;
import org.compiere.util.CLogMgt;
import org.compiere.util.DB;
import org.compiere.util.Ini;
import org.spin.template.setup.SetupLoader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * @author Setup loader class
 */
public class SetupLoader {
	/**	Default instance	*/
	private static SetupLoader instance;
	/**	Setup	*/
	private SetupWrapper setup;

	/** Supported Databases */
	private static List<String> SUPPORTED_DATABSASES = Arrays.asList(
		org.compiere.db.Database.DB_POSTGRESQL,
		org.compiere.db.Database.DB_ORACLE
	);


	/**
	 * Private constructor
	 * @param filePath
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	private SetupLoader(String filePath) throws JsonParseException, JsonMappingException, IOException {
		File setupFile = new File(filePath);
		ObjectMapper fileMapper = new ObjectMapper(new YAMLFactory());
		setup = fileMapper.readValue(setupFile, SetupWrapper.class);
	}
	
	/**
	 * Verify if is loaded else throw a exception
	 * @return
	 * @throws Exception 
	 */
	public void validateLoad() throws Exception {
		if(setup == null) {
			throw new Exception("Setup not found");
		}
		if (setup.getDatabase() == null) {
			throw new Exception("Setup Database not found");
		}

		Database dataBase = setup.getDatabase();
		//	Type
		if (dataBase.getType() == null) {
			throw new Exception("Database Type Not Found");
		}
		//	Validate only valid type
		if (!SUPPORTED_DATABSASES.contains(dataBase.getType())) {
			throw new Exception("Database Type Unsupported");
		}
		//	Host
		if (dataBase.getHost() == null) {
			throw new Exception("Database Host Not Found");
		}
		//	Port
		if (dataBase.getPort() == 0) {
			throw new Exception("Database Port Not Found");
		}
		//	Name
		if (dataBase.getName() == null) {
			throw new Exception("Database Name Not Found");
		}
		//	Password
		if (dataBase.getPassword() == null) {
			throw new Exception("Database Password Not Found");
		}
		CConnection connection = CConnection.get(
			dataBase.getType(),
			dataBase.getHost(), dataBase.getPort(), dataBase.getName(),
			dataBase.getUser(), dataBase.getPassword()
		);
		connection.setAppsHost("MyAppsServer");
		connection.setAppsPort(0);
		//	Set default init
		Ini.setProperty(Ini.P_CONNECTION, connection.toStringLong());
		Ini.setClient(true);
//		Ini.setProperty(Ini.P_ADEMPIERE_APPS_TYPE, "wildfly");
		Level logLevel = Level.parse(setup.getServer().getLog_level().toUpperCase());
		Ini.setProperty(Ini.P_TRACEFILE, logLevel.getName());
		CLogMgt.setLevel(logLevel);
		DB.setDBTarget(connection);
	}
	
	
	/**
	 * @return
	 * @see org.spin.template.setup.SetupWrapper#getDatabase()
	 */
	public final Database getDatabase() {
		return setup.getDatabase();
	}
	
	/**
	 * @return
	 * @see org.spin.template.setup.SetupWrapper#getServer()
	 */
	public final Server getServer() {
		return setup.getServer();
	}

	/**
	 * Get current instance
	 * @return
	 */
	public static SetupLoader getInstance() {
		return instance;
	}
	
	/**
	 * Load instance from file
	 * @param filePath
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static void loadSetup(String filePath) throws JsonParseException, JsonMappingException, IOException {
		instance = new SetupLoader(filePath);
	}
}
