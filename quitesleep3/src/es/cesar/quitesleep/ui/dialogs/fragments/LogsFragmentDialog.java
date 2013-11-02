/* 
 	Copyright 2010-2012 Cesar Valiente Gordo
 
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

package es.cesar.quitesleep.ui.dialogs.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.components.listeners.LogsDialogListener;
import es.cesar.quitesleep.settings.ConfigAppValues;


/**
 * 
 * @author Cesar Valiente Gordo (cesar.valiente@gmail.com)
 *
 * This class is used to create {@link SherlockDialogFragment} in the 
 * {@link LogsFragmentDialog}
 */
public class LogsFragmentDialog extends SherlockDialogFragment {

	private final String CLASS_NAME = getClass().getName();
	
	private ConfigAppValues.DialogType dialogType;
	
	private LogsDialogListener listener;
					
	private int message;
	
	
	/**
	 * Gets a new {@link LogsFragmentDialog}
	 * @param listener
	 * @param dialogType
	 * @return
	 */
	public static LogsFragmentDialog newInstance (			
			LogsDialogListener listener, 
			ConfigAppValues.DialogType dialogType) {
		
		LogsFragmentDialog callLogDialog = new LogsFragmentDialog(dialogType, listener);			
		return callLogDialog;
	}
	
	/**
	 * Constructor
	 * @param operationType
	 * @oaram listener
	 */
	public LogsFragmentDialog (ConfigAppValues.DialogType operationType, LogsDialogListener listener) {
		
		this.dialogType = operationType;
		this.listener = listener;
	}
	
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {						
												
		setMessageLabel();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity())								
			.setIcon(R.drawable.dialog_warning)
			.setTitle(R.string.warningdialog_caution_label)
			.setMessage(message)															
			.setCancelable(false)				
			.setPositiveButton(
    		   R.string.warningdialog_yes_label, 
    		   new DialogInterface.OnClickListener() {    			   
    			   public void onClick(DialogInterface dialog, int id) {

    				   if (dialogType == ConfigAppValues.DialogType.REFRESH_ALL_LOGS)
    					   listener.clickYesRefreshLogs();
    				   else if (dialogType == ConfigAppValues.DialogType.REMOVE_ALL_LOGS)
    					   listener.clickYesClearLogs();
    			   }
    		   })
			.setNegativeButton(
    		   R.string.warningdialog_no_label, 
    		   new DialogInterface.OnClickListener() {	    			   
    			   public void onClick(DialogInterface dialog, int id) {	        	   	               
    				   dialog.cancel();			    				   
    			   }
    		   });														
		return builder.create();
	}
	
	
	/**
	 * Set title, message and dialog icon, for each type of operation
	 */
	private void setMessageLabel () {
		
		if (dialogType == ConfigAppValues.DialogType.REMOVE_ALL_LOGS) 					
			message = R.string.warningdialog_calllog_remove_label;					
		else if (dialogType == ConfigAppValues.DialogType.REFRESH_ALL_LOGS) 			
			message = R.string.warningdialog_calllog_refresh_label;			
	}
	
}
