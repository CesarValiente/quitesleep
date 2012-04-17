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

package es.cesar.quitesleep.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.components.interfaces.IDialogs;
import es.cesar.quitesleep.components.listeners.DialogListener;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.CallLog;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.dialogs.fragments.WarningFragmentDialog;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class LogsFragment extends SherlockListFragment implements DialogListener {

	//Constants
	final private String CLASS_NAME = this.getClass().getName();
	final private int WARNING_REMOVE_DIALOG 	= 	0;
	final private int WARNING_REFRESH_DIALOG 	= 	1;
	final private int HELP_DIALOG 				= 	2;

	//Widgets Ids
	private final int removeCallLogMenuId = R.id.menu_calllog_remove;
	private final int refreshCallLogMenuId = R.id.menu_calllog_refresh;	
	
	//Widgets
	private WarningFragmentDialog warningRemoveDialog;
	private WarningFragmentDialog warningRefreshDialog;
	private ArrayAdapter<String> arrayAdapter;	
		
	
	
	
	@Override	
	public void onActivityCreated (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		
		getAllCallLogList();	
	}
	
	/**
	 * Get all CallLog list from de ddbb
	 */
	private void getAllCallLogList () {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			List<CallLog> callLogList = clientDDBB.getSelects().selectAllCallLog();
			List<String> callLogListString = convertCallLogList(callLogList);
			
			if (callLogListString != null) {
				arrayAdapter = new ArrayAdapter<String>(
					QuiteSleepApp.getContext(), 
					R.layout.logstab,
					R.id.logstab_textview_contact,
					callLogListString);
			
				setListAdapter(arrayAdapter);														
			}
			
			clientDDBB.close();
			
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}		
	}
	
	
	/**
	 * 
	 * @param 		contactList
	 * @return 		The contactList but only the list with the name contacts
	 * @see			List<String>
	 */
	private List<String> convertCallLogList (List<CallLog> callLogList) throws Exception {
		
		try {
			
			if (callLogList != null && callLogList.size()>0) {
				
				List<String> callLogListString = new ArrayList<String>();
				
				for (int i=0; i<callLogList.size(); i++) {
					String callLogString = callLogList.get(i).toString();
					if (callLogString != null)						
						callLogListString.add(callLogString);					
				}
				return callLogListString;
			}
			return null;
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Exception();
		}
	}
	
		
	/**
	 * Handler for clear the listView and the array adapter once we have been
	 * add all contacts to the banned list
	 */
	public final Handler handlerRemove = new Handler() {
		public void handleMessage(Message message) {						
			
			if (arrayAdapter != null && arrayAdapter.getCount()>0) {
				
				//int count = arrayAdapter.getCount();
				int numRemoveCallLogs = message.getData().getInt(
						ConfigAppValues.NUM_REMOVE_CALL_LOGS);
				
				//clear the arrayAdapter
				arrayAdapter.clear();
				
				//Show the toast message
				Toast.makeText(
                		QuiteSleepApp.getContext(),
                		numRemoveCallLogs + " " + QuiteSleepApp.getContext().getString(
                				R.string.menu_calllog_remove_toast),
                		Toast.LENGTH_SHORT).show();		
			}
		}
	};
	
	
	/**
	 * Handler for clear the listView and the array adapter once we have been
	 * add all contacts to the banned list
	 */
	public final Handler handlerRefresh = new Handler() {
		public void handleMessage(Message message) {						
			
			ArrayList<String> callLogListString = null;
			
			if (arrayAdapter != null) {																
				callLogListString = message.getData().getStringArrayList(
						ConfigAppValues.REFRESH_CALL_LOG);				
				
				//set the array adapter
				if (callLogListString != null) {
					
					//first delete the previous content list
					arrayAdapter.clear();
					
					//Second, add all call logs to the list
					for (int i=0; i<callLogListString.size(); i++) {
						String callLog = callLogListString.get(i);
						Log.d(CLASS_NAME, "callLog:" ) ;
						arrayAdapter.add(callLog);
					}
				}									
				//Show the toast message
				es.cesar.quitesleep.utils.Toast.d(
						QuiteSleepApp.getContext(),
						QuiteSleepApp.getContext().getString(
                				R.string.menu_calllog_refresh_toast),
                		Toast.LENGTH_SHORT);			
			}
			/* If the arrayAdapter previously doesn't have any call log, this
			 * not been initialized, so initialize now.
			 */
			else {				
				callLogListString = message.getData().getStringArrayList(
						ConfigAppValues.REFRESH_CALL_LOG);
				Log.d(CLASS_NAME, "inicializando arrayAdapter. " +
						"CallLogListString: " + callLogListString);
				arrayAdapter = new ArrayAdapter<String>(
						QuiteSleepApp.getContext(), 
						R.layout.logstab,
						R.id.logstab_textview_contact,
						callLogListString);
				setListAdapter(arrayAdapter);		
			}
		}
	};
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.calllogmenu, menu);		
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		
		switch (item.getItemId()) {
		
			case R.id.menu_calllog_refresh:
				//TODO
				break;		
			case R.id.menu_calllog_remove:
				//TODO
				break;
		}				
		return false;		
	}

	@Override
	public void clickYes() {
		// TODO Auto-generated method stub
		
	}
	
	
}
