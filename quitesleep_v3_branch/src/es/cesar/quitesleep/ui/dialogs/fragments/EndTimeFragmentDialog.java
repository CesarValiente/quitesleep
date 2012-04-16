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

import java.text.DateFormat;
import java.util.Calendar;

import com.actionbarsherlock.app.SherlockDialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.R.menu;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.Schedule;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class EndTimeFragmentDialog extends SherlockDialogFragment {
	
	private String CLASS_NAME = getClass().getName();	
	
	DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);		
	Calendar dateAndTime = Calendar.getInstance();
		
	private static TextView mEndTimeLabel;
			
	
	public static EndTimeFragmentDialog newInstance (Fragment fragment, TextView endTimeLabel) {
		
		EndTimeFragmentDialog endTimeDialog = new EndTimeFragmentDialog();
		endTimeDialog.setTargetFragment(fragment, 0);
		mEndTimeLabel = endTimeLabel;
		return endTimeDialog;		
	}
	
	TimePickerDialog.OnTimeSetListener timerPickerEnd = 
          new TimePickerDialog.OnTimeSetListener() {
          
          @Override
          public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                          
              view.setIs24HourView(true);
              dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
              dateAndTime.set(Calendar.MINUTE, minute);                       
              
              //Update operations
              updateSchedule();                  
              updateEndTimeLabel();
                  
          }
      };

	
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {								
		
		TimePickerDialog timePickerDialog = new TimePickerDialog(
				getTargetFragment().getActivity(),
				timerPickerEnd,
				dateAndTime.get(Calendar.HOUR_OF_DAY),
				dateAndTime.get(Calendar.MINUTE),
				true);	
		return timePickerDialog;
	}
	
	 /**
     * Update the endTimeLabel located in the activity ScheduleTab, with the
     * selected by user endTime.
     */
    private void updateEndTimeLabel () {
                                                                    
        if (mEndTimeLabel != null)
                mEndTimeLabel.setText(timeFormat.format(dateAndTime.getTime()));                   
    }
	
	
	/**
	 * Update the Schedule object from the database with the end time objects
	 * that have been used in the dialog and specified by the user.
	 * 
	 * @throws Exception
	 */
	private void updateSchedule () {
				
		try {
			
			ClientDDBB clientDDBB = new ClientDDBB();
			
			Schedule schedule = clientDDBB.getSelects().selectSchedule();
			
			/* If the Schedule object is null, then never have been created before
			 * so, we have to create here.
			 */			
			if (schedule == null)
				schedule = new Schedule();
			
			schedule.setAllEndTime(
					dateAndTime.getTime(), 
					timeFormat.format(dateAndTime.getTime()));
			
			clientDDBB.getUpdates().insertSchedule(schedule);
			
			clientDDBB.commit();			
			clientDDBB.close();						
			
			es.cesar.quitesleep.utils.Toast.r(
            		getTargetFragment().getActivity(),
            		QuiteSleepApp.getContext().getString(
            				R.string.schedule_toast_endTime),
            		Toast.LENGTH_SHORT);	
						
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
			throw new Error(e.toString());
		}
	}

}
	