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
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockDialogFragment;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.Settings;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.operations.SmsOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.dialogs.fragments.SmsEmailFragmentDialog;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class SmsSettings extends BaseSherlockActivity implements OnClickListener {
	
	final private String CLASS_NAME = getClass().getName();	
	
	//Ids for widgets
	final int smsEditTextId = R.id.smssettings_edittext_savesms;
	final int saveSmsButtonId = R.id.smssettings_button_savesms;	
	final int smsServiceToggleButtonId = R.id.smssettings_togglebutton_smsservice;	
	
	//Widgets
	private EditText smsEditText;
	private Button saveSmsButton;
	private ToggleButton smsServiceToggleButton;		
	
	
	public void onCreate (Bundle savedInstanceState) {				
					
		super.onCreate(savedInstanceState);
		super.homeToUp(true);
				
		setContentView(R.layout.smssettings);
					
		smsEditText = (EditText)findViewById(smsEditTextId);
		saveSmsButton = (Button)findViewById(saveSmsButtonId);
		smsServiceToggleButton = (ToggleButton)findViewById(smsServiceToggleButtonId);		
		
		saveSmsButton.setOnClickListener(this);
		smsServiceToggleButton.setOnClickListener(this);			
		
		
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
				if (smsServiceToggleButton.isChecked()) { 														
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();						
					SherlockDialogFragment dialog = SmsEmailFragmentDialog.newInstance(ConfigAppValues.DialogType.SMS_DIALOG);
					dialog.show(ft, "dialog");
				}else 					
					DialogOperations.checkSmsService(
							this, 
							smsServiceToggleButton.isChecked());												
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
