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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import es.cesar.quitesleep.R;
import es.cesar.quitesleep.data.controllers.ClientDDBB;
import es.cesar.quitesleep.data.models.Contact;
import es.cesar.quitesleep.data.models.Mail;
import es.cesar.quitesleep.data.models.Phone;
import es.cesar.quitesleep.operations.ContactOperations;
import es.cesar.quitesleep.settings.ConfigAppValues;
import es.cesar.quitesleep.ui.activities.base.BaseFragmentActivity;
import es.cesar.quitesleep.utils.ExceptionUtils;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class EditContact extends BaseFragmentActivity implements OnClickListener {
	
	final private String CLASS_NAME = getClass().getName();
	
	//Global widgets
	private ScrollView scrollView;
	private LinearLayout linearLayout;	
	
	//Phone and mail list for dynamic checkbox
	private List<CheckBox> phoneCheckboxList;
	private List<CheckBox> mailCheckboxList;
			
	/* The contact Name selected in parent and caller activity when the user
	 * selectd clicking in it. 
	 */
	private String selectContactName;
		
	//Ids for button widgets
	private final int removeContactButtonId 	= 	1;
	private final int editContactButtonId		=	2;		
	
	//Ids for colors
	private final int backgroundColor 		=	R.color.black;
	private final int textColor 			=	R.color.black; 

	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		super.homeToUp(true);
		
		initDynamicLayout();				
	}
	
	
	/**
	 * Create and initialize the dynamic layout and put some widgets on it
	 */
	private void initDynamicLayout () {
		
		try {
			
			/* Get the contactName passed from the parent activity to this, and
			 * use for get a Contact object refferenced to this name.
			 */
			selectContactName = getIntent().getExtras().getString(ConfigAppValues.CONTACT_NAME);
			
			ClientDDBB clientDDBB = new ClientDDBB();
			Contact contact = clientDDBB.getSelects().selectContactForName(selectContactName);
									
			if (contact != null && contact.isBanned()) {														
				
				createLayout();				
				createHeader();
															
				createPhoneNumbersSection(clientDDBB, contact);			
				createMailAddressesSection(clientDDBB, contact);
								
				addButtons();
				
				
				setContentView(scrollView);				
				clientDDBB.close();
				
			}else {
				clientDDBB.close();
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	/*
	 * Create the scrollView and LinearLayput for put some widgets on it
	 */
	private void createLayout () {
		
		try {
		
			scrollView = new ScrollView(this);						
			linearLayout = new LinearLayout(this);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			scrollView.setBackgroundColor(this.getResources().getColor(backgroundColor));
			
			scrollView.addView(linearLayout);						
						
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	/**
	 * Put the contactName and a separator view how header for the layout
	 */
	private void createHeader () {
		
		try {
			TextView contactName = new TextView(this);
			contactName.setText(selectContactName);
			//contactName.setTextColor(textColor);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 
					LinearLayout.LayoutParams.MATCH_PARENT);
			contactName.setLayoutParams(params);
			contactName.setTypeface(Typeface.create("", Typeface.BOLD_ITALIC));
			contactName.setTextSize(25);												
														
			linearLayout.addView(contactName);
			
			addDividerBlack();
			
						
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	/**
	 * Create the phone numbers section
	 * 
	 * @param clientDDBB
	 * @param contact
	 */
	private void createPhoneNumbersSection (ClientDDBB clientDDBB, Contact contact) {
		
		try {						
			
			List<Phone> contactPhones = 
				clientDDBB.getSelects().selectAllContactPhonesForName(selectContactName);
			
			if (contactPhones != null) {
				
				addDividerDetails(R.string.contactdetails_label_phonedetails);
				
				phoneCheckboxList = new ArrayList<CheckBox>(contactPhones.size());
				
				for (int i=0; i<contactPhones.size(); i++) {
					
					Phone phone = contactPhones.get(i);
					
					CheckBox checkbox = new CheckBox(this);
					checkbox.setText(phone.getContactPhone());
					checkbox.setChecked(phone.isUsedToSend());
					//checkbox.setTextColor(textColor);
										
					linearLayout.addView(checkbox);
					phoneCheckboxList.add(checkbox);
				}								
			}
							
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	/**
	 * Create the mail addresses section
	 * 
	 * @param clientDDBB
	 * @param contact
	 */
	private void createMailAddressesSection (ClientDDBB clientDDBB, Contact contact) {
		
		try {
			
			List<Mail> contactMails = 
				clientDDBB.getSelects().selectAllContactMailsForName(selectContactName);
			
			if (contactMails != null && contactMails.size() > 0) {
				
				addDividerDetails(R.string.contactdetails_label_maildetails);
				
				mailCheckboxList = new ArrayList<CheckBox>(contactMails.size());
												
				for (int i=0; i<contactMails.size(); i++) {
					
					Mail mail = contactMails.get(i);
					
					CheckBox checkbox = new CheckBox(this);
					checkbox.setText(mail.getContactMail());	
					checkbox.setChecked(mail.isUsedToSend());
					//checkbox.setTextColor(textColor);
					
					linearLayout.addView(checkbox);
					mailCheckboxList.add(checkbox);
				}
			}
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	/**
	 * Put one black dividerDetails view in the layout. 
	 * Pass the resId with the string that we like it. 
	 * @param resId
	 */
	private void addDividerDetails (int resId) {
		
		TextView details = new TextView(this);
		details.setBackgroundResource(R.drawable.solid_black);
		details.setTypeface(Typeface.create("", Typeface.BOLD));
		details.setTextSize(15);
		details.setText(resId);
		details.setPadding(0, 10, 0, 0);
		
		linearLayout.addView(details);
	}
	
	/**
	 * Put one black divider view in the layout as separator for other views
	 */
	private void addDividerBlack () {
		
		TextView dividerBlack = new TextView(this);
		dividerBlack.setBackgroundResource(R.drawable.gradient_black);			
		dividerBlack.setTextSize(3);
		
		linearLayout.addView(dividerBlack);					
	}
	
	/**
	 * Put one blue divider view in the layout as separator for other views
	 */
	private void addDividerBlue () {
		
		TextView dividerBlue = new TextView(this);
		dividerBlue.setBackgroundResource(R.drawable.gradient_blue);			
		dividerBlue.setTextSize(2);
		
		linearLayout.addView(dividerBlue);
	}
	
	
	/**
	 * Put two buttons, one for addContact to the banned list and other for
	 * cancel and go back to the parent activity.
	 */
	private void addButtons () {
		
		try {
			
			addDividerBlue();
			
			Button editButton = new Button(this);
			editButton.setText(R.string.editcontact_button_edit);
			editButton.setId(editContactButtonId);
			editButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
			editButton.setId(editContactButtonId);
			editButton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.common_button_selector));
			editButton.setTextColor(Color.WHITE);
			editButton.setTextSize(20);
			editButton.setOnClickListener(this);			
									
			linearLayout.addView(editButton);
			
			addDividerBlack();
						
			Button removeContactButton = new Button(this);
			removeContactButton.setText(R.string.editcontact_button_remove);
			removeContactButton.setId(removeContactButtonId);						
			removeContactButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delete, 0, 0, 0);
			removeContactButton.setId(removeContactButtonId);
			removeContactButton.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.common_button_selector));
			removeContactButton.setTextColor(Color.WHITE);
			removeContactButton.setTextSize(20);
			removeContactButton.setOnClickListener(this);
			
			linearLayout.addView(removeContactButton);						
											
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	@Override
	public void onClick (View view) {
				
		int viewId = view.getId();
		boolean result;
		
		switch (viewId) {
								
			case editContactButtonId:
				result = ContactOperations.editContact(
						selectContactName,
						phoneCheckboxList,
						mailCheckboxList);
				showEditToast(result);
				break;
				
			case removeContactButtonId:
				result = ContactOperations.removeContact(selectContactName);
				showRemoveToast(result);				
				setResult(Activity.RESULT_OK);
				finish();
				break;									
				
			default:
				break;
		}					
	}
	
	/**
	 * Show toast notification for information to user
	 * 
	 * @param result
	 */
	private void showEditToast (boolean result) {
		
		try {
			
			if (result) 
				es.cesar.quitesleep.utils.Toast.d(
                		this,
                		this.getString(
                				R.string.editcontact_toast_edit),
                		Toast.LENGTH_SHORT);		 
			else
				es.cesar.quitesleep.utils.Toast.d(
                		this,
                		this.getString(
                				R.string.editcontact_toast_edit_fail),
                		Toast.LENGTH_SHORT);		 
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}
	
	/**
	 * Show notification toast to the user for information.
	 * 
	 * @param result
	 */
	private void showRemoveToast (boolean result) {
		
		try {
			
			if (result) 
				es.cesar.quitesleep.utils.Toast.d(
                		this,
                		this.getString(
                				R.string.editcontact_toast_remove),
                		Toast.LENGTH_SHORT);		 
			else
				es.cesar.quitesleep.utils.Toast.d(
                		this,
                		this.getString(
                				R.string.editcontact_toast_remove_fail),
                		Toast.LENGTH_SHORT);		 
			
		}catch (Exception e) {
			Log.e(CLASS_NAME, ExceptionUtils.getString(e));
		}
	}

}
