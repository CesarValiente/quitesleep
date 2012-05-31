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

package es.cesar.quitesleep.data.ddbb;

import android.util.Log;

import com.db4o.ObjectContainer;

import es.cesar.quitesleep.components.interfaces.IDDBB;
import es.cesar.quitesleep.data.models.Banned;
import es.cesar.quitesleep.data.models.BlockCallsConf;
import es.cesar.quitesleep.data.models.CallLog;
import es.cesar.quitesleep.data.models.Contact;
import es.cesar.quitesleep.data.models.Mail;
import es.cesar.quitesleep.data.models.MuteOrHangUp;
import es.cesar.quitesleep.data.models.Phone;
import es.cesar.quitesleep.data.models.Schedule;
import es.cesar.quitesleep.data.models.Settings;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 * 
 * @note This class is also used as update parent class, because the operations
 * are the same in both cases
 *
 */
public class Inserts implements IDDBB{
	
	private final String CLASS_NAME = getClass().getName();
	
	//The link with the database for can does operations
	protected ObjectContainer db;		
	
	
	/**
	 * Constructor
	 * 
	 * @param db
	 */
	public Inserts (ObjectContainer db) {
		
		this.db = db;				
	}

	
	/**
	 * Insert one Contact object in the DDBB.
	 * 
	 * @param 		contact
	 * @return		true or false depends the operation result
	 * @see			boolean
	 */
	public boolean insertContact (Contact contact) {
		
		try {
			
			synchronized(SEMAPHORE) {
				
				db.store(contact);
				return true;
			}
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}
	
	/**
	 * 
	 * @param phone
	 * @return boolean
	 */
	public boolean insertPhone (Phone phone) {
		
		try {
			synchronized (SEMAPHORE) {
				db.store(phone);
				return true;			
			}
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}
	
	/**
	 * 
	 * @param mail
	 * @return
	 */
	public boolean insertMail (Mail mail) {
		
		try {
			synchronized (SEMAPHORE) {
				db.store(mail);
				return true;
			}
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}
	
	/**
	 * 
	 * @param banned
	 * @return
	 */
	public boolean insertBanned (Banned banned) {
		
		try {
			synchronized (SEMAPHORE) {
				
				db.store(banned);
				return true;				
			}
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}
	
	/**
	 * 
	 * @param schedule
	 * @return
	 */
	public boolean insertSchedule (Schedule schedule) {
		
		try {
			synchronized(SEMAPHORE) {
				
				db.store(schedule);
				return true;
			}
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}
	
	/**
	 * 
	 * @param settings
	 * @return
	 */
	public boolean insertSettings (Settings settings) {
		
		try {
			synchronized (SEMAPHORE) {
				
				db.store(settings);
				return true;
			}
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}
	
	/**
	 * 
	 * @param callLog
	 * @return
	 */
	public boolean insertCallLog (CallLog callLog) {
		
		try {
			synchronized (SEMAPHORE) {
				
				db.store(callLog);
				return true;
				
			}
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}
	
	
	/**
	 * Function which inserts the BlockCallsConf passed as parameter.
	 * @param blockCallsConf
	 * @return
	 */
	public boolean insertBlockCallsConf (BlockCallsConf blockCallsConf) {
		
		try {
			synchronized (SEMAPHORE) {
				db.store(blockCallsConf);
				return true;			
			}
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}
	
	/**
	 * This function inserts a MuteOrHangUp in the ddbb.
	 * 
	 * @param muteOrHangup
	 * @return
	 */
	public boolean insertMuteOrHangUp (MuteOrHangUp muteOrHangup) {
		
		try {
			synchronized (SEMAPHORE) {
				
				db.store(muteOrHangup);
				return true;
			}
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}

}
