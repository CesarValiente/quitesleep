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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.components.adapters.ContactListAdapter;
import es.cesar.quitesleep.components.listeners.ContactDialogListener;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.settings.ConfigAppValues.TypeFragment;
import es.cesar.quitesleep.tasks.LoadContactsDataTask;
import es.cesar.quitesleep.ui.activities.EditContact;
import es.cesar.quitesleep.ui.dialogs.fragments.ContactsFragmentDialog;
import es.cesar.quitesleep.ui.fragments.base.BaseListFragment;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.Log;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class DeleteBannedFragment extends BaseListFragment implements OnItemClickListener, ContactDialogListener {
	
	//Constants
	final private String CLASS_NAME = getClass().getName();	
	
	//Widgets Ids
	private final int removeAllMenuId = R.id.menu_removeall;

	//Attributes
	private String selectContactName;			
	
	public static DeleteBannedFragment newInstance () {
		
		DeleteBannedFragment deleteBanned = new DeleteBannedFragment();
		return deleteBanned;
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
				
		super.onActivityCreated(savedInstanceState);							
		setHasOptionsMenu(true);
		
		getListView().setOnItemClickListener(this);		
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
		new LoadContactsDataTask(this, TypeFragment.REMOVE_CONTACTS).execute();
	}
	
	
	/**
	 * Get all banned contact list from the database
	 */
	@Override
	public void getDataInfo (List<String> contactList) {
			
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility (false);
			
		if (contactList != null) {
			myOwnAdapter = new ContactListAdapter<String>(
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
								
			Intent intentEditContact = new Intent(QuiteSleepApp.getContext(), EditContact.class);
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
				if (resultCode == Activity.RESULT_OK) {
					myOwnAdapter.remove(selectContactName);
					refreshList();
				}
				break;
			default:
				break;
		}
	}
	
	

	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
												
		inflater.inflate(R.menu.removeallmenu, menu);							
	}
	
	/**
	 * @param 		item
	 * @return 		boolean
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
							
		switch (item.getItemId()) {		
			case removeAllMenuId:					
				SherlockDialogFragment dialogFragment = ContactsFragmentDialog.newInstance(
						this, ConfigAppValues.DialogType.REMOVE_ALL_CONTACTS);
				dialogFragment.show(getSherlockActivity().getSupportFragmentManager(), "warningDialog");
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
										

			int numRemoveContacts = message.getData().getInt(
					ConfigAppValues.NUM_REMOVE_CONTACTS);
			
			myOwnAdapter.clear();
			refreshList();
			
			//Show the toast message
			es.cesar.quitesleep.utils.Toast.d(
					QuiteSleepApp.getContext(),
            		numRemoveContacts + " " + QuiteSleepApp.getContext().getString(
            				R.string.menu_removeall_toast_removecount),
            		Toast.LENGTH_SHORT);		
			
		}
	};

	@Override
	public void clickYes() {	
		DialogOperations.removeAllContacts(getSherlockActivity(), myOwnAdapter, handler);
	}		

}
