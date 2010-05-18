/* 
 	Copyright 2010 Cesar Valiente Gordo
 
 	This file is part of QuiteSleep.

    QuiteSleep is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License, or
    (at your option) any later version.

    QuiteSleep is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with QuiteSleep.  If not, see <http://www.gnu.org/licenses/>.
*/

package es.cesar.quitesleep.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;


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
				showNotificationToast("Successful transmission!!");
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				Log.d(CLASS_NAME, "Nonspecific Failure!!");
				showNotificationToast("Nonspecific Failure!!");
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				Log.d(CLASS_NAME, "Radio is turned Off!!");
				showNotificationToast("Nonspecific Failure!!");
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				Log.d(CLASS_NAME, "PDU Failure");
				showNotificationToast("PDU Failure");
				break;
		}					
	}

	
	/**
	 * Show the notification toast
	 * 
	 * @param message
	 */
	private void showNotificationToast (String message) {
		
		try {			
			//Show the toast message
			Toast.makeText(
            		ConfigAppValues.getContext(),
            		message,
            		Toast.LENGTH_SHORT).show();	
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}

}
