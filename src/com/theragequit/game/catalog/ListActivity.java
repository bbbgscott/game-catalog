package com.theragequit.game.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ListActivity extends ExpandableListActivity {
	ExpandableListView elv;
	Button okBtn;
	
	protected Object mActionMode;
	
	private GameOpenHelper dbHelper;
	public ArrayList<String> actives;
	public SimpleExpandableListAdapter expListAdapter;
	public PopupWindow popUp;
	LinearLayout layout;
	LayoutParams params;
	ImageView image;
	
	private PopupWindow popWindow;
	private ImageView popImage;
	private TextView popTitle;
	private TextView popProducer;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		dbHelper = new GameOpenHelper(this);
		dbHelper.open();
		// Setup pop up window and layout
		popUp = new PopupWindow(this);
		layout = new LinearLayout(this);
		// Setup ArrayList for active consoles
		actives = new ArrayList<String>();
		// Setup ExpandableListView
		expListAdapter = 
				new SimpleExpandableListAdapter(
						this,
						createGroupList(),
						R.layout.group_row,
						new String[]{ "system" },
						new int[] { R.id.groupname },
						createChildList(),
						R.layout.child_row,
						new String[] { "title" },
						new int[] { R.id.childname });
		
		setListAdapter(expListAdapter);
		
		image = new ImageView(this);
		//layout.setOrientation(LinearLayout.VERTICAL);
		//layout.addView(image, params);
		
		View v = findViewById(android.R.id.list);
		registerForContextMenu(v);
		getExpandableListView().setOnItemLongClickListener(new OnItemLongClickListener(){
			
			public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id){
				if(ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
					int groupPosition = ExpandableListView.getPackedPositionGroup(id);
					int childPosition = ExpandableListView.getPackedPositionChild(id);
					
					if(mActionMode != null){
						return false;
					}
					
					mActionMode = ListActivity.this.startActionMode(mActionModeCallback);
					((ActionMode) mActionMode).setTag(id);
					return true;
				}
				return false;
			}
		});
	}
	
	private List createGroupList(){
		ArrayList result = new ArrayList();
		String[] temp = getResources().getStringArray(R.array.consoleArray);
		for(int i = 0; i < temp.length; i++){
			if(!dbHelper.isEmpty(temp[i].toString())){
				String t = temp[i].toString();
				actives.add(t);
				HashMap m = new HashMap();
				m.put("system", temp[i]);
				result.add(m);
			}
		}
		return result;
	}

	private List createChildList(){
		ArrayList result = new ArrayList();
		// Loop through active tables
		for(String s: actives){
			ArrayList secList = new ArrayList();
			Cursor c = dbHelper.getGames(s);
			if(!dbHelper.isEmpty(s)){
				c.moveToFirst();
				while(!c.isAfterLast()){
					HashMap h = new HashMap();
					h.put("console", s);
					h.put("title", c.getString(c.getColumnIndex("Title")));
					h.put("barcode", c.getString(c.getColumnIndex("Barcode")));
					h.put("image", c.getString(c.getColumnIndex("Image")));
					h.put("producer", c.getString(c.getColumnIndex("Producer")));
					secList.add(h);
					c.moveToNext();
				}
			}
			result.add(secList);
		}
		return result;
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id){
		
		LinearLayout layout = new LinearLayout(ListActivity.this);
		HashMap h = (HashMap) expListAdapter.getChild(groupPosition, childPosition);
		
		LayoutInflater layInf = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popView = layInf.inflate(R.layout.popup, (ViewGroup)findViewById(R.id.pop_elem));
		popWindow = new PopupWindow(
				popView,
				450,
				700,
				true);
		popWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
		
		popImage = (ImageView)popView.findViewById(R.id.popupImage);
		popImage.setMaxWidth(400);
		popImage.setMinimumWidth(200);
		popImage.setMaxHeight(400);
		popImage.setMinimumHeight(200);
		
		popTitle = (TextView)popView.findViewById(R.id.popupTitle);
		popTitle.setText("Title: " + h.get("title").toString());
		
		popProducer = (TextView)popView.findViewById(R.id.popupProducer);
		popProducer.setText("Producer: " + h.get("producer").toString());

		okBtn = (Button)popView.findViewById(R.id.popupBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("list", "Destroy popup");
				popWindow.dismiss();
			}
			
		});
		String i = (String) h.get("image");
		popImage.setImageURI(null);
		ImageDown down = new ImageDown(popImage);
		down.execute(i);
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list, menu);
		return true;
	}
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
		public boolean onCreateActionMode(ActionMode mode, Menu menu){
			MenuInflater inf = mode.getMenuInflater();
			inf.inflate(R.menu.context_menu, menu);
			return true;
		}
		
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
		
		public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
			
			long id = (Long) mode.getTag();
			int groupPos = ExpandableListView.getPackedPositionGroup(id);
			int childPos = ExpandableListView.getPackedPositionChild(id);
			HashMap h = (HashMap) expListAdapter.getChild(groupPos, childPos);
			final String title = (String)h.get("title");
			final String barcode = (String)h.get("barcode");
			final String console = (String)h.get("console");
			switch(item.getItemId()){
			case R.id.edit:
				Game g = dbHelper.getOneGame(barcode, console);
				g.system = console;
				Intent i = new Intent();
				Bundle b = new Bundle();
    			b.putParcelable("game", g);
    			i.putExtras(b);
    			i.setClass(ListActivity.this, UpdateActivity.class);
    			startActivity(i);
    			mode.finish();
				Log.d("list", "You want to edit this item");
				return true;
			
			case R.id.delete:
				Log.d("list", "you want to delete this item");
				AlertDialog alertDialog = new AlertDialog.Builder(ListActivity.this).create();
				alertDialog.setTitle("Delete Title?");
				alertDialog.setMessage("Are you sure you want to delete " + title + "?");
				alertDialog.setButton(-2, "Cancel", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
						mode.finish();
					}
				});
				alertDialog.setButton(-3, "OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dbHelper.deleteGame(barcode);
						dialog.dismiss();
						mode.finish();
						
					}
				});
				alertDialog.show();
				return true;
				
			default:
				return false;
			}
		}
		
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	};
	

}
