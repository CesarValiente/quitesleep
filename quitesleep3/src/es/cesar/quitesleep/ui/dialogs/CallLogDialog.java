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

package es.cesar.quitesleep.ui.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.settings.ConfigAppValues;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class CallLogDialog {
	
	private String CLASS_NAME = getClass().getName();	
		
	private ProgressDialog progressDialog;
	
	
	//--------	Getters & Setters	--------------------------------------//
	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}		
	//--------------------------------------------------------------------//
	
	/**
	 * Constructor without parameters. 
	 */
	public CallLogDialog () {		
			
	}
			
	

	/**
	 * Show the synchronization message
	 */
	public void showDialog (Context context, ConfigAppValues.DialogType typeDialog) {
		
		if (typeDialog == ConfigAppValues.DialogType.REMOVE_ALL_LOGS)
			progressDialog = ProgressDialog.show(
				context, 
				"",
				context.getString(R.string.calllogdialog_dialog_remove_label), 
				true);
		
		else if (typeDialog == ConfigAppValues.DialogType.REFRESH_ALL_LOGS)
			progressDialog = ProgressDialog.show(
					context, 
					"",
					context.getString(R.string.calllogdialog_dialog_refresh_label), 
					true);
			
	}
	
	/**
	 * Hide and dismiss the synchronization message
	 * @param context
	 */
	public void stopDialog () {
		//progressDialog.cancel();
		progressDialog.dismiss();
	}

}
