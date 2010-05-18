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

package es.cesar.quitesleep.operations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.util.Log;
import es.cesar.quitesleep.ddbb.CallLog;
import es.cesar.quitesleep.ddbb.ClientDDBB;
import es.cesar.quitesleep.ddbb.Contact;
import es.cesar.quitesleep.ddbb.Schedule;
import es.cesar.quitesleep.ddbb.Settings;
import es.cesar.quitesleep.mailmessages.SendMail;
import es.cesar.quitesleep.smsmessages.SendSMSThread;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.TokenizerUtils;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class CallFilter {
	
	private static final String CLASS_NAME = "es.cesar.quitesleep.utils.operations.CallFilter"; 
		
	
	/**
	 * Function that check if the incoming call number is from a banned contact,
	 * if is true, put the mobile to silent mode (sound and vibrate), if is
	 * false do nothing.
	 * 
	 * UPDATE 05-05-2010: now, we puts always the phone in silent mode when incoming
	 * call, but, then, quitesleep check if the contact is banned and if is in the
	 * schedule time, if is true, creates a new CallLog object, and we don't nothing
	 * with the phone mode, so it was put in silent mode at beginning.
	 * But if the incoming phone not is from a banned contact and/or if not
	 * is in the schedule time, put the mobile phone in normal mode.
	 * I have done this, because, is the only manner i get, that the phone never ring
	 * one sec when an incoming call from a banned contact arrives and the only way
	 * to put the mobile phone with vibrator off, one time the mobile phone is in normal
	 * mode the ring is too on.
	 * The inconvenience is that whatever incoming call the first tone the mobile 
	 * phone silence it, not is the perfect way but is the only and the best way 
	 * that i have reached with >Android 2.0 at day.  
	 * 
	 * @param 		incomingNumber
	 */
	public static void silentIncomingCall (String incomingNumber) {
		
		try {
			
			//Put the mobile phone in silent mode (sound+vibration)
			putRingerModeSilent();
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			String phoneNumberWhithoutDashes = 
				TokenizerUtils.tokenizerPhoneNumber(incomingNumber, null);
			
			Contact contactBanned = 
				clientDDBB.getSelects().selectBannedContactForPhoneNumber(
						phoneNumberWhithoutDashes);								
			
			//If the contact is in the banned list
			if (contactBanned != null) {
				
				Log.d(CLASS_NAME, "Contact: " + contactBanned.getContactName() + 
						"\t isBanned: " + contactBanned.isBanned());								
				
				//create the CallLog object for log calls.
				CallLog callLog = new CallLog();
				
				//check if the call is in the interval time
				boolean isInInterval = checkScheduleAndServiceState(callLog);
				
				if (isInInterval) {
										
					/* Check if the mail service is running, if it is true
					 * create a SendMail object for try to send one or more 
					 * email to the contact with the incoming number
					 */
					sendMail(incomingNumber, callLog);
					
					/* Check if the sms service is running, if it is true
					 * create a SendSMS object for try to send a SMS to 
					 * the contact with the incoming number
					 */
					sendSMS(incomingNumber, callLog);																		
					
					//get the nomOrder for the new CallLog
					int numOrder = clientDDBB.getSelects().countCallLog();				
					Log.d(CLASS_NAME, "CallLog numOrder: " + numOrder);
					
					//Set the parameters and save it
					callLog.setPhoneNumber(phoneNumberWhithoutDashes);
					callLog.setContact(contactBanned);
					callLog.setNumOrder(numOrder+1);
					
					clientDDBB.getInserts().insertCallLog(callLog);
					clientDDBB.commit();
					clientDDBB.close();
					
				}
				//If the call isn't in the interval time
				else {
					putRingerModeNormal();
					clientDDBB.close();
				}							
			}
			//If the incoming call number isn't of the any banned contact
			else {
				Log.d(CLASS_NAME, "ContactBanned == NULL!!!!");
				putRingerModeNormal();
				clientDDBB.close();
			}
																											
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
				
		}
	}
	

	/**
	 * Check if the Settings object is created in the ddbb. If it's created
	 * check this attribute serviceState for check if the service is up
	 * or is down.
	 * 
	 * @return		true if the service is running or false if not.
	 */
	public static boolean checkQuiteSleepServiceState () {
		
		try {
						 			
			boolean serviceState = false;
			
			if (ConfigAppValues.getQuiteSleepServiceState() != null) 				
				serviceState = ConfigAppValues.getQuiteSleepServiceState();
			else {
				ClientDDBB clientDDBB = new ClientDDBB();
				Settings settings = clientDDBB.getSelects().selectSettings();
				clientDDBB.close();
				
				if (settings != null)
					serviceState = settings.isQuiteSleepServiceState();
			}
			
			return serviceState;						
												
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;
		}
	}
	
	
	/**
	 * Compare the present time with the schedule start and end times (interval)
	 * 
	 * @return			true if the present time is in the interval or false
	 * 					if isn't
	 * @see				boolean
	 */
	private static boolean checkScheduleAndServiceState (CallLog callLog) {
		
		try {
			
			boolean quiteSleepServiceState = checkQuiteSleepServiceState();
			
			if (quiteSleepServiceState) {	
			
				ClientDDBB clientDDBB = new ClientDDBB();
				Schedule schedule = clientDDBB.getSelects().selectSchedule();
				clientDDBB.close();
				
				if (schedule != null && quiteSleepServiceState && checkDayWeek(schedule)) {
					
					return isInInterval(callLog, schedule);								
				}
				return false;
			}
			return false;
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
			return false;
		}		
	}
	
	
	/**
	 * Function that check if the call incoming with the now time, is between the 
	 * twho hours specified as start and end of the interval specified by the user.
	 * 
	 * @param 			callLog
	 * @param 			schedule
	 * @return			true or false if the incoming call with the actual hour
	 * 					is in an interval delimit by the start and end hours
	 * @see				boolean
	 */
	private static boolean isInInterval (CallLog callLog, Schedule schedule) {
		
		try {
			DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);					
			Calendar dateAndTime = Calendar.getInstance();
			
			String timeNow = timeFormat.format(dateAndTime.getTime());
			String timeStart = schedule.getStartFormatTime();
			String timeEnd = schedule.getEndFormatTime();
													
			String timeNowComplete = getCompleteDate(dateAndTime);
			Log.d(CLASS_NAME, "time now: " + timeNowComplete);
			
			callLog.setTimeCall(timeNowComplete);
			
			SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
			Date start = parser.parse(timeStart);
			Date end = parser.parse(timeEnd);
			Date now = parser.parse(timeNow);
			
			Log.d(CLASS_NAME, "start: " + start + "\ttimeStart: " + timeStart);
			Log.d(CLASS_NAME, "end: " + end + "\ttimeEnd: " + timeEnd);
			Log.d(CLASS_NAME, "now: " + now + "\timeNow: " + timeNow);
			
			
			String dayCompleteString = "24:00";
			Date  dayComplete = parser.parse(dayCompleteString);
			String dayInitString = "00:00";
			Date  dayInit = parser.parse(dayInitString);
			
			
			final int INCREASE = 24;
			boolean isInInterval;
			
			//If both times are equals (24h)		(si 8:00 = 8:00)
			if (start.compareTo(end) == 0)
				isInInterval = true;					
			
			//(Si start=10:00 end=21:00))
			//If end time is after than start time (ie: start=10:00 end=21:00)
			else if (end.after(start) && (now.after(start) && now.before(end)))
				isInInterval = true;
			
			/* If end time is before than start time (ie: start=22:00 end=3:00)
			 * then, we must be add 24 to the end time. (so ie: start=22:00 end:27:00)
			 */					
			
			//(Si start=22:00 end=3:00 ==> newEnd=3:00+24=27:00)
			else if (end.before(start)) {
				String newEndTimeString = TokenizerUtils.addIncreaseDate(
						timeEnd, 
						INCREASE, 
						null);						
				Date newEndTime = parser.parse(newEndTimeString);											
				
				/* (Si start=22:00 now=23:00 dayComplete=24:00, antes 
				 * hemos comprobado que end<start==> end: 3:00)
				 */
				if (now.after(start) && now.before(dayComplete))
					isInInterval = true;
				
				/* (Si now>00:00==> now=2:00 now<newEnd(27:00) ==> now=2:00+24=26:00)
				 * así que ahora queda start=23:00 end=27:00 y now=26:00 
				 */
				else if (now.after(dayInit) && now.before(newEndTime)) {
					String newNowTimeString = TokenizerUtils.addIncreaseDate(
							timeNow,
							INCREASE,
							null);
					Date newNowTime = parser.parse(newNowTimeString);
					
					if (newNowTime.after(start) && newNowTime.before(newEndTime))
						isInInterval = true;
					else
						isInInterval = false;
				}else
					isInInterval = false;						
			}else
				isInInterval = false;
																		
			Log.d(CLASS_NAME, "Está en el intervalo: " + isInInterval);
			
			return isInInterval;
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Get the compelte format date in English mode
	 * 
	 * @param 			now
	 * @return			the complete format date
	 * @see				String
	 */
	private static String getCompleteDate (Calendar now) {
		
		try {
			
			return (now.get(Calendar.MONTH) + 1) + "-"
			        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR) + " "
			        + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":"
			        + now.get(Calendar.SECOND);
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return null;
		}
	}
	
	/**
	 * Check if today is a selected day for use the banned contacts list and schedule.
	 * 
	 * @param 			schedule
	 * @return			true if today is one "banned day" or false if isn't
	 * @see				boolean
	 */
	private static boolean checkDayWeek (Schedule schedule) {
		
		try {
							
			Calendar dateAndTime = Calendar.getInstance();
			
			int dayWeek = dateAndTime.get(Calendar.DAY_OF_WEEK);
			
			Log.d(CLASS_NAME, "Day Week: " + dayWeek);
		
			switch (dayWeek) {
				case Calendar.SUNDAY:
					return schedule.isSunday();					
				case Calendar.MONDAY:
					return schedule.isMonday();					
				case Calendar.TUESDAY:
					return schedule.isTuesday();					
				case Calendar.WEDNESDAY:
					return schedule.isWednesday();
				case Calendar.THURSDAY:
					return schedule.isThursday();
				case Calendar.FRIDAY:
					return schedule.isFriday();
				case Calendar.SATURDAY:
					return schedule.isSaturday();				
			}
			
			return false;
			
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Check if the mail service for send email is activated or not
	 * 
	 * @return		true or false if it is activated or not
	 */
	private static boolean checkMailService () {
		
		try {
			
			if (ConfigAppValues.getMailServiceState() != null)
				return ConfigAppValues.getMailServiceState();
			else {
				ClientDDBB clientDDBB = new ClientDDBB();
				Settings settings = clientDDBB.getSelects().selectSettings();
				if (settings != null) {
					ConfigAppValues.setMailServiceState(settings.isMailService());
					clientDDBB.close();
					return ConfigAppValues.getMailServiceState();									
				}
				/* Mustn't be never this case because previously the settings object
				 * must be created
				 */				
				else {
					settings = new Settings(false);
					clientDDBB.getInserts().insertSettings(settings);
					clientDDBB.commit();
					clientDDBB.close();
					return false;
				}
					
			}
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(e.toString(), e.getStackTrace()));
			return false;
		}
	}
	
	/**
	 * Check if the sms service for send sms is activated or not
	 * 
	 * @return		true or false depends of the state
	 * @see			boolean
	 */
	private static boolean checkSmsService () {
		
		try {
			if (ConfigAppValues.getSmsServiceState() != null)
				return ConfigAppValues.getSmsServiceState();
			else {
				ClientDDBB clientDDBB = new ClientDDBB();
				Settings settings = clientDDBB.getSelects().selectSettings();
				if (settings != null) {
					ConfigAppValues.setSmsServiceState(settings.isSmsService());
					clientDDBB.close();
					return ConfigAppValues.getSmsServiceState();
				}else {
					settings = new Settings(false);
					clientDDBB.getInserts().insertSettings(settings);
					clientDDBB.commit();
					clientDDBB.close();
					return false;
				}				
			}			
						
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			return false;
		}
	}
	
	
	/**
	 * Put the vibrator in mode off
	 */
	public static void vibrateOff () {
		
		try {
			
			String vibratorService = Context.VIBRATOR_SERVICE;
			Vibrator vibrator = (Vibrator)ConfigAppValues.getContext().
				getSystemService(vibratorService);
						
			vibrator.cancel();
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Put the vibrator in mode On
	 */
	public static void vibrateOn () {
		
		try {
			
			String vibratorService = Context.VIBRATOR_SERVICE;
			Vibrator vibrator = (Vibrator)ConfigAppValues.getContext().
				getSystemService(vibratorService);
			
			vibrator.vibrate(1000);
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Send mail to the contact caller
	 * @param incomingNumber
	 */
	private static void sendMail (String incomingNumber, CallLog callLog) {
		
		try {			
			if (checkMailService()) {
			
				//Test for send mail
				SendMail sendMail = new SendMail(incomingNumber);
				int numShipments = sendMail.sendMail();
				if (numShipments > 0) {
					Log.d(CLASS_NAME, "Mail enviado a: " + incomingNumber);
					Log.d(CLASS_NAME, "Num mail enviados: " + numShipments);
				}
				else
					Log.d(CLASS_NAME, "Mail NO se ha enviado");
				
				callLog.setNumSendMail(numShipments);
			}	
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Send SMS message to the contact caller
	 * 
	 * @param incomingNumber
	 */
	private static void sendSMS (String incomingNumber, CallLog callLog) {
		
		try {
			
			if (checkSmsService()) {
					
				Log.d(CLASS_NAME, "antes de enviar el sms");				
				
				//--  If u choose use Android Service for send SMS use this --//
				/*
				ConfigAppValues.getContext().startService(
						new Intent(
								ConfigAppValues.getContext(),
								SendSMSService.class).putExtra(
									ConfigAppValues.RECEIVER, 
									incomingNumber));
				*/
				//------------------------------------------------------------//
				
				//-----	  If u choose Java Thread for send SMS use this  -----//
				SendSMSThread sendSMS = new SendSMSThread(incomingNumber);
				sendSMS.start();
				//------------------------------------------------------------//
															
			}
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));			
		}
	}
	
	/**
	 * Put the mobile in silence mode (audio and vibrate)
	 * 
	 */
	public static void putRingerModeSilent () {
		
		
		try {
			Log.d(CLASS_NAME, "Poniendo el movil en modo silencio");
									
			AudioManager audioManager = 
				(AudioManager)ConfigAppValues.getContext().getSystemService(Context.AUDIO_SERVICE);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);																					
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
		
	}
		
	/**
	 * Put the mobile in normal mode (audio and vibrate), important: normal mode
	 * such the user defined previously  (before to put the mobile in silence)	 
	 * 
	 */
	public static void putRingerModeNormal () {
		
		Log.d(CLASS_NAME, "Poniendo el movil en modo normal");
		
		AudioManager audioManager = 
			(AudioManager)ConfigAppValues.getContext().getSystemService(Context.AUDIO_SERVICE);
		audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}
	
	
	/**
	 * Get what ringer mode is at the moment. 
	 * 
	 * @return			return RINGER_MODE_SILENT(0), RINGER_MODE_NORMAL(2), RINGER_MODE_VIBRATE(1)	  								
	 * @see 			int	
	 * @throws 			Exception
	 */
	public static int ringerMode () throws Exception {
		
		try {
			
			AudioManager audioManager = 
				(AudioManager)ConfigAppValues.getContext().getSystemService(Context.AUDIO_SERVICE);
			
			switch (audioManager.getRingerMode()) {
			
				case AudioManager.RINGER_MODE_SILENT:
					Log.d(CLASS_NAME, "Ringer_Mode_Silent");					
					break;
				
				case AudioManager.RINGER_MODE_NORMAL:
					Log.d(CLASS_NAME, "Ringer_Mode_Normal");					
					break;
				
				case AudioManager.RINGER_MODE_VIBRATE:
					Log.d(CLASS_NAME, "Ringer_Mode_Vibrate");
					break;
					
				default:
					break;
			}
			
			return audioManager.getRingerMode();
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
			throw new Exception();
		}
		
	}
		
}
