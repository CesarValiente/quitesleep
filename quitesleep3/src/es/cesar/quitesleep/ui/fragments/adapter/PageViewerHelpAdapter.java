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

import com.actionbarsherlock.app.SherlockFragment;

import es.cesar.quitesleep.ui.fragments.HelpFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author Cesar Valiente Gordo (cesar.valiente@gmail.com)
 *
 * This class is used as {@link FragmentPagerAdapter} to create the different
 * {@link SherlockFragment} used.
 */
public class PageViewerHelpAdapter extends FragmentPagerAdapter {

	private final String CLASS_NAME = getClass().getName();
	
	private final int NUM_PAGES = 5;
	
	public PageViewerHelpAdapter (FragmentManager fm) {
		super(fm);
	}
        
	@Override
	public Fragment getItem(int position) {		
		return HelpFragment.newInstance(position);
	}

	@Override
	public int getCount() {		
		return NUM_PAGES;
	}
	
	

}
