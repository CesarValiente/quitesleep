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

package es.cesar.quitesleep.ui.fragments.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.TitleProvider;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.application.QuiteSleepApp;
import es.cesar.quitesleep.ui.fragments.ContactsFragment;
import es.cesar.quitesleep.ui.fragments.LogsFragment;
import es.cesar.quitesleep.ui.fragments.ScheduleFragment;
import es.cesar.quitesleep.ui.fragments.SettingsFragment;
import es.cesar.quitesleep.utils.Log;

public class PageViewerTabsAdapter extends FragmentPagerAdapter implements TitleProvider {
    
	private final String CLASS_NAME = getClass().getName();
	
	private Resources resources = QuiteSleepApp.getContext().getResources();
	
    public final String[] TAB_TITLES = { 
    	resources.getString(R.string.contacts_tab_label), 
    	resources.getString(R.string.schedule_tab_label),
    	resources.getString(R.string.settings_tab_label),
    	resources.getString(R.string.logs_tab_label)};
    
    
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
