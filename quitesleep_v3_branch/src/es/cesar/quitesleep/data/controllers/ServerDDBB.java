/* 
 	Copyright 2010 Cesar Valiente Gordo
 
 	This file is part of QuiteSleep.

    QuiteSleep is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    QuiteSleep is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with QuiteSleep.  If not, see <http://www.gnu.org/licenses/>.
*/

package es.cesar.quitesleep.data.controllers;

import android.content.Context;

import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;

import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.components.interfaces.IDDBB;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.Log;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class ServerDDBB implements IDDBB {
	
	private final static String CLASS_NAME = "es.cesar.quitesleep.ddbb.ServerDDBB";			
	
	/**
	 * This object is for get only one Singleton object and create ONLY inside
	 * of this class mode.
	 */
	private static ServerDDBB SINGLETON = null;	
	
	/**
	 * This object is the server DDBB file properly said
	 */
	private static ObjectServer server = null;
	
	
	/**
	 * Constructor for the ServerDDBB		
	 */
	private ServerDDBB () {
		
		try
		{																						
			ServerConfiguration configuration = 
				Db4oClientServer.newServerConfiguration();						
			
			configuration.common().allowVersionUpdates(true);
			configuration.common().activationDepth(DEEP);
																											
			server = Db4oClientServer.openServer(
					configuration, getDDBBFile(DDBB_FILE), 0);			
			if (server == null)
				Log.d(CLASS_NAME, "server null!!!!");
																													
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
		
	
	/**
	 * Create the server instance if the server doesn't create before
	 */
	private synchronized static void createInstance () {
				
		if (server == null)  {
			Log.d(CLASS_NAME, "Creating server");
			SINGLETON = new ServerDDBB();
		}
	}
	
	/**
	 * Get the server DDBB instance, if the singleton object doesn't create
	 * before, we create for use it.
	 * 
	 * @return		The server object
	 * @see			ObjectsServer
	 */
	public static ObjectServer getServer() {			
	
		if (SINGLETON == null)
			createInstance();
		return server;								
	}
		
	
	/**
	 * Destructor class function
	 */
	protected void finalize () {
		if (server != null) {
			server.close();
			server = null;
		}
	}
	
	
	/**
	 * Function that defrag de Server DDBB File for get free space
	 */
	public static void defrag () {
		
		try
		{
			if (server != null)
				com.db4o.defragment.Defragment.defrag(
						getDDBBFile(DDBB_FILE), 
						getDDBBFile(DDBB_FILE + ".old")); 
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	
	
	/**
	 * Function that returns the ddbb file string from the app
	 * 
	 * @param 		ddbb_file
	 * @return		The ddbb filename
	 * @see			String
	 */
	private static String getDDBBFile(String ddbb_file) {
									
		return QuiteSleepApp.getContext().getDir(
				DDBB_DIR, 
				Context.MODE_PRIVATE) 
				+ "/" + ddbb_file;							
		
		//NOTA: Para escribir en la sdcard, no se utiliza getContext().getDir()
		//Se utiliza FileOutputStream y las funciones normales de java.io
	}
} 



