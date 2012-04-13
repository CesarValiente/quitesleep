package es.cesar.quitesleep.application;

import android.app.Application;
import android.content.Context;

public class QuiteSleepApp extends Application {
	
	private static Context context = null;
	
	@Override
	public void onCreate () {
		
		super.onCreate();
		
		if (context == null)
			context = this.getApplicationContext();
	}
	
	/**
	 * 
	 * @return {@link Context}
	 */
	public static Context getContext () {
		return context;
	}

}
