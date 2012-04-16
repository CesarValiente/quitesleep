package es.cesar.quitesleep.ui.dialogs.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.operations.DialogOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class SmsEmailFragmentDialog extends SherlockDialogFragment {

        private final String CLASS_NAME = getClass().getName();
                        
        private String titleLabel, messageLabel;
        private ConfigAppValues.DialogType dialogType;
        
        
        /**
         * Gets a new instance of {@link SmsEmailFragmentDialog}
         * @param dialogType
         * @return
         */
        public static SmsEmailFragmentDialog newInstance (ConfigAppValues.DialogType dialogType) {
        	
        	SmsEmailFragmentDialog copy = new SmsEmailFragmentDialog(dialogType);
        	return copy;
        }
        
        /**
         * Constructor
         * @param dialogType
         */
        public SmsEmailFragmentDialog (ConfigAppValues.DialogType dialogType) {
        	
        	this.dialogType = dialogType;        	
        }
                                                        
        
       
       @Override
        public Dialog onCreateDialog (Bundle savedInstanceState) {
                                
    	   	setLabels();
    	   
            AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());                                                    
            
            builder.setIcon(R.drawable.dialog_warning)
            	.setTitle(titleLabel)
            	.setMessage(messageLabel)
            	.setPositiveButton(R.string.warningdialog_ok_label, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (dialogType == ConfigAppValues.DialogType.SMS_DIALOG)
							DialogOperations.checkSmsService(QuiteSleepApp.getContext(), true);
						else if (dialogType == ConfigAppValues.DialogType.MAIL_DIALOG)
							DialogOperations.checkMailService(QuiteSleepApp.getContext(), true);					
					}
				});
                                                                                 
            return builder.create();         
        }
        
       /**
        * Sets the labels used in the dialog
        */
       private void setLabels () {
    	   
    	   if (dialogType == ConfigAppValues.DialogType.MAIL_DIALOG) {
    		   titleLabel = QuiteSleepApp.getContext().getString(R.string.warningdialog_caution_label);
    		   messageLabel = QuiteSleepApp.getContext().getString(R.string.warningdialog_mail_label);
    	   }else if (dialogType == ConfigAppValues.DialogType.SMS_DIALOG) {
    		   titleLabel = QuiteSleepApp.getContext().getString(R.string.warningdialog_caution_label);
    		   messageLabel = QuiteSleepApp.getContext().getString(R.string.warningdialog_sms_label);
    	   }
    	   
       }
        
        
}
