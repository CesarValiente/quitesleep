/* 
 	Copyright 2010 Cesar Valiente Gordo
 
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

package es.cesar.quitesleep.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.BlockCallsConf;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.activities.base.BaseFragmentActivity;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.Log;

/**
 * 
 * @author		Cesar Valiente Gordo
 * @mail		cesar.valiente@gmail.com	
 * 
 * @version 1 07/29/2010
 * @version 2 01/23/2011
 * 
 * Class to set the block way of the incoming calls, as all incoming calls 
 * and/or unknown calls, etc.
 * 
 */
public class BlockCallsActivity extends BaseFragmentActivity implements OnClickListener {
	
	private final String CLASS_NAME = this.getClass().getName();
	
	//IDs for the radioButton elements
	private final int blockAllId = R.id.configothercalls_radiobutton_blockall;
	private final int blockBlockedContactsId = R.id.configothercalls_radiobutton_block_blocked_contacts;	
	private final int blockUnknownId = R.id.configothercalls_radiobutton_blockunknown;
	private final int blockUnknownAndBlockedContactsId = R.id.configothercalls_radiobutton_block_unknwown_and_blocked_contacts;
	
	//Widgets using in this Activity
	private RadioButton blockAll;
	private RadioButton blockBlockedContacts;
	private RadioButton blockUnknown;
	private RadioButton blockUnknownAndBlockedContacts;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
						
		super.onCreate(savedInstanceState);
		super.homeToUp(true);
				
		setContentView(R.layout.configblockcalls);
		
		//Initialize the radioButtons
		blockAll = (RadioButton)findViewById(blockAllId);			
		blockBlockedContacts = (RadioButton)findViewById(blockBlockedContactsId);
		blockUnknown = (RadioButton)findViewById(blockUnknownId);
		blockUnknownAndBlockedContacts = (RadioButton)findViewById(blockUnknownAndBlockedContactsId);
		
		//Setting the OnClickListener events for every radioButton
		blockAll.setOnClickListener(this);			
		blockBlockedContacts.setOnClickListener(this);
		blockUnknown.setOnClickListener(this);
		blockUnknownAndBlockedContacts.setOnClickListener(this);
		
		initActivity();							
	}
		
	/**
	 * Initialize the activity with the main actions like get the BlockCall 
	 * object with all its content.
	 */
	private void initActivity () {
		
		try {						
			
			if (ConfigAppValues.getBlockCallsConf() == null) {
				
				ClientDDBB clientDDBB = new ClientDDBB();
				BlockCallsConf blockCallsConf =
					clientDDBB.getSelects().selectBlockCallConf();
				
				/* This is for if both objects ConfigAppValues.blockCallsObj
				 * and blockCallsObj that must be stored in the database are null,
				 * that is to say, when no exists none blockCallsObj object, so
				 * this is only for the 1st time where the user enters in this screen
				 */				
				if (blockCallsConf == null) {
					blockCallsConf = new BlockCallsConf();
					clientDDBB.getInserts().insertBlockCallsConf(blockCallsConf);
					clientDDBB.commit();
				}
				
				ConfigAppValues.setBlockCallsConf(blockCallsConf);			
				clientDDBB.close();
			}						
			
			switch (ConfigAppValues.getBlockCallsConf().whatIsInUse()) {			
				case 0:
					break;
				case 1:
					blockAll.setChecked(true);
					break;
				case 2:
					blockBlockedContacts.setChecked(true);
					break;			
				case 3:
					blockUnknown.setChecked(true);
					break;
				case 4:
					blockUnknownAndBlockedContacts.setChecked(true);
					break;				
			}						
						
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	@Override
	public void onResume () {				
		
		super.onResume();
		initActivity();					
	}
	
	@Override
	public void onClick (View view) {
		
		int viewId = view.getId();
		
		ClientDDBB clientDDBB = new ClientDDBB();
		
		//Here, the object always should be created previously. 
		BlockCallsConf blockCallsConf = clientDDBB.getSelects().selectBlockCallConf();
		
		
		switch (viewId) {
			
			case blockAllId:
				blockCallsConf.setBlockAll(true);
				break;	
			case blockBlockedContactsId:
				blockCallsConf.setBlockBlockedContacts(true);
				break;
			case blockUnknownId:
				blockCallsConf.setBlockUnknown(true);
				break;
			case blockUnknownAndBlockedContactsId:
				blockCallsConf.setBlockUnknownAndBlockedContacts(true);
				break;
		}
						
		clientDDBB.getUpdates().insertBlockCallsConf(blockCallsConf);
		
		ConfigAppValues.setBlockCallsConf(blockCallsConf);
		clientDDBB.commit();
		clientDDBB.close();					
	}
	
}
