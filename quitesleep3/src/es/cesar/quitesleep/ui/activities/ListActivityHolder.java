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
import es.cesar.quitesleep.ui.activities.base.BaseFragmentActivity;
import es.cesar.quitesleep.ui.fragments.AddBannedFragment;
import es.cesar.quitesleep.ui.fragments.DeleteBannedFragment;
import es.cesar.quitesleep.utils.Log;

public class ListActivityHolder extends BaseFragmentActivity {
	
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
			
			if (fragmentType == ConfigAppValues.TypeFragment.ADD_CONTACTS.ordinal()) {
				Log.d(CLASS_NAME, "fragmentType addcontacts: " + fragmentType);
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				AddBannedFragment fragment = AddBannedFragment.newInstance(); 
				ft.replace(R.id.holder, fragment);
				ft.commit();
			}else if (fragmentType == ConfigAppValues.TypeFragment.REMOVE_CONTACTS.ordinal()) {
				Log.d(CLASS_NAME, "fragmentType removeContacts: " + fragmentType);
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				DeleteBannedFragment fragment = DeleteBannedFragment.newInstance();
				ft.replace(R.id.holder, fragment);
				ft.commit();
			}
		}
	}	
}
