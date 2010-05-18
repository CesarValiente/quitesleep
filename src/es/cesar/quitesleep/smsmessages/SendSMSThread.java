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

package es.cesar.quitesleep.smsmessages;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Phone;
import es.cesar.quitesleep.ddbb.Settings;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;


/**
 * 
 * @author 	Cesar Valiente Gordo
 * @mail 	cesar.valiente@gmail.com
 * 
 * Class that implements Java Thread for send sms to the incoming caller
 *
 */
public class SendSMSThread extends Thread {
	
	private final String CLASS_NAME = getClass().getName();
	
	private String smsText;
	private String phoneReceiver;		
	
	//--------------		Getters & Setters		--------------------------//
	public String getSmsText() {
		return smsText;
	}
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	
	public String getPhoneReceiver() {
		return phoneReceiver;
	}
	public void setPhoneReceiver(String phoneReceiver) {
		this.phoneReceiver = phoneReceiver;
	}
	//------------------------------------------------------------------------//
	
	
	/**
	 * Constructor with the phonenumber of the receiver sms
	 */
	public SendSMSThread (String receiver) {
		init(receiver);
	}
	

	/**
	 * Function that is called for the constructor
	 * @param receiver
	 */
	public void init (String receiver) {
		
		this.phoneReceiver = receiver;	
		getAllData();
	}
			
	
	/**
	 * Get all data of SMS settings
	 */
	private void getAllData () {
		
		try {
			ClientDDBB clientDDBB = new ClientDDBB();
			Settings settings = clientDDBB.getSelects().selectSettings();
			
			if (settings != null)  				
				smsText = settings.getSmsText();
										
			clientDDBB.close();
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Check if the receiver phone number have permission to use for send SMS
	 * message
	 * 
	 * @return		true or false if the phone number (receiver) can or not receive
	 * messages
	 */
	private  boolean checkSendPhoneNumber () {
		
		try {
			ClientDDBB clientDDBB = new ClientDDBB();
			Phone phone = clientDDBB.getSelects().selectPhoneForPhoneNumber(phoneReceiver);
			clientDDBB.close();
			
			if (phone != null) 			
				return phone.isUsedToSend();
			else
				return false;										
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
		
	@Override
	public void run () {
		sendSms();
	}
	
	
	/**
	 * Send one SMS message to the receiver
	 * 
	 */
	public void sendSms () {
		
		try {			
			if (checkSendPhoneNumber()) {
				
				
				final String SENT_SMS_ACTION 			= 	"SENT_SMS_ACTION";
				final String DELIVERED_SMS_ACTION 		= 	"DELIVERED_SMS_ACTION";
				
				//Create the setIntent parameter
				Intent sentIntent = new Intent(SENT_SMS_ACTION);
				PendingIntent sentPI = PendingIntent.getBroadcast(
						ConfigAppValues.getContext(),
						0,
						sentIntent,
						0);
				
				//Create the deliveryIntetn parameter
				Intent deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
				PendingIntent deliverPI = PendingIntent.getBroadcast(
						ConfigAppValues.getContext(),
						0,
						deliveryIntent,
						0);							
				
				SmsManager smsManager = SmsManager.getDefault();																									
				
				
				Log.d(CLASS_NAME, "SmsText: " + smsText);
				//String test = "5556";
				smsManager.sendTextMessage(
						phoneReceiver, 
						null, 
						smsText, 
						sentPI, 
						deliverPI);							
			}			
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}
	
	
	
	
	

}
