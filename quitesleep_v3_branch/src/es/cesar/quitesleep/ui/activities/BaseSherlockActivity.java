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

package es.cesar.quitesleep.ui.activities;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.utils.Log;

/**
 * 
 * @author Cesar Valiente Gordo (cesar.valiente@gmail.com)
 *
 * This class is used as base of the rest of activities to have
 * the same and repeated functionality withou the necesity to implement the
 * same code over and over again.
 */
public class BaseSherlockActivity extends SherlockFragmentActivity {				
	
	private final String CLASS_NAME = getClass().getName();
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);				
	}
		
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.informationmenu, menu);
		
		return true;				
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		
		switch(item.getItemId()) {
			
			//To come back to the previous activity we finalize it	
			case android.R.id.home :
				finish();			
				break;
			case R.id.menu_information_about:
				//TODO
				Log.d(CLASS_NAME, "about");
				break;
			case R.id.menu_information_help:
				//TODO
				Log.d(CLASS_NAME, "help");
				break;				
		}				
		return true;
	}
	
	/**
	 * This function update the action bar home icon to allow up navigation 
	 * @param homeToUp
	 */
	public void homeToUp (boolean homeToUp) {
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(homeToUp);
	}			
}
