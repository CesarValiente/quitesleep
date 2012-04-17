package es.cesar.quitesleep.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.TextView.BufferType;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.fragments.AddBanned;
import es.cesar.quitesleep.ui.fragments.DeleteBanned;
import es.cesar.quitesleep.utils.Log;

public class ListActivityHolder extends BaseSherlockActivity {
	
	private final String CLASS_NAME = getClass().getName();
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		super.homeToUp(true);				
		
		setContentView(R.layout.listactivityholder);
		
		Intent intent = getIntent();
		if (intent != null) {			
			int fragmentType = intent.getIntExtra(ConfigAppValues.TYPE_FRAGMENT, 0);
			Log.d(CLASS_NAME, "fragmentType: " + fragmentType);
			
			if (fragmentType == ConfigAppValues.TypeContacts.ADD_CONTACTS.ordinal()) {
				Log.d(CLASS_NAME, "fragmentType addcontacts: " + fragmentType);
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				AddBanned fragment = AddBanned.newInstance(); 
				ft.replace(R.id.holder, fragment);
				ft.commit();
			}else if (fragmentType == ConfigAppValues.TypeContacts.REMOVE_CONTACTS.ordinal()) {
				Log.d(CLASS_NAME, "fragmentType removeContacts: " + fragmentType);
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				DeleteBanned fragment = DeleteBanned.newInstance();
				ft.replace(R.id.holder, fragment);
				ft.commit();
			}
		}
	}
	/*
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.addallmenu, menu);
		
		return true;				
	}
	*/
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		
		switch(item.getItemId()) {
			
			//To come back to the previous activity we finalize it	
			case android.R.id.home :
				finish();			
				break;						
		}				
		return true;
	}
	

}
