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
 */
public class BaseActivity extends SherlockFragmentActivity {				
	
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
