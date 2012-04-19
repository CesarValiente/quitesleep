package es.cesar.quitesleep.tasks;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import es.cesar.quitesleep.components.listeners.LogsDialogListener;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.CallLog;
import es.cesar.quitesleep.ui.fragments.base.BaseListFragment;
import es.cesar.quitesleep.utils.Log;


/**
 * 
 * @author Cesar Valiente Gordo (cesar.valiente@gmail.com)
 *  
 * This class is used to load all {@link CallLog} objects
 */
public class LoadLogsTask extends AsyncTask<Void, Void, List<String>>{
		
	private BaseListFragment listener;	
	
	/**
	 * Constructor 
	 * @param handler
	 */
	public LoadLogsTask (BaseListFragment listener) {
		
		this.listener = listener;
	}
	
	
	@Override
	protected List<String> doInBackground(Void... params) {
		
		Log.d("cesar", "loading calllogs");
		ClientDDBB clientDDBB = new ClientDDBB();
		
		List<CallLog> callLogList = clientDDBB.getSelects().selectAllCallLog();
		List<String> callLogListString = convertCallLogList(callLogList);
		
		clientDDBB.close();
		
		return callLogListString;
	}
	
	@Override
	protected void onPostExecute (List<String> result) {
		
		listener.getDataInfo(result);				
	}
		
	/**
	 * 
	 * @param 		callLogList
	 * @return 		The callLogList 
	 * @see			List<String>
	 */
	private List<String> convertCallLogList (List<CallLog> callLogList) {
							
		if (callLogList != null && callLogList.size()>0) {
			
			List<String> callLogListString = new ArrayList<String>();
			
			for (int i=0; i<callLogList.size(); i++) {
				String callLogString = callLogList.get(i).toString();
				if (callLogString != null)						
					callLogListString.add(callLogString);					
			}
			return callLogListString;
		}
		return null;					
	}
	

}
