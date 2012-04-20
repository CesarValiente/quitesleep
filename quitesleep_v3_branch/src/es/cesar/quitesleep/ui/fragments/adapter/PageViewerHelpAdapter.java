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
