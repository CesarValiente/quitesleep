package es.cesar.quitesleep.ui.fragments.adapter;

import es.cesar.quitesleep.ui.fragments.ContactsFragment;
import es.cesar.quitesleep.ui.fragments.LogsFragment;
import es.cesar.quitesleep.ui.fragments.ScheduleFragment;
import es.cesar.quitesleep.ui.fragments.SettingsFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageViewerAdapter extends FragmentPagerAdapter implements TitleProvider {
    
    public static final String[] TAB_TITLES = { "Contacts", "Schedule", "Settings", "Logs"};
    
    public PageViewerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new ContactsFragment();
        } else if(position == 1){
            return new ScheduleFragment();
        } else if(position == 2){
            return new SettingsFragment();
        } else if (position == 3) {
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
