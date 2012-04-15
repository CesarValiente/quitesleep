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
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.dialogs.WarningDialog;
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
public class Main extends BaseSherlockActivity {
	
	final String CLASS_NAME = getClass().getName();
	
	private FragmentPagerAdapter mAdatper;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	
	final private int FIRST_TIME_DIALOG 	=	1;	 
     final private int ABOUT_DIALOG                  = 2; 
     final private int HELP_DIALOG                   = 3;    
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
		
		if (isTheFirstTime()) {
			//We instantiate firstTimeDialog 
			//firstTimeDialog = new WarningDialog(this, ConfigAppValues.WARNING_FIRST_TIME);				
			//showDialog(FIRST_TIME_DIALOG);
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
			case FIRST_TIME_DIALOG:
				//dialog = firstTimeDialog.getAlertDialog();
				break;
			default:
				dialog = null;
		}
		
		//return dialog;
		return null;
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
