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

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.components.dialogs.WarningDialog;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.Settings;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.operations.SmsOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class SmsSettings extends BaseActivity implements OnClickListener {
	
	final private String CLASS_NAME = getClass().getName();
	final private int WARNING_DIALOG = 0;	
	
	//Ids for widgets
	final int smsEditTextId = R.id.smssettings_edittext_savesms;
	final int saveSmsButtonId = R.id.smssettings_button_savesms;	
	final int smsServiceToggleButtonId = R.id.smssettings_togglebutton_smsservice;	
	
	//Widgets
	private EditText smsEditText;
	private Button saveSmsButton;
	private ToggleButton smsServiceToggleButton;	
	private WarningDialog warningDialog;
	
	
	public void onCreate (Bundle savedInstanceState) {				
					
		super.onCreate(savedInstanceState);
		super.homeToUp(true);
				
		setContentView(R.layout.smssettings);
					
		smsEditText = (EditText)findViewById(smsEditTextId);
		saveSmsButton = (Button)findViewById(saveSmsButtonId);
		smsServiceToggleButton = (ToggleButton)findViewById(smsServiceToggleButtonId);		
		
		saveSmsButton.setOnClickListener(this);
		smsServiceToggleButton.setOnClickListener(this);			
		
		warningDialog = new WarningDialog(
				this, 
				ConfigAppValues.WARNING_SMS_ACTION);				
		
		//Put in the widgets the prevoious data saved into ddbb.
		getDefaultValues();				
	}
	
		
	@Override
	public void onClick (View view) {
		
		int viewId = view.getId();
				
		switch (viewId) {
		
			case saveSmsButtonId:				
				prepareSaveSmsOperation();
				break;
				
			case smsServiceToggleButtonId:
				if (smsServiceToggleButton.isChecked()) 					
					showDialog(WARNING_DIALOG);				
				else 					
					DialogOperations.checkSmsService(
							this, 
							smsServiceToggleButton.isChecked());												
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
				warningDialog.setToggleButtonIsChecked(smsServiceToggleButton.isChecked());
				break;
				
			default:
				break;
		}								
	}
	
	/**
	 * Put the default values saved in the ddbb in the widgets
	 */
	private void getDefaultValues () {
		
		try {
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();
			
			if (settings != null) {
				if (settings.getSmsText()!= null && !settings.getSmsText().equals(""))
					smsEditText.setText(settings.getSmsText());
				
				smsServiceToggleButton.setChecked(settings.isSmsService());
				
			}else {
				settings = new Settings(false);
				clientDDBB.getInserts().insertSettings(settings);
				
				/* Save the sms text in the settings if the Settings object haven't
				 * been created, so the predefined text will be, for the moment,
				 * the sms text in the settings object
				 */
				settings.setSmsText(smsEditText.getText().toString());
				clientDDBB.commit();
			}
			
			clientDDBB.close();
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	/**
	 * Function that prepare the data for save into ddbb and call to the function
	 * that does the operation.
	 */
	private void prepareSaveSmsOperation () {
		
		try {
			
			String smsText = smsEditText.getText().toString();						
			
			if (SmsOperations.saveSmsSettings(smsText))
				es.cesar.quitesleep.utils.Toast.r(
                		this,
                		this.getString(
                				R.string.smssettings_toast_save),
                		Toast.LENGTH_SHORT);
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	

}
