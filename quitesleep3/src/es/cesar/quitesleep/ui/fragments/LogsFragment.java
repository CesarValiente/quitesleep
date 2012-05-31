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

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.components.listeners.LogsDialogListener;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.settings.ConfigAppValues.TypeFragment;
import es.cesar.quitesleep.tasks.LoadLogsTask;
import es.cesar.quitesleep.ui.dialogs.fragments.ContactsFragmentDialog;
import es.cesar.quitesleep.ui.dialogs.fragments.LogsFragmentDialog;
import es.cesar.quitesleep.ui.fragments.base.BaseListFragment;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class LogsFragment extends BaseListFragment implements LogsDialogListener {

	final private String CLASS_NAME = this.getClass().getName();
			
	
	@Override	
	public void onCreate (Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setHasOptionsMenu(true);
		
		Log.d(CLASS_NAME, "activity created");
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);					
		new LoadLogsTask(this).execute();		
	}

	@Override
	public void getDataInfo(List<String> dataInfoList) {
										
		getSherlockActivity().setSupportProgressBarIndeterminate(false);				
		
		if (dataInfoList != null) {
			Log.d(CLASS_NAME, "data received");
			
			myOwnAdapter = new ArrayAdapter<String>(
				QuiteSleepApp.getContext(), 
				R.layout.logstab,
				R.id.logstab_textview_contact,
				dataInfoList);
		
			setListAdapter(myOwnAdapter);
			refreshList();
		}										
	}
				
	/**
	 * Handler for clear the listView and the array adapter once we have been
	 * add all contacts to the banned list
	 */
	public final Handler handlerRemove = new Handler() {
		public void handleMessage(Message message) {												
			
			super.handleMessage(message);
			
			int numRemoveCallLogs = message.getData().getInt(
					ConfigAppValues.NUM_REMOVE_CALL_LOGS);
			
			//clear the arrayAdapter
			myOwnAdapter.clear();
			refreshList();
			
			//Show the toast message
			Toast.makeText(
            		QuiteSleepApp.getContext(),
            		numRemoveCallLogs + " " + QuiteSleepApp.getContext().getString(
            				R.string.menu_calllog_remove_toast),
            		Toast.LENGTH_SHORT).show();					
		}
	};
	
	
	/**
	 * Handler for clear the listView and the array adapter once we have been
	 * add all contacts to the banned list
	 */
	public final Handler handlerRefresh = new Handler() {
		public void handleMessage(Message message) {						
			
			ArrayList<String> callLogListString = null;
																		
			callLogListString = message.getData().getStringArrayList(
					ConfigAppValues.REFRESH_CALL_LOG);				
			
			//set the array adapter
			if (callLogListString != null) {
				
				//first delete the previous content list
				myOwnAdapter.clear();					
				
				//Second, add all call logs to the list
				for (int i=0; i<callLogListString.size(); i++) {
					String callLog = callLogListString.get(i);
					Log.d(CLASS_NAME, "callLog:" ) ;
					myOwnAdapter.add(callLog);
				}
				
				refreshList();
			}									
			//Show the toast message
			es.cesar.quitesleep.utils.Toast.d(
					QuiteSleepApp.getContext(),
					QuiteSleepApp.getContext().getString(
            				R.string.menu_calllog_refresh_toast),
            		Toast.LENGTH_SHORT);			
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
				SherlockDialogFragment refresh = LogsFragmentDialog.newInstance(
						this, ConfigAppValues.DialogType.REFRESH_ALL_LOGS);
				refresh.show(getSherlockActivity().getSupportFragmentManager(), "warningDialog");
				break;		
			case R.id.menu_calllog_remove:
				SherlockDialogFragment clear = LogsFragmentDialog.newInstance(
						this, ConfigAppValues.DialogType.REMOVE_ALL_LOGS);
				clear.show(getSherlockActivity().getSupportFragmentManager(), "warningDialog");
				break;
		}				
		return false;		
	}

	@Override
	public void clickYesClearLogs() {
		DialogOperations.removeAllCallLogs(getSherlockActivity(), myOwnAdapter, handlerRemove);		
	}

	@Override
	public void clickYesRefreshLogs() {
		DialogOperations.refreshAllCallLogs(getSherlockActivity(), myOwnAdapter, handlerRefresh);		
	}
	
}
