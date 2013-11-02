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

package es.cesar.quitesleep.subactivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import es.cesar.quitesleep.R;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class Help extends Activity implements OnClickListener{
	
	private final String CLASS_NAME = getClass().getName();
	
	private final int backButtonId = R.id.information_button_cancel;
	
	private Button backButton;
	
	public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.help);
		
		backButton = (Button)findViewById(backButtonId);
		
		backButton.setOnClickListener(this);
	}
	
	public void onClick (View view) {
		
		int viewId = view.getId();
		
		switch (viewId) {
		
			case backButtonId:			
				setResult(Activity.RESULT_CANCELED);
				finish();
				break;
				
			default:
				break;
		}
	}

}
