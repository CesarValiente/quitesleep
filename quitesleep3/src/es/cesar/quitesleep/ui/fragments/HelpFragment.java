package es.cesar.quitesleep.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;

import es.cesar.quitesleep.R;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.utils.Log;


/**
 * 
 * @author Cesar Valiente Gordo (cesar.valiente@gmail.com)
 * 
 *  This class is used to allocate the {@link WebView} used to show information
 *  about the different options of the app and the about me info.
 */
public class HelpFragment extends SherlockFragment {
	
	private final String CLASS_NAME = getClass().getName();
	
	private WebView webView;	
			
	/**
	 * Gets a new instance of the {@link HelpFragment} with the 
	 * correspondent uri
	 * @param helpPage
	 * @return
	 */
	public static HelpFragment newInstance (int helpPage) {			
		
		String uri = null;
		HelpFragment helpFragment = new HelpFragment();
		
		switch (helpPage) {
			case 0:
				uri = ConfigAppValues.HELP_CONTACT_URI;
				break;
			case 1: 
				uri = ConfigAppValues.HELP_SCHEDULE_URI;
				break;
			case 2:
				uri = ConfigAppValues.HELP_SETTINGS_URI;
				break;
			case 3: 
				uri = ConfigAppValues.HELP_LOGS_URI;
				break;
			case 4:
				uri = ConfigAppValues.ABOUT_URI;
				break;
		}
				
		Bundle bundle = new Bundle();
		bundle.putString(ConfigAppValues.TYPE_FRAGMENT, uri);
		helpFragment.setArguments(bundle);
		
		Log.d("HelpFragment", "Fragment: " + helpPage + "\tPage: " + uri);
		return helpFragment;
	}
	
	
	/**
	 * Gets the uri used in the fragment
	 * @return
	 */
	public String getUri() {
		
		return getArguments().getString(ConfigAppValues.TYPE_FRAGMENT);
	}
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {				
		
		if (container == null)
			return null;								
			
		View view = inflater.inflate(R.layout.help, container, false);										
		String uri = getUri();
		if (uri != null) {												
			 webView = (WebView) view.findViewById(R.id.webview);
			 webView.getSettings().setJavaScriptEnabled(true);
			 webView.loadUrl(uri);
		}										
		return view;
	}

}
