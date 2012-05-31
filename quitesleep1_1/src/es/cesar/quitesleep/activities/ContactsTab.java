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

package es.cesar.quitesleep.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.dialogs.WarningDialog;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.subactivities.About;
import es.cesar.quitesleep.subactivities.AddBanned;
import es.cesar.quitesleep.subactivities.DeleteBanned;
import es.cesar.quitesleep.subactivities.Help;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class ContactsTab extends Activity implements OnClickListener {
	
	final private String CLASS_NAME = getClass().getName();	
	final private int WARNING_DIALOG = 1;
	
	//Ids for the button widgets
	private final int addBannedId = R.id.contacts_button_addBanned;
	private final int deleteBannedId = R.id.contacts_button_deleteBanned;	
	private final int syncContactsID = R.id.contacts_button_syncContacts;
	
	//Ids for option menu
	final int aboutMenuId = R.id.menu_information_about;
	final int helpMenuId = R.id.menu_information_help;

	//Ids for warning dialog
	private WarningDialog warningDialog;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {			
		
		super.onCreate(savedInstanceState);				
		
		setContentView(R.layout.contactstab);
		
		try {															
						
			//Instanciate all buttons
			Button addBannedButton = (Button)findViewById(addBannedId);
			Button deleteBannedButton = (Button)findViewById(deleteBannedId);
			Button syncContactsButton = (Button)findViewById(syncContactsID);
									
			//Define the buttons listener
			addBannedButton.setOnClickListener(this);
			deleteBannedButton.setOnClickListener(this);
			syncContactsButton.setOnClickListener(this);
			
			warningDialog = new WarningDialog(this, ConfigAppValues.WARNING_SYNC_CONTACTS);	

								
		}catch (Exception e) {
			e.printStackTrace();
			if (QSLog.DEBUG_E) QSLog.e(CLASS_NAME, e.toString());			
		}		
	}
	
	
	/**
	 * onClick method for the view widgets
	 * 
	 * @param view	View of the used widget
	 */
	public void onClick (View view) {
		
		int viewId = view.getId();
		
		
		switch (viewId) {
			case addBannedId:
				
				Intent intentAddContacts = new Intent(this, AddBanned.class);											
				startActivityForResult(intentAddContacts, ConfigAppValues.REQCODE_ADD_BANNED);
				break;
				
			case deleteBannedId:
				Intent intentViewContacts = new Intent(this, DeleteBanned.class);
				startActivityForResult(intentViewContacts, ConfigAppValues.REQCODE_DELETE_BANNED);
				break;
				
			case syncContactsID:
				showDialog(WARNING_DIALOG);
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
				if (QSLog.DEBUG_D) QSLog.d(CLASS_NAME, "Create the WarningDialog for 1st time");
				dialog = warningDialog.getAlertDialog();	
				break;
			default:
				dialog = null;
		}
		
		return dialog;	
	}
	
	
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {		
		//TODO
		if (QSLog.DEBUG_D) QSLog.d(CLASS_NAME, "RequestCode: " + requestCode + "\nResultCode: " + resultCode);		
	}
		
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		
		try {							
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.informationmenu, menu);
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E) QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;
		}		
	}
	
	/**
	 * @param 		item
	 * @return 		boolean
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		
		try {
			
			switch (item.getItemId()) {
				case aboutMenuId:
					Intent intentAbout = new Intent(this, About.class);
					startActivityForResult(intentAbout, ConfigAppValues.LAUNCH_ABOUT);
					break;
				case helpMenuId:
					Intent intentHelp = new Intent(this, Help.class);
					startActivityForResult(intentHelp, ConfigAppValues.LAUNCH_ABOUT);
					break;
				default:
					break;
			}
			return false;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E) QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;			
		}
	}
	
									
}
