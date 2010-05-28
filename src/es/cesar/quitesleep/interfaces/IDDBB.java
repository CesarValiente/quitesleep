/* 
 	Copyright 2010 Cesar Valiente Gordo
 
 	This file is part of QuiteSleep.

    QuiteSleep is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License, or
    (at your option) any later version.

    QuiteSleep is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with QuiteSleep.  If not, see <http://www.gnu.org/licenses/>.
*/

package es.cesar.quitesleep.interfaces;

import android.os.Environment;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public interface IDDBB {
		
	//For the sdcard io
	public final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	public final String QUITESLEEP_PATH = "quitesleep";
	
	//For the sdcard & internal data app
	public final String DDBB_DIR = "ddbb"; 
	public final String DDBB_FILE = "quitesleep.db";
			
	
	//Constante para especificar el nivel de profundidad de busqueda en un 
	//objeto
	public final int DEEP = 3;
	
	public static final String SEMAPHORE = "SEMAPHORE";		
	
}
