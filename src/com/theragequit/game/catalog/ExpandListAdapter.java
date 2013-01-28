package com.theragequit.game.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ExpandableListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleExpandableListAdapter;

public class ExpandListAdapter extends ExpandableListActivity {
	
	private GameOpenHelper dbHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		SimpleExpandableListAdapter expListAdapter = 
				new SimpleExpandableListAdapter(
						this,
						createGroupList(),
						R.layout.child_row,
						new String[]{ "system" },
						new int[] { R.id.childname },
						createChildList(),
						R.layout.child_row,
						new String[] { "title" },
						new int[] { R.id.childname });
		
		dbHelper = new GameOpenHelper(this);
		dbHelper.open();
		
		setListAdapter(expListAdapter);
	}
	
	private List createGroupList(){
		ArrayList result = new ArrayList();
		String[] temp = getResources().getStringArray(R.array.consoleArray);
		for(int i = 0; i < temp.length; i++){
			HashMap m = new HashMap();
			m.put("system", temp[i]);
			result.add(m);
		}
		return result;
	}
	
	private List createChildList(){
		ArrayList result = new ArrayList();
		String[] temp = getResources().getStringArray(R.array.consoleArray);
		for(int i = 0; i < temp.length; i++){
			ArrayList secList = new ArrayList();
			for(int n = 0; n < temp[i].length(); n += 2){
				HashMap child = new HashMap();
				Cursor c = dbHelper.getGames(temp[i]);
				
				if(c != null){
					if(c.moveToFirst()){
						do{
							child.put("title", c.getString(c.getColumnIndex("Title")));
							secList.add(child);
						} while(c.moveToNext());
					}
				}
			}
			result.add(secList);
		}
		return result;
	}
}
