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

package es.cesar.quitesleep.listeners;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import es.cesar.quitesleep.operations.CallFilter;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class MyPhoneStateListener extends PhoneStateListener {
	
	private final String CLASS_NAME = getClass().getName();
		
	
	/**
	 * Constructor with the context parameter for use if the application
	 * not run and is the BOOT_COMPLETED BroadcastReceiver that is launch
	 * this.
	 * @param context
	 */
	public MyPhoneStateListener (Context context) {
		
		super();
		if (ConfigAppValues.getContext() == null)
			ConfigAppValues.setContext(context);			
	}
	
	
	/**
	 * Function that receive the state id for the phone state type 
	 * (idle, offhook and ringing), and the incoming number that is doing
	 * the call.
	 */
	public void onCallStateChanged (int state, String incomingNumber) {
		
		try {														
			switch (state) {					
			
				//-------------		CALL_STATE_IDLE		----------------------//
				case TelephonyManager.CALL_STATE_IDLE:					
																
					processCallStateIdle();												
					break;
				
				//-----------------		CALL_STATE_OFFHOOK		--------------//
				case TelephonyManager.CALL_STATE_OFFHOOK:
					
					processCallStateOffhook();
					break;
				
				//-----------------		CALL_STATE_RINGING		--------------//
				case TelephonyManager.CALL_STATE_RINGING:
							
					processCallStateRinging(incomingNumber);
					break;
					
				default:
					break;
			}
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}		
	
	
	/**
	 * Proces the CALL_STATE_IDLE signal from PhoneReceiver
	 */
	private void processCallStateIdle () {
		
		try {
			Log.d(CLASS_NAME, "IDLE");
			
			if (CallFilter.ringerMode() == AudioManager.RINGER_MODE_SILENT && 
					CallFilter.checkQuiteSleepServiceState()) {
				
				/* Put one pause of 1 second for wait before put the
				 * ringer mode  to normal again
				 */						
				Thread.sleep(1000);
				
				CallFilter.putRingerModeNormal();		
				//CallFilter.vibrateOn();
			
				Toast.makeText(
						ConfigAppValues.getContext().getApplicationContext(),
						"IDLE!!!!!!!!",
						Toast.LENGTH_SHORT).show();
			}					
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(e.toString(), e.getStackTrace()));
			
		}
	}
	
	/**
	 * Proces the CALL_STATE_OFFHOOK signal from PhoneReceiver
	 */
	private void processCallStateOffhook () {
		
		try {
			Log.d(CLASS_NAME, "OFFHOOK");
			Toast.makeText(
            		ConfigAppValues.getContext().getApplicationContext(),
            		"OFFHOOK!!!!!!!!",
            		Toast.LENGTH_SHORT).show();	
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	
	/**
	 *	Proces the CALL_STATE_RINGING signal from PhoneReceiver 
	 */
	private void processCallStateRinging (String incomingNumber) {
		
		try {
			Log.d(CLASS_NAME, "RINGING");					
			
			/* Put the device in silent mode if the incoming number is
			 * from contact banned and in schedule interval 
			 */					
			if (CallFilter.ringerMode() != AudioManager.RINGER_MODE_SILENT)  {
				
				CallFilter.silentIncomingCall(incomingNumber);					
										
				Toast.makeText(
						ConfigAppValues.getContext().getApplicationContext(),
						"RINGING!!!!!!!!",
						Toast.LENGTH_SHORT).show();															
			}														
	
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}

}
