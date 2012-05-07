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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.SlidingDrawer;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.components.listeners.ContactDialogListener;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.activities.base.BaseFragmentActivity;
import es.cesar.quitesleep.ui.dialogs.fragments.ContactsFragmentDialog;
import es.cesar.quitesleep.ui.fragments.adapter.PageViewerHelpAdapter;
import es.cesar.quitesleep.ui.fragments.adapter.PageViewerTabsAdapter;
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
public class Main extends BaseFragmentActivity implements ContactDialogListener {
	
	final String CLASS_NAME = getClass().getName();
	
	private FragmentPagerAdapter mAdatper;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	
	private SlidingDrawer mSlidingDrawer;
	private FragmentPagerAdapter mpHelpAdapter;
	private ViewPager mHelpPager;
	private PageIndicator mHelpIndicator;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {																		
		
		super.onCreate(savedInstanceState);				
		
		setContentView(R.layout.main);				
		
		//Stuff created to navegate through the different
		//options in the app
		mAdatper = new PageViewerTabsAdapter(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdatper);
		mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		
		//Stuff created to navegate through the different help and about me info
		mSlidingDrawer = (SlidingDrawer) findViewById(R.id.sliding_drawer);
		mpHelpAdapter = new PageViewerHelpAdapter(getSupportFragmentManager());				
		mHelpPager = (ViewPager)findViewById(R.id.help_pager);
		mHelpPager.setAdapter(mpHelpAdapter);					
		mHelpIndicator = (CirclePageIndicator)findViewById(R.id.help_indicator);
		mHelpIndicator.setViewPager(mHelpPager);			
				
		//If is the first time QuiteSleep is running, then performs sync operations.		
		if (isTheFirstTime()) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();					
			SherlockDialogFragment dialog = ContactsFragmentDialog.newInstance(
					this, ConfigAppValues.DialogType.SYNC_FIRST_TIME);
			dialog.show(ft, "dialog");							
		}											
	}	
	
	@Override
	public void onBackPressed () {
		
		//If we have the slidingDrawer open, we close it, if not the normal
		//behavior of the back button
		if (mSlidingDrawer.isOpened())
			mSlidingDrawer.close();
		else
			super.onBackPressed();				
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


	@Override
	public void clickYes() {
		DialogOperations.synchronizeFirstTime(this, handler);
	}
	
}
