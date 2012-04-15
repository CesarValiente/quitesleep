package es.cesar.quitesleep.components.listeners;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

/**
 * 
 * @author Cesar Valiente Gordo (cesar.valiente@gmail.com)
 *
 * This interface provides mechanisms to communicate between {@link AsyncTask} and
 * {@link Fragment} or {@link Activity} to retrieve the data after obtained it
 * or to 
 */
public interface LoadContactsListener {
	
	public void getDataContacts (List<String> contactList);
}
