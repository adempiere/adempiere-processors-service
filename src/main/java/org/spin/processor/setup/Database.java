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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                     *
 * GNU General Public License for more details.                                      *
 * You should have received a copy of the GNU General Public License                 *
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.processor.setup;

/**
 * Determinate all ADempiere client setup values for Human Resource
 * @author Yamel Senih
 */
public class Database {
	/**	Host Name	*/
	private String host;
	/**	Port	*/
	private int port;
	/**	User Name	*/
	private String user;
	/**	Password	*/
	private String password;
	/**	Database	*/
	private String name;
	/**	Database type	*/
	private String type;
	/**	IDLE Timeout	*/
	private long idle_timeout;
	/**	Minimum IDLE	*/
	private int minimum_idle;
	/**	Maximum Pool Size	*/
	private int maximum_pool_size;
	/**	Connection Timeout	*/
	private long connection_timeout;
	/**	Maximum Lifetime	*/
	private long maximum_lifetime;
	/**	Keepalive Time	*/
	private long keepalive_time;
	/**	Connection Test Query	*/
	private String connection_test_query;
	
	/**
	 * Default constructor
	 * @param host
	 * @param port
	 * @param user
	 * @param password
	 * @param name
	 * @param type
	 */
	public Database(String host, int port, String user, String password, String name, String type) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Default constructor without parameters
	 */
	public Database() {
		
	}

	/**
	 * @return the host
	 */
	public final String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public final int getPort() {
		return port;
	}

	/**
	 * @return the user
	 */
	public final String getUser() {
		return user;
	}

	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	public long getIdle_timeout() {
		return idle_timeout;
	}

	public int getMinimum_idle() {
		return minimum_idle;
	}

	public int getMaximum_pool_size() {
		return maximum_pool_size;
	}

	public long getConnection_timeout() {
		return connection_timeout;
	}

	public long getMaximum_lifetime() {
		return maximum_lifetime;
	}

	public long getKeepalive_time() {
		return keepalive_time;
	}

	public String getConnection_test_query() {
		return connection_test_query;
	}

	@Override
	public String toString() {
		return "Database [host=" + host + ", port=" + port + ", user=" + user + ", password=******, name="
				+ name + ", type=" + type + "]";
	}
}
