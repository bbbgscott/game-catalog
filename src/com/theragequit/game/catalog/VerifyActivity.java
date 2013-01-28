package com.theragequit.game.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class VerifyActivity extends Activity {

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
		//image.setImageURI(game.image);
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
				if(dbHelper.checkExists(game)){
					AlertDialog alertDialog = new AlertDialog.Builder(VerifyActivity.this).create();
					alertDialog.setTitle("Duplicate Content");
					alertDialog.setMessage("There is already an entry for " + game.title);
					alertDialog.setButton(-3, "OK", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							
						}
					});
					alertDialog.show();
				} else {
					dbHelper.addGame(game);
					AlertDialog alertDialog = new AlertDialog.Builder(VerifyActivity.this).create();
					alertDialog.setTitle("Game Added");
					alertDialog.setMessage(game.title + " has been added to your database.");
					alertDialog.setButton(-3, "OK", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
							
						}
					});
					alertDialog.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_verify, menu);
		return true;
	}
	
	public Object fetch(String address) throws MalformedURLException, IOException{
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

}

class ImageDown extends AsyncTask<String, Void, Bitmap> {
	
	HttpClient client = new DefaultHttpClient();
	HttpGet get;
	HttpResponse response;

	private final WeakReference<ImageView> ivr;
	
	public ImageDown(ImageView iv){
		ivr = new WeakReference<ImageView>(iv);
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		String url = params[0];
		InputStream is = null;
		try{
			return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
		} catch(MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Bitmap result){
		if(isCancelled()){
			result = null;
		}
		if(ivr != null){
			ImageView iv = ivr.get();
			if(iv != null){
				iv.setImageBitmap(result);
			}
		}
	}
	
}
