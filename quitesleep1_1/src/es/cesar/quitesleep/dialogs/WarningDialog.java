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

package es.cesar.quitesleep.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.staticValues.ConfigAppValues;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class WarningDialog {

	private final String CLASS_NAME = getClass().getName();
			
	private int alertDialogImage = R.drawable.dialog_warning;
	
	private AlertDialog alertDialog;
	private int operationType;
	
	//Use for addAll, removeAll, smsService and mailService
	private Context context;
	
	//Use for addAll and removeAll
	private ArrayAdapter<String> arrayAdapter;
	private Handler handler;
	
	//Use for smsService and mailService
	private boolean toggleButtonIsChecked;
			
	private int title;
	private int message;
			
	//------------------		Getters & Setters		----------------------//
	public AlertDialog getAlertDialog() {
		return alertDialog;
	}
	public void setAlertDialog(AlertDialog alertDialog) {
		this.alertDialog = alertDialog;
	}
	
	public int getOperationType() {
		return operationType;
	}
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}
	
	public int getTitle() {
		return title;
	}
	public void setTitle(int title) {
		this.title = title;
	}
	
	public int getMessage() {
		return message;
	}
	public void setMessage(int message) {
		this.message = message;
	}
	
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
	public ArrayAdapter<String> getArrayAdapter() {
		return arrayAdapter;
	}
	public void setArrayAdapter(ArrayAdapter<String> arrayAdapter) {
		this.arrayAdapter = arrayAdapter;
	}
	
	public Handler getHandler() {
		return handler;
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	
	public boolean isToggleButtonIsChecked() {
		return toggleButtonIsChecked;
	}
	public void setToggleButtonIsChecked(boolean toggleButtonIsChecked) {
		this.toggleButtonIsChecked = toggleButtonIsChecked;
	}
	//------------------------------------------------------------------------//
	
	
	
	
	
	
	/**
	 * Get the title and message for each type of operation
	 */
	private void getLabelsOperationType () {
		
		try {
			
			title = R.string.warningdialog_caution_label;
			
			switch (operationType) {
			
				case ConfigAppValues.WARNING_ADD_ALL_CONTACTS:
					message = R.string.warningdialog_contactOperations_label;
					break;
				case ConfigAppValues.WARNING_REMOVE_ALL_CONTACTS:
					message = R.string.warningdialog_contactOperations_label;
					break;
				case ConfigAppValues.WARNING_SYNC_CONTACTS:
					message = R.string.warningdialog_synccontact_label;
					break;
				case ConfigAppValues.WARNING_SMS_ACTION:
					message = R.string.warningdialog_sms_label;
					break;
				case ConfigAppValues.WARNING_MAIL_ACTION:
					message = R.string.warningdialog_mail_label;
					break;
				case ConfigAppValues.WARNING_REMOVE_ALL_CALL_LOGS:
					message = R.string.warningdialog_calllog_remove_label;
					break;
				case ConfigAppValues.WARNING_REFRESH_CALL_LOG:
					message = R.string.warningdialog_calllog_refresh_label;
					break;
				default:
					break;
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * 
	 */
	private void callOperationType () {
		
		try {
			
			switch (operationType) {
				
				case ConfigAppValues.WARNING_ADD_ALL_CONTACTS:
					DialogOperations.addAllContacts(context, arrayAdapter, handler);
					break;
				case ConfigAppValues.WARNING_REMOVE_ALL_CONTACTS:		
					DialogOperations.removeAllContacts(context, arrayAdapter, handler);
					break;
				case ConfigAppValues.WARNING_SYNC_CONTACTS:
					DialogOperations.syncContactsRefresh();
					break;
				case ConfigAppValues.WARNING_SMS_ACTION:
					DialogOperations.checkSmsService(context, toggleButtonIsChecked);
					break;
				case ConfigAppValues.WARNING_MAIL_ACTION:
					DialogOperations.checkMailService(context, toggleButtonIsChecked);
					break;
				case ConfigAppValues.WARNING_REMOVE_ALL_CALL_LOGS:
					DialogOperations.removeAllCallLogs(context, arrayAdapter, handler);
					break;
				case ConfigAppValues.WARNING_REFRESH_CALL_LOG:
					DialogOperations.refreshAllCallLogs(context, arrayAdapter, handler);
					break;
				default:
					break;
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(), 
					e.getStackTrace()));
		}
	}
	
	/**
	 * Constructor with the basic parameter
	 * @param activity
	 */
	public WarningDialog (final Activity activity, int operationType) {
				
		this.operationType 	= 		operationType;		
		
		getLabelsOperationType();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
								
		builder.setIcon(alertDialogImage);
		builder.setTitle(title)
				.setMessage(message);				
				
				if ((operationType != ConfigAppValues.WARNING_SMS_ACTION) &&
						(operationType != ConfigAppValues.WARNING_MAIL_ACTION))  {
					
					builder.setCancelable(false);
					
					builder.setPositiveButton(
		    		   R.string.warningdialog_yes_label, 
		    		   new DialogInterface.OnClickListener() {
		    			   
		    			   public void onClick(DialogInterface dialog, int id) {
		    				   Log.d(CLASS_NAME, "YES");		            		                		              
		                		
		    				   callOperationType();		                		                           
		    			   }
		    		   });
					builder.setNegativeButton(
		    		   R.string.warningdialog_no_label, 
		    		   new DialogInterface.OnClickListener() {
		    			   
		    			   public void onClick(DialogInterface dialog, int id) {
		        	   
		    				   Log.d(CLASS_NAME, "NO");
		               
		    				   dialog.cancel();		               
		    				   //no debemos hacer nada extra.
		    			   }
		    		   });		
					
				}else {
					builder.setNeutralButton(
						R.string.warningdialog_ok_label, 
						new DialogInterface.OnClickListener() {
				    			   
							public void onClick(DialogInterface dialog, int id) {
								
								Log.d(CLASS_NAME, "OK");
								callOperationType();		               
								
				    		}
						});							
				}
		
		alertDialog = builder.create();		
	}
	
	
	
}
