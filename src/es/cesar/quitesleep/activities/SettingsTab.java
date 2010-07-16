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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Settings;
import es.cesar.quitesleep.notifications.QuiteSleepNotification;
import es.cesar.quitesleep.operations.StartStopServicesOperations;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.subactivities.About;
import es.cesar.quitesleep.subactivities.Help;
import es.cesar.quitesleep.subactivities.MailSettings;
import es.cesar.quitesleep.subactivities.SmsSettings;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;
import es.cesar.quitesleep.utils.QSToast;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@mgail.com
 *
 */
public class SettingsTab extends Activity implements OnClickListener {
	
	private final String CLASS_NAME = getClass().getName();
	
	//Ids for the button widgets	
	final int mailButtonId = R.id.settings_button_mail;
	final int smsButtonId = R.id.settings_button_sms;
	final int serviceToggleButtonId = R.id.settings_togglebutton_service;	
	
	//Ids for option menu
	final int aboutMenuId = R.id.menu_information_about;
	final int helpMenuId = R.id.menu_information_help;
	
	//Activity buttons
	private Button mailButton;
	private Button smsButton; 
	private ToggleButton serviceToggleButton;
	
	//Notification
	//private NotificationManager notificationManager;
	//final private int notificationId = R.layout.quitesleep_notification;
	
	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingstab);

		//All Activity buttons
		mailButton = (Button)findViewById(mailButtonId);
		smsButton = (Button)findViewById(smsButtonId);
		serviceToggleButton = (ToggleButton)findViewById(serviceToggleButtonId);
		
			
		//Define all button listeners
		mailButton.setOnClickListener(this);
		smsButton.setOnClickListener(this);
		serviceToggleButton.setOnClickListener(this);
		
		//Get the notification manager service
		//notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		/* Set the previous saved state in the ddbb in the widgets that need
		 * the saved state, such as toggle button.
		 */
		setDefaultSavedData();
	}
	
	/**
	 * Listener for all buttons in this Activity
	 */
	public void onClick (View view) {
		
		int viewId = view.getId();
				
		switch (viewId) {
		
			case mailButtonId:
				Intent intentMailSettings = new Intent(this, MailSettings.class);
				startActivityForResult(intentMailSettings, ConfigAppValues.REQCODE_MAIL_SETTINGS);
				break;
				
			case smsButtonId:
				Intent intentSmsSettings = new Intent(this, SmsSettings.class);
				startActivityForResult(intentSmsSettings, ConfigAppValues.REQCODE_SMS_SETTINGS);
				break;
				
			case serviceToggleButtonId:
				startStopServiceProcess();					
				QuiteSleepNotification.showNotification(
						this, 
						serviceToggleButton.isChecked());			
				break;
				
			default:
				break;
		}						
	}
	
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		
		try {							
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.informationmenu, menu);
			
			return true;
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
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
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;			
		}
	}
	
	/**
	 * Put the ddbb saved data in the activity widgets if someone need to
	 * chage this state using a previous save data.
	 */
	private void setDefaultSavedData () {
		
		try {
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();
			if (settings != null) {
				serviceToggleButton.setChecked(settings.isQuiteSleepServiceState());
			}else
				serviceToggleButton.setChecked(false);
			
			clientDDBB.close();						
						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
		}
	}
				
	
	
	/**
	 * Function that depends the serviceToggleButton state try to start the
	 * QuiteSleep incoming call service or stop it.
	 */
	private void startStopServiceProcess () {
		
		try {
			
			boolean result = StartStopServicesOperations.startStopQuiteSleepService(
					serviceToggleButton.isChecked());
			
			if (serviceToggleButton.isChecked()) {			
				
				/* Deactivate the notification toast because now use the
				 * status bar notification 
				 */
				/*
				if (result)
				
					//All right, start the service was ok!
					Toast.makeText(
	                		this,
	                		this.getString(
	                				R.string.settings_toast_start_service),
	                		Toast.LENGTH_SHORT).show();
	                
				else
					//An error has ocurred!!
					Toast.makeText(
	                		this,
	                		this.getString(
	                				R.string.settings_toast_fail_service),
	                		Toast.LENGTH_SHORT).show();			
	           */								
			}else {																												
				if (result)
					//All right, stop the service was ok!
					if (QSToast.RELEASE) QSToast.r(
	                		this,
	                		this.getString(
	                				R.string.settings_toast_stop_service),
	                		Toast.LENGTH_SHORT);
				else
					//An error has ocurred!!
					if (QSToast.RELEASE) QSToast.r(
	                		this,
	                		this.getString(
	                				R.string.settings_toast_fail_service),
	                		Toast.LENGTH_SHORT);								
			}							
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}
	
	
	/*
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);				
	}
	*/
	
	

}

