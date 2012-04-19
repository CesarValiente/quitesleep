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

package es.cesar.quitesleep.operations;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.data.syncdata.SyncContactsNew;
import es.cesar.quitesleep.data.syncdata.SyncContactsRefresh;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.dialogs.AddAllDialog;
import es.cesar.quitesleep.ui.dialogs.CallLogDialog;
import es.cesar.quitesleep.ui.dialogs.RemoveAllDialog;
import es.cesar.quitesleep.ui.dialogs.SyncContactsDialog;
import es.cesar.quitesleep.ui.menus.AddAllMenu;
import es.cesar.quitesleep.ui.menus.RefreshCallLogMenu;
import es.cesar.quitesleep.ui.menus.RemoveAllMenu;
import es.cesar.quitesleep.ui.menus.RemoveCallLogMenu;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class DialogOperations {
	
	private final static String CLASS_NAME = "es.cesar.quitesleep.operations.DialogOperations";
		

	/**
	 * Sync contacts data between SQLite and DB4O databases when user push the
	 * sync button any time. 
	 * It is done using a thread for it.
	 */
	public static void syncContactsRefresh (Context context) {
		
		try {										
			SyncContactsDialog syncDialog = new SyncContactsDialog();		
			
			SyncContactsRefresh syncContacts = 
				new SyncContactsRefresh(context, syncDialog);						
					
			syncDialog.showDialogRefreshList(context);
			syncContacts.start();
																																	
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Error();
		}
	}		
	
	
	/**
	 * Function that add all contacts to the banned list in one separated thread,
	 * and show a progressDialog fow wait for it, and an handler for clear the
	 * arrayAdapter one habe been finished.
	 */
	public static void addAllContacts (
			Context  context, 
			ArrayAdapter<String> arrayAdapter, 
			Handler handler) {
		
		try {
						
			/* Only can selectAll contacts if the arrayAdapter is != null and
			 * have content, that is the state of it when prevously we add all
			 * contacts.  
			 */
			if (arrayAdapter != null && arrayAdapter.getCount()>0) {	

				//Create the progressDialog used while the system add all contacts
				AddAllDialog addAllDialog = new AddAllDialog();		
				
				//Create the object that this thread will do the process
				AddAllMenu addAllMenu = new AddAllMenu(arrayAdapter, addAllDialog, handler);
				
				//Show the progress dialog and run the thread for add all contacts
				addAllDialog.showDialog(context);
				addAllMenu.start();								
			}
											
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Error();
		}
	}
	
	/**
	 * Function that start the remove banned contacts from the banned list
	 * in a separated thread and use a progressDialog for wait for it, and 
	 * handler for clear the arrayAdapter once have finished.
	 */
	public static void removeAllContacts (
			Context context, 
			ArrayAdapter<String> arrayAdapter, 
			Handler handler) {
		
		try {
			/* Only can selectAll contacts if the arrayAdapter is != null and
			 * have content, that is the state of it when prevously we add all
			 * contacts.  
			 */
			if (arrayAdapter != null && arrayAdapter.getCount()>0) {	

				//Create the progressDialog used while the system add all contacts
				RemoveAllDialog removeAllDialog = new RemoveAllDialog();
				
				//Create the object that this thread will do the process
				RemoveAllMenu removeAllMenu = 
					new RemoveAllMenu(arrayAdapter, removeAllDialog, handler);
				
				//Show the progress dialog and run the thread for add all contacts
				removeAllDialog.showDialog(context);
				removeAllMenu.start();								
			}
											
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Error();
		}
	}
	
	
	/**
	 * Function that does the operations about start and stop the sms service
	 */
	public static void checkSmsService (Context context, boolean isChecked) {
		
		try {
			
			boolean result = StartStopServicesOperations.startStopSmsService(isChecked);
			
			if (isChecked) {
				
				if (result) 
					//All right, start the service was ok!
					es.cesar.quitesleep.utils.Toast.r(
	                		context,
	                		context.getString(
	                				R.string.smssettings_toast_start_service),
	                		Toast.LENGTH_SHORT);
				else
					//An error has ocurred!!
					es.cesar.quitesleep.utils.Toast.r(
	                		context,
	                		context.getString(
	                				R.string.smssettings_toast_fail_service),
	                		Toast.LENGTH_SHORT);								
			}else {																												
				if (result)
					//All right, stop the service was ok!
					es.cesar.quitesleep.utils.Toast.r(
	                		context,
	                		context.getString(
	                				R.string.smssettings_toast_stop_service),
	                		Toast.LENGTH_SHORT);
				else
					//An error has ocurred!!
					es.cesar.quitesleep.utils.Toast.r(
	                		context,
	                		context.getString(
	                				R.string.smssettings_toast_fail_service),
	                		Toast.LENGTH_SHORT);								
			}
			
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));		
		}
	}
	
	
	/**
	 * Function that does the operations about start and stop the mail service
	 */
	public static void checkMailService (Context context, boolean isChecked) {
		
		try {
			
			boolean result = StartStopServicesOperations.startStopMailService(isChecked);
			
			if (isChecked) {
				
				if (result) 
					//All right, start the service was ok!
					es.cesar.quitesleep.utils.Toast.r(
	                		context,
	                		context.getString(
	                				R.string.mailsettings_toast_start_service),
	                		Toast.LENGTH_SHORT);
				else
					//An error has ocurred!!
					es.cesar.quitesleep.utils.Toast.r(
	                		context,
	                		context.getString(
	                				R.string.mailsettings_toast_fail_service),
	                		Toast.LENGTH_SHORT);								
			}else {																												
				if (result)
					//All right, stop the service was ok!
					es.cesar.quitesleep.utils.Toast.r(
	                		context,
	                		context.getString(
	                				R.string.mailsettings_toast_stop_service),
	                		Toast.LENGTH_SHORT);
				else
					//An error has ocurred!!
					es.cesar.quitesleep.utils.Toast.r(
	                		context,
	                		context.getString(
	                				R.string.mailsettings_toast_fail_service),
	                		Toast.LENGTH_SHORT);								
			}
			
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));		
		}
	}
	
	
	/**
	 * Remove all call log objects from the ddbb for clean the call log information
	 * list.
	 * 
	 * @param context
	 * @param arrayAdapter
	 * @param handler
	 */
	public static void removeAllCallLogs (
			Context context, 
			ArrayAdapter<String> arrayAdapter, 
			Handler handler) {
		
		try {
			/* Only can selectAll contacts if the arrayAdapter is != null and
			 * have content, that is the state of it when prevously we add all
			 * contacts.  
			 */
			if (arrayAdapter != null && arrayAdapter.getCount()>0) {	

				//Create the progressDialog used while the system add all contacts
				CallLogDialog callLogDialog = new CallLogDialog();
				
				//Create the object that this thread will do the process
				RemoveCallLogMenu callLogMenu = 
					new RemoveCallLogMenu(arrayAdapter, callLogDialog, handler);
				
				//Show the progress dialog and run the thread for add all contacts
				callLogDialog.showDialog(context, ConfigAppValues.DialogType.REMOVE_ALL_LOGS);
				callLogMenu.start();								
			}
											
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Error();
		}
	}
	
	public static void refreshAllCallLogs (
			Context context, 
			ArrayAdapter<String> arrayAdapter, 
			Handler handler) {
		
		try {
						
			//Create the progressDialog used while the system add all contacts
			CallLogDialog callLogDialog = new CallLogDialog();
				
			//Create the object that this thread will do the process
			RefreshCallLogMenu refreshCallLogMenu = 
				new RefreshCallLogMenu(arrayAdapter, callLogDialog, handler);
				
			//Show the progress dialog and run the thread for add all contacts
			callLogDialog.showDialog(context, ConfigAppValues.DialogType.REFRESH_ALL_LOGS);
			refreshCallLogMenu.start();											
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}	
	
	/**
	 * This function synchronize the db4o database with SQLite database at first
	 * time when QuiteSleep is launched
	 * @param context
	 * @param handler
	 */
	public static void synchronizeFirstTime (Context context, Handler handler) {
		
		try {
			
			SyncContactsDialog syncDialog = new SyncContactsDialog();
			
			SyncContactsNew syncContactsNew = new SyncContactsNew(
					context, 
					syncDialog, 
					handler);
						
			Log.d(CLASS_NAME, "Proceed with the synchronization for the first time");								
				
			syncDialog.showDialogFirstTime(context);
			syncContactsNew.start();
							
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	

}
