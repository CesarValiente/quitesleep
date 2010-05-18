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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Settings;
import es.cesar.quitesleep.listeners.MyPhoneStateListener;
import es.cesar.quitesleep.notifications.QuiteSleepNotification;
import es.cesar.quitesleep.operations.CallFilter;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class PhoneStateReceiver extends BroadcastReceiver {
	
	private final String CLASS_NAME = getClass().getName();
	
	//Constants for all broadcastreceiver types used in the app
	private final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
	private final String PHONE_STATE = "android.intent.action.PHONE_STATE";
	private final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	
	
	//Other constants used
	private final String PDUS = "pdus";	
	
	
	
	
	@Override
	public void onReceive (Context context, Intent intent) {
				
		Log.d(CLASS_NAME, "BroadcastReceive. Tipo: " + intent.getAction());
		
		//-------------		LISTEN FOR BOOT COMPLETED		------------------//
		if (intent.getAction().equals(BOOT_COMPLETED)) {
			listenBootCompleted(context);
		}		
		
		//----------		LISTEN FOR INCOMING CALLS		------------------//		
		else if (intent.getAction().equals(PHONE_STATE)) {
			listenIncomingCalls(context);
			
		}
		
		//-----------		LISTEN FOR INCOMING SMS		----------------------//
		else if (intent.getAction().equals(SMS_RECEIVED)) {
			listenIncomingSMS(context, intent);
		}
			
	}
	
	
	/**
	 * Listen for BOOT_COMPLETED event and show the QuiteSleep status bar 
	 * notification if the QuiteSleep service is previously configured to start
	 * Also initialize things like ConfigAppValues.setContext()
	 * 
	 * @param context
	 */
	private void listenBootCompleted (Context context) {
		
		try {
			Log.d(CLASS_NAME, "en Boot_Completed!!");
			
			if (ConfigAppValues.getContext() == null)
				ConfigAppValues.setContext(context);									
			
			if (ConfigAppValues.getQuiteSleepServiceState() != null) {
							
				if (ConfigAppValues.getQuiteSleepServiceState()) {
					Log.d(CLASS_NAME, "Show notification x ConfigAppValues: " + ConfigAppValues.getQuiteSleepServiceState());
					QuiteSleepNotification.showNotification(context, true);
				}
			}
			else {
				
				ClientDDBB clientDDBB = new ClientDDBB();
				Settings settings = clientDDBB.getSelects().selectSettings();
				if (settings != null) {
					ConfigAppValues.setQuiteSleepServiceState(settings.isQuiteSleepServiceState());
					if (settings.isQuiteSleepServiceState()) {
						Log.d(CLASS_NAME, "Show notification x Settings: " + settings.isQuiteSleepServiceState());
						QuiteSleepNotification.showNotification(context, true);
					}
				}
				else {
					ConfigAppValues.setQuiteSleepServiceState(false);
					settings = new Settings(false);
					clientDDBB.getInserts().insertSettings(settings);
					clientDDBB.commit();
				}
				clientDDBB.close();
			}
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	
	/**
	 * Listen for incoming calls and use the listener for.
	 * @param context
	 */
	private void listenIncomingCalls (Context context) { 
		
		try {							
			
			MyPhoneStateListener phoneListener = new MyPhoneStateListener(context);
			TelephonyManager telephony = 
				(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
			
			
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}		
	}
	
	
	/**
	 * Listen the incoming SMS.
	 * 
	 * @param context
	 * @param intent
	 */
	private void listenIncomingSMS (Context context, Intent intent) {
		
		try {
			
			Log.d(CLASS_NAME, "SMS RECEIVED!!!!!");
			
			SmsManager sms = SmsManager.getDefault();
			
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[])bundle.get(PDUS);
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i=0; i<pdus.length; i++) 
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				
				for (SmsMessage message:messages) {
					String messageText = message.getMessageBody();
					String phoneSender = message.getOriginatingAddress();
					
					Log.d(CLASS_NAME, "SMS Msg: " + messageText);
					Log.d(CLASS_NAME, "SMS From: " + phoneSender);										
					
					if (CallFilter.ringerMode() != AudioManager.RINGER_MODE_SILENT)  
						CallFilter.silentIncomingCall(phoneSender);
					
					
					/*
					if (msg.toLowerCase().startsWith(QUERY_STRING)) {
						String out = msg.substring(QUERY_STRING.length());
						sms.sendTextMessage(to, null, out, null, null);
					}
					*/
				}
			}
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}
}
