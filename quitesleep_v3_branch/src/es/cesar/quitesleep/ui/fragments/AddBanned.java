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

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.components.adapters.ContactListAdapter;
import es.cesar.quitesleep.components.listeners.DialogListener;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.settings.ConfigAppValues.TypeContacts;
import es.cesar.quitesleep.tasks.LoadContactsDataTask;
import es.cesar.quitesleep.ui.activities.BaseListSherlockFragment;
import es.cesar.quitesleep.ui.activities.ContactDetails;
import es.cesar.quitesleep.ui.dialogs.fragments.WarningFragmentDialog;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.Log;
import es.cesar.quitesleep.utils.Toast;

/**
 * @author		Cesar Valiente Gordo
 * @mail		cesar.valiente@gmail.com	
 * 
 * Class for AddContacts to the banned user list
 */
public class AddBanned extends BaseListSherlockFragment implements OnItemClickListener, DialogListener {
	
	//Constants
	final private String CLASS_NAME = this.getClass().getName();	
	
		
	//Auxiliar attributes
	private String selectContactName;			
			
	/**
	 * Creates a new instance of {@link AddBanned}
	 * @return {@link AddBanned}
	 */
	public static AddBanned newInstance () {				
		
		AddBanned addBanned = new AddBanned();								
		return addBanned;		
	}
	
		
	/**
	 * onCreate
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
							
		getListView().setOnItemClickListener(this);		
		
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
		
		Log.d(CLASS_NAME, "in addBanned");
		
		new LoadContactsDataTask(this, TypeContacts.ADD_CONTACTS).execute();
	}
	
	
	
	
	@Override
	public void getDataContacts (List<String> contactList) {
			
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility (false);
			
		if (contactList != null) {
			Log.d(CLASS_NAME, "contact list != null");
			
			myOwnAdapter 
			 = new ContactListAdapter<String>(
				QuiteSleepApp.getContext(), 
				R.layout.list_item,
				contactList, 
				this);
					
			setListAdapter(myOwnAdapter);
			
			refreshList();		
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
				
			Intent intentContactDetails = new Intent(QuiteSleepApp.getContext(),ContactDetails.class);
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
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
												
		inflater.inflate(R.menu.addallmenu, menu);								
	}
	
	/**
	 * @param 		item
	 * @return 		boolean
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
							
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
					
			case R.id.menu_addall:	
				Log.d(CLASS_NAME, "click in ADD contacts");
				SherlockDialogFragment dialogFragment = WarningFragmentDialog.newInstance(
						this, ConfigAppValues.DialogType.ADD_ALL_CONTACTS);
				dialogFragment.show(getSherlockActivity().getSupportFragmentManager(), "warningDialog");					
				break;									
			default:
				break;
		}
		return false;			
	}
	
	/**
	 * Handler for clear the listView and the array adapter once we have added
	 * all contacts to the banned list
	 */
	public final Handler handler = new Handler() {
		public void handleMessage(Message message) {
			
			final String NUM_BANNED = "NUM_BANNED";
			
			if (myOwnAdapter != null && myOwnAdapter.getCount()>0) {
				
				//int count = arrayAdapter.getCount();
				int numBanned = message.getData().getInt(NUM_BANNED);
				
				refreshList();
				
				//Show the toast message
				Toast.d(
					QuiteSleepApp.getContext(),
            		numBanned + " " + QuiteSleepApp.getContext().getString(
            				R.string.menu_addall_toast_insertscount),
            		android.widget.Toast.LENGTH_SHORT);		
			}
		}
	};


	@Override
	public void clickYes() {
		Log.d(CLASS_NAME, "ClickYes!!!");
		 DialogOperations.addAllContacts(QuiteSleepApp.getContext(), myOwnAdapter, handler);		
	}			
}