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


import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.components.dialogs.WarningDialog;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.fragments.ContactsFragment;
import es.cesar.quitesleep.ui.fragments.LogsFragment;
import es.cesar.quitesleep.ui.fragments.ScheduleFragment;
import es.cesar.quitesleep.ui.fragments.SettingsFragment;
import es.cesar.quitesleep.ui.fragments.adapter.PageViewerAdapter;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author		Cesar Valiente Gordo
 * @mail		cesar.valiente@gmail.com	
 * 
 * @version 1.0, 02-21-2010
 * 
 * Main class for start QuiteSleep App, this class implement the tabs wigets
 * to show them.
 * 
 */
public class Main extends SherlockFragmentActivity {
	
	final String CLASS_NAME = getClass().getName();
	
	private FragmentPagerAdapter mAdatper;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	
	final private int FIRST_TIME_DIALOG 	=	1;		
	private WarningDialog firstTimeDialog;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {																		
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		mAdatper = new PageViewerAdapter(getSupportFragmentManager());
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdatper);
		
		mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		
				
		//If is the first time QuiteSleep is running, then performs sync operations.
		/*
		if (isTheFirstTime()) {
			//We instantiate firstTimeDialog 
			firstTimeDialog = new WarningDialog(this, ConfigAppValues.WARNING_FIRST_TIME);				
			showDialog(FIRST_TIME_DIALOG);
		}
		*/							
		
		
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
			case FIRST_TIME_DIALOG:
				dialog = firstTimeDialog.getAlertDialog();
				break;
			default:
				dialog = null;
		}
		
		return dialog;	
	}
	
	/**
	 * This function prepares the dialog with the passed parameters.
	 */
	protected void onPrepareDialog (int idDialog, Dialog dialog) {
		
		try {		
			switch (idDialog) {
				case FIRST_TIME_DIALOG:
					firstTimeDialog.setContext(this);					
					firstTimeDialog.setHandler(handler);										
					break;
					
				default:
					break;
			}						
		}catch (Exception e) {
			Log.d(CLASS_NAME, ExceptionUtils.getString(e));			
		}
	}
	
	/**
	 * This funcion check if the db4o database is full contacts empty, so indicate
	 * that is the first time too run the application.
	 * 
	 * @return				True or false if the db4o is contact empty or not
	 * @see					boolean
	 */
	private boolean isTheFirstTime () {
		
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
		
			int numContacts = clientDDBB.getSelects().getNumberOfContacts();
			clientDDBB.close();						
			
			if (numContacts == 0)
				return true;
			else 
				return false;
								
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			return false;
		}
	}	
	
	/**
	 * Create the Contacts Tab
	 * 
	 * @param intent
	 * @param tabSpec
	 * @param tabHost
	 * @param resources
	 * @throws Exception
	 */
	private void createContactsTab (
			Intent intent,
			TabSpec tabSpec, 
			TabHost tabHost, 
			Resources resources) throws Exception {

		try {
		
			//---------------		Contacts Tab		----------------------//
			
			//Create an intent to launch an Activiy for the tab (to be reused)
			intent = new Intent().setClass(this, ContactsFragment.class);
			
			//Get the string resource from the string xml set constants
			CharSequence contactsLabel = getString(R.string.contacts_tab_label);
			
			//Initialize a TabSpec for each tab and add it to the TabHost. Also
			//we add the intent to the tab for when it push, use the intent added
			//(go to the clock tab)
			tabSpec = tabHost.newTabSpec(contactsLabel.toString()).setIndicator(
					contactsLabel,
					resources.getDrawable(R.drawable.contactstab)).setContent(intent);			
					
			//Add the TabSpec to the TabHost
			tabHost.addTab(tabSpec);

			
		}catch (Exception e) {
			e.printStackTrace();
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Exception(e.toString());
		}
					
	}
	
	/**
	 * Create the Schedule Tab
	 * 
	 * @param intent
	 * @param tabSpec
	 * @param tabHost
	 * @param resources
	 * @throws Exception
	 */
	private void createScheduleTab (
			Intent intent,
			TabSpec tabSpec, 
			TabHost tabHost, 
			Resources resources) throws Exception {
		
		try {
			
			//------------		Schedule Tab	--------------------------//
			
			intent = new Intent().setClass(this, ScheduleFragment.class);
			
			CharSequence scheduleLabel = getString(R.string.schedule_tab_label);

			tabSpec = tabHost.newTabSpec(scheduleLabel.toString()).setIndicator(
					scheduleLabel,
					resources.getDrawable(R.drawable.scheduletab)).setContent(intent);
			
			tabHost.addTab(tabSpec);

		}catch (Exception e) {			
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Exception(e.toString());
		}
	}	
	
	
	/**
	 * 
	 * @param intent
	 * @param tabSpec
	 * @param tabHost
	 * @param resources
	 * @throws Exception
	 */
	private void createSettingsTab (
			Intent intent,
			TabSpec tabSpec, 
			TabHost tabHost, 
			Resources resources) throws Exception {
		
		try {
			
			//------------		Settings Tab	--------------------------//
			
			intent = new Intent().setClass(this, SettingsFragment.class);
			
			CharSequence messageLabel = getString(R.string.settings_tab_label);

			tabSpec = tabHost.newTabSpec(messageLabel.toString()).setIndicator(
					messageLabel,
					resources.getDrawable(R.drawable.settingstab)).setContent(intent);
			
			tabHost.addTab(tabSpec);

		}catch (Exception e) {			
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Exception(e.toString());
		}
	}	
	
	/**
	 * 
	 * @param intent
	 * @param tabSpec
	 * @param tabHost
	 * @param resources
	 * @throws Exception
	 */
	private void createLogsTab (
			Intent intent,
			TabSpec tabSpec, 
			TabHost tabHost, 
			Resources resources) throws Exception {
		
		try {
			
			//------------		Logs Tab	--------------------------//
			
			intent = new Intent().setClass(this, LogsFragment.class);
			
			CharSequence messageLabel = getString(R.string.logs_tab_label);

			tabSpec = tabHost.newTabSpec(messageLabel.toString()).setIndicator(
					messageLabel,
					resources.getDrawable(R.drawable.logstab)).setContent(intent);
			
			tabHost.addTab(tabSpec);

		}catch (Exception e) {			
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Exception(e.toString());
		}
	}	
	

	/**
	 * This handler manages the action regarding to check if the user click yes 
	 * over the confirm action that realize the first database synchronization
	 * or not.
	 */
	public final Handler handler = new Handler() {
		public void handleMessage(Message message) {
											
			final String NUM_CONTACTS = "NUM_CONTACTS";
			
			int numContacts = message.getData().getInt(NUM_CONTACTS);
			
			Log.d(CLASS_NAME, "Num contacts sync 1st time: " + numContacts);								
		}
	};

}
