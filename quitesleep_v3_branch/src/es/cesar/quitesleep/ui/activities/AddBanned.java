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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.components.adapters.ContactListAdapter;
import es.cesar.quitesleep.components.dialogs.WarningDialog;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.Contact;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.Log;
import es.cesar.quitesleep.utils.Toast;

/**
 * 
 * @author		Cesar Valiente Gordo
 * @mail		cesar.valiente@gmail.com	
 * 
 * @version 0.1, 03-13-2010
 * 
 * Class for AddContacts to the banned user list
 * 
 */
public class AddBanned extends Activity implements OnItemClickListener {
	
	//Constants
	final private String CLASS_NAME = this.getClass().getName();
	final private int WARNING_DIALOG = 0;	
		
	//Widgets Id's
	final private int addAllMenuId = R.id.menu_addall; 
	
	//Widgets
	private WarningDialog warningDialog;
	private ContactListAdapter<String> myOwnAdapter;
	private ListView listView;
	
	//Auxiliar attributes
	private String selectContactName;
		
	
	/**
	 * onCreate
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.contact_list);
		listView = (ListView)findViewById(R.id.contact_list_view);		
		warningDialog = new WarningDialog(
				this, 
				ConfigAppValues.WARNING_ADD_ALL_CONTACTS);						
				
		getAllContactList();		
		listView.setOnItemClickListener(this);		
	}
	
	
	
	/**
	 * Get all not banned contacts from the database and parse it for create
	 * one contact list only with their contactNames
	 */
	private void getAllContactList () {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			List<Contact> contactList = clientDDBB.getSelects().selectAllNotBannedContacts();
			List<String> contactListString = convertContactList(contactList);						
			
			if (contactListString != null) {
				myOwnAdapter = new ContactListAdapter<String>(
					getApplicationContext(), 
					R.layout.list_item,
					contactListString, 
					this);
			
				listView.setAdapter(myOwnAdapter);
				
				listView.setFastScrollEnabled(true);
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
	private List<String> convertContactList (List<Contact> contactList) throws Exception {
		
		try {
			
			if (contactList != null && contactList.size()>0) {
				
				List<String> contactListString = new ArrayList<String>();
				
				for (int i=0; i<contactList.size(); i++) {
					String contactName = contactList.get(i).getContactName();
					if (contactName != null)						
						contactListString.add(contactName);					
				}
				return contactListString;
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
			 * and select what phone number and/or mail addresses are used for 
			 * send busy response.
			 */			
			Intent intentContactDetails = new Intent(this,ContactDetails.class);
			intentContactDetails.putExtra(ConfigAppValues.CONTACT_NAME, selectContactName);
			startActivityForResult(intentContactDetails, ConfigAppValues.REQCODE_CONTACT_DETAILS);
						
									
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));			
		}		
	}		
	
	
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) {
			case ConfigAppValues.REQCODE_CONTACT_DETAILS:				
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
				dialog = warningDialog.getAlertDialog();				
				break;
			default:
				dialog = null;
		}
		
		return dialog;	
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
		
									
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.addallmenu, menu);
		
		return true;					
	}
	
	/**
	 * @param 		item
	 * @return 		boolean
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
							
		switch (item.getItemId()) {
		
			case addAllMenuId:					
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
			
			final String NUM_BANNED = "NUM_BANNED";
			
			if (myOwnAdapter != null && myOwnAdapter.getCount()>0) {
				
				//int count = arrayAdapter.getCount();
				int numBanned = message.getData().getInt(NUM_BANNED);
				
				//clear the arrayAdapter
				myOwnAdapter.clear();
				
				//Show the toast message
				Toast.d(
                		ConfigAppValues.getContext(),
                		numBanned + " " + ConfigAppValues.getContext().getString(
                				R.string.menu_addall_toast_insertscount),
                		android.widget.Toast.LENGTH_SHORT);		
			}
		}
	};
	
	
	
}
 