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
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.components.listeners.ContactDialogListener;
import es.cesar.quitesleep.settings.ConfigAppValues;


/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class ContactsFragmentDialog extends SherlockDialogFragment {

	private final String CLASS_NAME = getClass().getName();
				
	private ConfigAppValues.DialogType dialogType;
	
	private ContactDialogListener listener;
			
	private int alertDialogImage;	
	private int title;
	private int message;
			
	//------------------------------------------------------------------------//
	
	 
	/**
	 * Gets a new {@link ContactsFragmentDialog}
	 * @param listener
	 * @param dialogType
	 * @return
	 */
	public static ContactsFragmentDialog newInstance (			
			ContactDialogListener listener, 
			ConfigAppValues.DialogType dialogType) {
		
		ContactsFragmentDialog warningDialog = new ContactsFragmentDialog(dialogType, listener);			
		return warningDialog;
	}
			
	/**
	 * Constructor
	 * @param operationType
	 * @oaram listener
	 */
	public ContactsFragmentDialog (ConfigAppValues.DialogType operationType, ContactDialogListener listener) {
		
		this.dialogType = operationType;
		this.listener = listener;
	}		

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {						
												
		setLabelsOperationType();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity())								
			.setIcon(alertDialogImage)
			.setTitle(title)
			.setMessage(message)															
			.setCancelable(false)				
			.setPositiveButton(
    		   R.string.warningdialog_yes_label, 
    		   new DialogInterface.OnClickListener() {    			   
    			   public void onClick(DialogInterface dialog, int id) {    				         		                		              	                
    				   listener.clickYes();		                		                          
    			   }
    		   })
			.setNegativeButton(
    		   R.string.warningdialog_no_label, 
    		   new DialogInterface.OnClickListener() {	    			   
    			   public void onClick(DialogInterface dialog, int id) {	        	      				   	              
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
			alertDialogImage = R.drawable.quitesleep;
			title = R.string.warningdialog_firstime_title;
			message = R.string.warningdialog_firstime_message;
		}else if (dialogType == ConfigAppValues.DialogType.SYNC_REST_OF_TIMES) {
			alertDialogImage = R.drawable.dialog_warning;
			title = R.string.warningdialog_caution_label;
			message = R.string.warningdialog_synccontact_label;
		}else if (dialogType == ConfigAppValues.DialogType.ADD_ALL_CONTACTS) {
			alertDialogImage = R.drawable.dialog_warning;
			title = R.string.warningdialog_caution_label;
			message = R.string.warningdialog_contactOperations_label;
		}else if (dialogType == ConfigAppValues.DialogType.REMOVE_ALL_CONTACTS) {
			alertDialogImage = R.drawable.dialog_warning;
			title = R.string.warningdialog_caution_label;
			message = R.string.warningdialog_contactOperations_label;
		}
	}
	

}
