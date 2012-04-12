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

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.components.dialogs.WarningDialog;
import es.cesar.quitesleep.components.interfaces.IDialogs;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.activities.AddBanned;
import es.cesar.quitesleep.ui.activities.DeleteBanned;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.Log;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class ContactsFragment extends SherlockFragment implements OnClickListener, IDialogs {
	
	final private String CLASS_NAME = getClass().getName();	
	final private int SYNCHRONIZE_DIALOG 	= 1;
	final private int ABOUT_DIALOG 			= 2; 
	final private int HELP_DIALOG 			= 3;	
	
	//Ids for the button widgets
	private final int addBannedId = R.id.contacts_button_addBanned;
	private final int deleteBannedId = R.id.contacts_button_deleteBanned;
	private final int syncContactsId = R.id.contacts_button_syncContacts;
	
	//Ids for option menu
	final int aboutMenuId = R.id.menu_information_about;
	final int helpMenuId = R.id.menu_information_help;

	//IDs dialogs
	private WarningDialog synchronizeDialog;	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.contactstab, container, false);
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {			
		
		super.onCreate(savedInstanceState);																								
						
		//Instanciate all buttons
		Button addBannedButton = (Button)getSherlockActivity().findViewById(addBannedId);
		Button deleteBannedButton = (Button)getSherlockActivity().findViewById(deleteBannedId);			
		Button syncContactsButton = (Button)getSherlockActivity().findViewById(syncContactsId);
								
		//Define the buttons listener
		addBannedButton.setOnClickListener(this);
		deleteBannedButton.setOnClickListener(this);			
		syncContactsButton.setOnClickListener(this);
		
		synchronizeDialog = new WarningDialog(this, ConfigAppValues.WARNING_SYNC_CONTACTS);															
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
			case syncContactsId:
				showDialog(SYNCHRONIZE_DIALOG);
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
			case SYNCHRONIZE_DIALOG:				
				dialog = synchronizeDialog.getAlertDialog();
				break;
			case ABOUT_DIALOG:				
				dialog = showWebviewDialog(IDialogs.ABOUT_URI);
				break;	
			case HELP_DIALOG:
				dialog = showWebviewDialog(IDialogs.HELP_CONTACT_URI);
				break;		
			default:
				dialog = null;
		}
		
		return dialog;	
	}
	
	/**
	 * Create the webview dialog using the file (uri) specified to show the information.
	 * 
	 * @return
	 */
	public Dialog showWebviewDialog(String uri) {
		
		try {
			  View contentView = getLayoutInflater().inflate(R.layout.webview_dialog, null, false);
              WebView webView = (WebView) contentView.findViewById(R.id.webview_content);
              webView.getSettings().setJavaScriptEnabled(true);              
              
              webView.loadUrl(uri);

              return new AlertDialog.Builder(this)
                  .setCustomTitle(null)
                  .setPositiveButton(android.R.string.ok, null)
                  .setView(contentView)
                  .create();
              
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return null;
		}
	}			
		
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
										
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.informationmenu, menu);
		
		return true;					
	}
	
	/**
	 * @param 		item
	 * @return 		boolean
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {				
			
		switch (item.getItemId()) {
			case aboutMenuId:				
				showDialog(ABOUT_DIALOG);
				break;
			case helpMenuId:
				showDialog(HELP_DIALOG);
				break;
			default:
				break;
		}
		return false;					
	}
	
									
}
