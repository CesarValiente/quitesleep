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

package es.cesar.quitesleep.ui.dialogs.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockDialogFragment;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.components.interfaces.IDialogs;
import es.cesar.quitesleep.components.listeners.DialogListener;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.activities.BaseSherlockActivity;
import es.cesar.quitesleep.ui.fragments.ContactsFragment;
import es.cesar.quitesleep.utils.ExceptionUtils;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class WarningFragmentDialog extends SherlockDialogFragment {

	private final String CLASS_NAME = getClass().getName();
			
	private int alertDialogImage;	
	private ConfigAppValues.DialogType dialogType;
	
	private DialogListener listener;
				
	private int title;
	private int message;
			
	//------------------------------------------------------------------------//
	
	 
	/**
	 * Gets a new {@link WarningFragmentDialog}
	 * @param fragment
	 * @param listener
	 * @param dialogType
	 * @return
	 */
	public static WarningFragmentDialog newInstance (
			Fragment fragment, 
			DialogListener listener, 
			ConfigAppValues.DialogType dialogType) {
		
		WarningFragmentDialog warningDialog = new WarningFragmentDialog(dialogType, listener);	
		if (fragment != null) {
			warningDialog.setTargetFragment(fragment, 0);
		}
		return warningDialog;
	}
	
	
	
	/**
	 * Constructor
	 * @param operationType
	 * @oaram listener
	 */
	public WarningFragmentDialog (ConfigAppValues.DialogType operationType, DialogListener listener) {
		
		this.dialogType = operationType;
		this.listener = listener;
	}		

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {						
												
		setLabelsOperationType();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity())								
			.setIcon(alertDialogImage)
			.setTitle(title).setMessage(message)															
			.setCancelable(false)				
			.setPositiveButton(
    		   R.string.warningdialog_yes_label, 
    		   new DialogInterface.OnClickListener() {    			   
    			   public void onClick(DialogInterface dialog, int id) {
    				   Log.d(CLASS_NAME, "YES");		            		                		              	                	
    				   listener.clickYes();		                		                          
    			   }
    		   })
			.setNegativeButton(
    		   R.string.warningdialog_no_label, 
    		   new DialogInterface.OnClickListener() {	    			   
    			   public void onClick(DialogInterface dialog, int id) {	        	   
    				   Log.d(CLASS_NAME, "NO");	               
    				   dialog.cancel();			    				   
    				   if (dialogType == ConfigAppValues.DialogType.SYNC_FIRST_TIME)
    					   getSherlockActivity().finish();	    				   	    				  
    			   }
    		   });														
		return builder.create();
	}
	
					
	/**
	 * Set title, message and dialog icon, for each type of operation
	 */
	private void setLabelsOperationType () {
		
								
		if (dialogType == ConfigAppValues.DialogType.SYNC_FIRST_TIME) {
				alertDialogImage = R.drawable.dialog_warning;
				title = R.string.warningdialog_caution_label;
				message = R.string.warningdialog_contactOperations_label;
		}else if (dialogType == ConfigAppValues.DialogType.SYNC_REST_OF_TIMES) {
			  alertDialogImage = R.drawable.dialog_warning;
              title = R.string.warningdialog_caution_label;
              message = R.string.warningdialog_synccontact_label;
		}else if (dialogType == ConfigAppValues.DialogType.SYNC_REST_OF_TIMES) {
				alertDialogImage = R.drawable.dialog_warning;
				title = R.string.warningdialog_caution_label;
				message = R.string.warningdialog_synccontact_label;									
		}else if (dialogType == ConfigAppValues.DialogType.REMOVE_ALL_LOGS) {
				alertDialogImage = R.drawable.dialog_warning;
				title = R.string.warningdialog_caution_label;
				message = R.string.warningdialog_calllog_remove_label;					
		}else if (dialogType == ConfigAppValues.DialogType.REFRESH_ALL_LOGS) {
				alertDialogImage = R.drawable.dialog_warning;
				title = R.string.warningdialog_caution_label;
				message = R.string.warningdialog_calllog_refresh_label;
		}
	}
	

}
