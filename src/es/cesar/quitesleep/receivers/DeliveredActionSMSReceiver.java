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

import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 * 
 * Class that implements the sms delivery action for listen the send action message 
 *
 */
public class DeliveredActionSMSReceiver extends BroadcastReceiver {
	
	private String CLASS_NAME = getClass().getName();
	
	final String DELIVERED_SMS_ACTION 		= 	"DELIVERED_SMS_ACTION";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(CLASS_NAME, "The user have been receive the SMS message!!");
		showNotificationToast("The user have been receive the SMS message!!");
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
