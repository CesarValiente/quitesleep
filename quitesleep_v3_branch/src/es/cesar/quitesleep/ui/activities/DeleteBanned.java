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

package es.cesar.quitesleep.ui.activities;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.components.adapters.ContactListAdapter;
import es.cesar.quitesleep.components.listeners.LoadContactsListener;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.Banned;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.settings.ConfigAppValues.TypeContacts;
import es.cesar.quitesleep.tasks.LoadContactsDataTask;
import es.cesar.quitesleep.ui.dialogs.fragments.WarningFragmentDialog;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.Log;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class DeleteBanned extends BaseListSherlockFragment implements OnItemClickListener {
	
	//Constants
	final private String CLASS_NAME = this.getClass().getName();
	final private int WARNING_DIALOG = 0;
	
	//Widgets Ids
	private final int removeAllMenuId = R.id.menu_removeall;
	
	//Widgets
	private WarningFragmentDialog warningDialog;			
	
	//Attributes
	private String selectContactName;			
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);				
			
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		/*
		warningDialog = new WarningDialog(
				this, 
				ConfigAppValues.WARNING_REMOVE_ALL_CONTACTS);		
		*/
		getListView().setOnItemClickListener(this);
		
		setSupportProgressBarIndeterminateVisibility(true);
		new LoadContactsDataTask(this, TypeContacts.BANNED).execute();
	}
	
	
	/**
	 * Get all banned contact list from the database
	 */
	@Override
	public void getDataContacts (List<String> contactList) {
			
		setSupportProgressBarIndeterminateVisibility (false);
			
		if (contactList != null) {
			myOwnAdapter = new ContactListAdapter<String>(
				getApplicationContext(), 
				R.layout.list_item,					
				contactList, 
				this);
		
			getListView().setAdapter(myOwnAdapter);
			
			refreshList();																							
		}				
	}
	
	/**
	 * Function that convert all banned contact list in Contact objects to
	 * one List<String> with only the contact name attribute.
	 * 
	 * @param 		contactList
	 * @return 		The contactList but only the list with the name contacts
	 * @see			List<String>
	 */
	private List<String> convertContactList (List<Banned> bannedList) throws Exception {
		
		try {
			
			if (bannedList != null && bannedList.size()>0) {
				
				List<String> bannedListString = new ArrayList<String>(bannedList.size());
				
				for (int i=0; i<bannedList.size(); i++) {
					Banned banned = bannedList.get(i);
					String contactName = banned.getContact().getContactName();
					if (contactName == null)
						contactName = "";
					bannedListString.add(contactName);
					
				}
				return bannedListString;
			}
			return null;
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Exception();
		}
	}
	
	@Override
	public void onItemClick(
			AdapterView<?> parent, 
			View view,
			int position, 
			long id) {
		
		try {													
			selectContactName = (String) myOwnAdapter.getItem(position);									
			
			/* If we like to use one subactivity for show better contact details
			 * and edit what phone number and/or mail addresses are used for 
			 * send busy response, and remove contact from banned list.
			 */			
			Intent intentEditContact = new Intent(this, EditContact.class);
			intentEditContact.putExtra(ConfigAppValues.CONTACT_NAME, selectContactName);
			startActivityForResult(intentEditContact, ConfigAppValues.REQCODE_EDIT_CONTACT);
									
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));			
		}		
	}
	
	@Override	
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) {
			case ConfigAppValues.REQCODE_EDIT_CONTACT:				
				if (resultCode == Activity.RESULT_OK)
					myOwnAdapter.remove(selectContactName);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Create the activity dialogs used for it
	 * 
	 * @param id
	 * @return the dialog for the option specified
	 * @see Dialog
	 */
	@Override
	protected Dialog onCreateDialog (int id) {
		
		Dialog dialog;
		
		switch (id) {
			case WARNING_DIALOG:				
				//dialog = warningDialog.getAlertDialog();				
				break;
			default:
				dialog = null;
		}
		
		//return dialog;
		return null;
	}
	
	/**
	 * This function prepare the dalogs every time to call for some of this
	 * 
	 *  @param int
	 *  @param dialog
	 */
	@Override
	protected void onPrepareDialog (int idDialog, Dialog dialog) {
						
		switch (idDialog) {			
			case WARNING_DIALOG:
				warningDialog.setContext(this);
				warningDialog.setArrayAdapter(myOwnAdapter);
				warningDialog.setHandler(handler);										
				break;
				
			default:
				break;
		}								
	}

	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
										
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.removeallmenu, menu);
		
		return true;					
	}
	
	/**
	 * @param 		item
	 * @return 		boolean
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
							
		switch (item.getItemId()) {
		
			//To comeback to the previous activity we finalize it	
			case android.R.id.home :
				finish();			
				break;
			case removeAllMenuId:					
				showDialog(WARNING_DIALOG);
				break;					
			default:
				break;
		}
		return false;					
	}
	
	
	
	/**
	 * Handler for clear the listView and the array adapter once we have been
	 * add all contacts to the banned list
	 */
	public final Handler handler = new Handler() {
		public void handleMessage(Message message) {						
			
			if (myOwnAdapter != null && myOwnAdapter.getCount()>0) {
				
				//int count = arrayAdapter.getCount();
				int numRemoveContacts = message.getData().getInt(
						ConfigAppValues.NUM_REMOVE_CONTACTS);
				
				//clear the arrayAdapter
				myOwnAdapter.clear();
				
				//Show the toast message
				es.cesar.quitesleep.utils.Toast.d(
						QuiteSleepApp.getContext(),
                		numRemoveContacts + " " + QuiteSleepApp.getContext().getString(
                				R.string.menu_removeall_toast_removecount),
                		Toast.LENGTH_SHORT);		
			}
		}
	};		

}
