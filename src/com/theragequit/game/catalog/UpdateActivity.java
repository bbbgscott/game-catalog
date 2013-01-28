package com.theragequit.game.catalog;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UpdateActivity extends Activity {

	ImageView image;
	EditText titleText;
	EditText pubText;
	Button backBtn;
	Button okBtn;
	
	Game game;
	
	private GameOpenHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify);
		Bundle b = this.getIntent().getExtras();
		if(b != null){
			game = b.getParcelable("game");
		}

		dbHelper = new GameOpenHelper(this);
		dbHelper.open();
		
		image = (ImageView)findViewById(R.id.imageView1);
		titleText = (EditText)findViewById(R.id.gameTitle);
		pubText = (EditText)findViewById(R.id.gamePublisher);
		backBtn = (Button)findViewById(R.id.verifyBack);
		okBtn = (Button)findViewById(R.id.verifyOk);
		
		image.setImageURI(null);
		ImageDown down = new ImageDown(image);
		down.execute(game.image.toString());
		titleText.append(game.title);
		pubText.append(game.producer);
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
		
		final Context ctx = this;
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				game.title = titleText.getText().toString();
				game.producer = pubText.getText().toString();
				
				dbHelper.updateGame(game);
				finish();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_update, menu);
		return true;
	}

}

