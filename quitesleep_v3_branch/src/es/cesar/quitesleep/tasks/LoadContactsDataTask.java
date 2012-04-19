package es.cesar.quitesleep.tasks;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.Banned;
import es.cesar.quitesleep.data.models.Contact;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.settings.ConfigAppValues.TypeFragment;
import es.cesar.quitesleep.ui.fragments.base.BaseListFragment;

public class LoadContactsDataTask extends AsyncTask<Void, Void, List<String>> {

	private BaseListFragment listener;
	private ConfigAppValues.TypeFragment typeContacts;
	
	/**
	 * Constructor
	 * @param listener
	 * @param typeContacts
	 */
	public LoadContactsDataTask(BaseListFragment listener, TypeFragment typeContacts) {
	
		this.listener = listener;
		this.typeContacts = typeContacts;
	}		
		
	@Override	
	protected List<String> doInBackground (Void ... params) {
	
		ClientDDBB clientDDBB = new ClientDDBB();			
		
		List contactList = null;
		
		if (typeContacts == TypeFragment.ADD_CONTACTS)
			contactList = (List<Contact>) clientDDBB.getSelects().selectAllNotBannedContacts();
		else
			contactList = (List<Banned>) clientDDBB.getSelects().selectAllBannedContacts();
		
		List<String> contactListString = convertContactList(contactList);		
		
		clientDDBB.close();
		
		return contactListString;
	}
	
	@Override
	protected void onPostExecute (List<String> result) {
	
		listener.getDataInfo(result);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param 		contactList
	 * @return 		The contactList but only the list with the name contacts
	 * @see			List<String>
	 */
	private <T> List<String> convertContactList (List<T> contactList) {
							
		if (contactList != null && contactList.size()>0) {
			
			List<String> contactListString = new ArrayList<String>();
			
			String contactName = null;
			for (int i=0; i<contactList.size(); i++) {
								
				if (typeContacts == typeContacts.ADD_CONTACTS) 
					contactName = ((List<Contact>)contactList).get(i).getContactName();					
				else 
					contactName = ((List<Banned>)contactList).get(i).getContact().getContactName();
									
				if (contactName != null)						
					contactListString.add(contactName);					
			}
			return contactListString;
		}
		return null;					
	}
	
	

}
