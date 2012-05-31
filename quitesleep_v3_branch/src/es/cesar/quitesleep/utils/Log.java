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

package es.cesar.quitesleep.utils;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@icemobile.com
 *
 * This class is used to create 
 */
public class Log {
	
	private static final boolean DEBUG = true;
	
	public static void d (String tag, String content) {
		if (DEBUG)
			android.util.Log.d(tag, content);
	}
	
	public static void e (String tag, String content) {
		if (DEBUG)
			android.util.Log.e(tag, content);
	}
	
	public static void w (String tag, String content) {
		if (DEBUG)
			android.util.Log.w(tag, content);
	}
	
	public static void v (String tag, String content) {
		if (DEBUG)
			android.util.Log.v(tag, content);
	}
	
	public static void i (String tag, String content) {
		if (DEBUG)
			android.util.Log.i(tag, content);
	}
	
	

}
