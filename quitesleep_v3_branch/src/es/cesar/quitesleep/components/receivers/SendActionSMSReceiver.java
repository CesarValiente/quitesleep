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

package es.cesar.quitesleep.components.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.settings.ConfigAppValues;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 * 
 * Class that implements the sms receiver action for listen the send action message  
 *
 */
public class SendActionSMSReceiver extends BroadcastReceiver{
			
	private String CLASS_NAME 				= 		getClass().getName();
	
	final String SENT_SMS_ACTION 			= 		"SENT_SMS_ACTION";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {

		switch (getResultCode()) {
			case Activity.RESULT_OK:
				Log.d(CLASS_NAME, "Successful transmission!!");
				es.cesar.quitesleep.utils.Toast.d(
						QuiteSleepApp.getContext(),
	            		"Successful transmission!!",
	            		Toast.LENGTH_SHORT);
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:				
				Log.d(CLASS_NAME, "Nonspecific Failure!! Intent extra: " 
						+ intent.getExtras().getInt("errorCode"));
				es.cesar.quitesleep.utils.Toast.d(
						QuiteSleepApp.getContext(),
	            		"Nonspecific Failure!!",
	            		Toast.LENGTH_SHORT);
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				Log.d(CLASS_NAME, "Radio is turned Off!!");
				es.cesar.quitesleep.utils.Toast.d(
						QuiteSleepApp.getContext(),
	            		"Radio is turned Off!!",
	            		Toast.LENGTH_SHORT);
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				Log.d(CLASS_NAME, "PDU Failure");
				es.cesar.quitesleep.utils.Toast.d(
						QuiteSleepApp.getContext(),
	            		"PDU Failure",
	            		Toast.LENGTH_SHORT);
				break;
		}					
	}		
}
