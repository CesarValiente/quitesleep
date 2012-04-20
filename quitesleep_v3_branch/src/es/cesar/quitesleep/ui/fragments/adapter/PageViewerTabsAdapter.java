package es.cesar.quitesleep.ui.fragments.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.TitleProvider;

import es.cesar.quitesleep.ui.fragments.ContactsFragment;
import es.cesar.quitesleep.ui.fragments.LogsFragment;
import es.cesar.quitesleep.ui.fragments.ScheduleFragment;
import es.cesar.quitesleep.ui.fragments.SettingsFragment;
import es.cesar.quitesleep.utils.Log;

public class PageViewerTabsAdapter extends FragmentPagerAdapter implements TitleProvider {
    
	private final String CLASS_NAME = getClass().getName();
	
    public static final String[] TAB_TITLES = { "Contacts", "Schedule", "Settings", "Logs"};
    
    
    public PageViewerTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	
        if(position == 0){
        	Log.d(CLASS_NAME, "Contacts");
            return new ContactsFragment();
        } else if(position == 1){
        	Log.d(CLASS_NAME, "Schedule");
            return new ScheduleFragment();
        } else if(position == 2){
        	Log.d(CLASS_NAME, "Settings");
            return new SettingsFragment();
        } else if (position == 3) {
        	Log.d(CLASS_NAME, "Logs");
        	return new LogsFragment();
        }
        
        return null;
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

    @Override
    public String getTitle(int position) {
        return TAB_TITLES[position % TAB_TITLES.length].toUpperCase();
    }
}
