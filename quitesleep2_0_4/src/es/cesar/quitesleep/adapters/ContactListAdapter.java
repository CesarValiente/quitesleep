/* 
 	Copyright 2011 Cesar Valiente Gordo
 
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

package es.cesar.quitesleep.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import es.cesar.quitesleep.R;


/**
 * This class is the adapter to use in the contact list, both the add contacts to ban list, and to remove contacts
 * from this one.
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 * @param <T>
 */
public class ContactListAdapter<T> extends ArrayAdapter<T> implements SectionIndexer {

		final String CLASS_NAME = getClass().getName();
	
		Activity activity;
		List<String> contactListString;
		HashMap<String, Integer> alphaIndexer;
		String[] sections;
		
		
		/**
		 * Constructor
		 * @param context
		 * @param viewResourceId
		 * @param objects
		 */
		public ContactListAdapter (Context context, int viewResourceId, List<T> objects, Activity activity) {
			
			super (context, viewResourceId, objects);
			
			this.activity= activity;
			
			//Gets the elements passed by param
			contactListString = (ArrayList<String>)objects;
			
			//We order the contact list string
			Collections.sort(contactListString);
			
			Log.d(CLASS_NAME, "contact list string: " + contactListString);
			
			//Init the indexer to use in the list
			alphaIndexer = new HashMap<String, Integer>();
			
			//We get the first letter of each item, but as is a hashmap we get only the last one of them
			int size = contactListString.size();
			for (int i=size-1; i>=0; i--) {				
				String item = contactListString.get(i);
				alphaIndexer.put(item.substring(0,1), i);				
			}
			
			Log.d(CLASS_NAME, "alphaIndexer: " + alphaIndexer);
			
			//Creates the keylist with the first letter
			List<String> keyList = new ArrayList<String>(alphaIndexer.size());
			for (String key : alphaIndexer.keySet()) {
				keyList.add(key);
			}
			
			//We order it
			Collections.sort(keyList);
			
			//We create the array with the keys
			sections = new String[keyList.size()];
			keyList.toArray(sections);			
			
			Log.d(CLASS_NAME, "sections: " + sections);
		}
		
		@Override
		public View getView (int position, View convertView, ViewGroup parent) {
			
			View view;
			
			TextView textview;
			
			Log.d(CLASS_NAME, "get view!!!!");
			
			if (convertView == null)
				view = activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
			else
				view = convertView;
			
			try {
				textview = (TextView)view;
			}catch (ClassCastException e) {
				throw new IllegalStateException(e.toString(), e);
			}
			
			//Gets the item in the actual position and casting it
			T item = getItem(position);
			textview.setText(item.toString());
			Log.d(CLASS_NAME, "Item: " + item.toString());
			
			return view;
		}
		
		
		@Override
		public Object[] getSections() {
			return sections;
		}

		@Override
		public int getPositionForSection(int section) {
			
			String letter = sections[section];
			return alphaIndexer.get(letter);
		}

		@Override
		public int getSectionForPosition(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		
	}