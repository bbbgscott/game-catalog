package com.theragequit.game.catalog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONException;
//import net.sf.json.JSONObject;
//import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.*;

public class Game_Catalog_Activity extends Activity {
	
	Button addGameBtn;
	Button addMovieBtn;
	Button showGamesBtn;
	Button showMoviesBtn;
	
	Spinner gameSpinner;
	Spinner movieSpinner;
	
	ProgressBar div1;
	ProgressBar div2;
	
	final static String key = "AIzaSyDWv6d2u7_0N9MV_RE8s5j34b6VPk-gv_s";
	final static String url1 = "https://www.googleapis.com/shopping/search/v1/public/products?key=";
	public String gameSelect;
	public String movieSelect;
	public boolean game_flag = false;
	public boolean movie_flag = false;
	
	private GameOpenHelper dbHelper;
	
	DefaultHttpClient client;
	HttpsURLConnection urlcon;
	JSONObject json;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new GameOpenHelper(this);
        dbHelper.open();
        setContentView(R.layout.activity_game_catalog);
        
        gameSpinner = (Spinner)findViewById(R.id.spinConsole);
        ArrayAdapter<CharSequence> gameAdapter = ArrayAdapter.createFromResource(
        		this, R.array.consoleArray, android.R.layout.simple_spinner_item);
        gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSpinner.setAdapter(gameAdapter);
        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> p, View v, int pos, long id){
        		gameSelect = p.getItemAtPosition(pos).toString();
        	}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
        
        addGameBtn = (Button)findViewById(R.id.addGame);
        addGameBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
        		Log.d("myapp", gameSelect);
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				startActivityForResult(intent, 0);
			}
		});
        
        movieSpinner = (Spinner)findViewById(R.id.spinMovie);
        ArrayAdapter<CharSequence> movieAdapter = ArrayAdapter.createFromResource(
        		this, R.array.movieArray, android.R.layout.simple_spinner_item);
        movieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        movieSpinner.setAdapter(movieAdapter);
        movieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
				movieSelect = p.getItemAtPosition(pos).toString();
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
			});
        
        addMovieBtn = (Button)findViewById(R.id.addMovie);
        addMovieBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Log.d("myapp", movieSelect);
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				startActivityForResult(intent, 1);
			}
		});
        
        showGamesBtn = (Button)findViewById(R.id.showGames);
        showGamesBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(Game_Catalog_Activity.this, ListActivity.class);
				startActivity(i);
			}
		});
        
        showMoviesBtn = (Button)findViewById(R.id.showMovies);
        showMoviesBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(Game_Catalog_Activity.this, ListActivity.class);
				startActivity(i);
				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game_catalog, menu);
        return true;
    }

public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		Log.d("myapp", "Made it to checkpoint 2, request is " + requestCode);
		switch(requestCode){
		case 0: // It's a Game
			if(resultCode == RESULT_OK){
    			String contents = intent.getStringExtra("SCAN_RESULT");
    			String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
    			
    			JSONObject game = null;
    			Game game2 = new Game();
    			
    			RetreiveGame recon = new RetreiveGame();
    			recon.execute(contents);
    			try {
					game2 = recon.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
    			game2.barcode = contents;
    			
    			Log.d("myapp", "recon: " + recon.toString());
    			Log.d("myapp", "title: " + game2.title);
    			Log.d("myapp", "producer: " + game2.producer);
    			Log.d("myapp", "image: " + game2.image.toString());
				
    			try {
					game = new JSONObject(recon.toString());
				} catch (org.json.JSONException e) {
					e.printStackTrace();
				}

    			String title = "";
    			
    			AlertDialog alertDialog = new AlertDialog.Builder(Game_Catalog_Activity.this).create();
    			alertDialog.setTitle("Game Info");
    			//alertDialog.setMessage(contents + "\n" + format);
    			alertDialog.setMessage("Title: " + title);
    			alertDialog.setButton(-3, "OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
    			Intent i = new Intent(Intent.ACTION_SEARCH);
    			i.setClassName("com.google.android.apps.shopper",
    			"com.google.android.apps.shopper.results.SearchResultsActivity");
    			i.putExtra(SearchManager.QUERY, contents);
    			
    			game2.system = gameSelect;
    			Intent in = new Intent();
    			Bundle b = new Bundle();
    			b.putParcelable("game", game2);
    			in.putExtras(b);
    			in.setClass(this, VerifyActivity.class);
    			startActivity(in);
    			
    			//dbHelper.addGame(game2, gameSelect);
    			
    		} else if(resultCode == RESULT_CANCELED){
    			// Handle cancel
    		}
			break;
		case 1: // It's a Movie
			if(resultCode == RESULT_OK){
    			String contents = intent.getStringExtra("SCAN_RESULT");
    			String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
    			
    			JSONObject movie = null;
    			Movie mov = new Movie();
    			
    			RetreiveMovie recon = new RetreiveMovie();
    			recon.execute(contents);
    			try {
					mov = recon.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
    			mov.barcode = contents;
    			
    			AlertDialog alertDialog = new AlertDialog.Builder(Game_Catalog_Activity.this).create();
    			alertDialog.setTitle("Movie Barcode");
    			alertDialog.setMessage(contents + "\n" + format);
    			alertDialog.setButton(-3, "OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
    			//alertDialog.setPositiveButton("BUTTON_NEUTRAL", "OK", alertDialog.dismiss());
    			alertDialog.show();
    		} else if(resultCode == RESULT_CANCELED){
    			// Handle cancel
    		}
			break;
		case 2:
			if(resultCode == RESULT_OK){
				String contents = intent.getStringExtra("SCAN_RESULT");
				String result = url1 + key + "&country=US&q=" + contents + "&alt=json";
			}
		}
    }

	public JSONObject products(String upc){
		
		HttpResponse r = null;
		String data = null;
		String jsonTxt = null;
		StringBuilder body = null;
		String line = null;
		String text = null;
		
		String url = url1 + key + "&country=US&q=" + upc + "&alt=json";
		URL url2 = null;
		Log.d("myapp", "checkpoint 4, url is set");
		
		try{
			HttpClient client = new DefaultHttpClient();
			Log.d("myapp", "4a, client initiated");
			HttpGet request = new HttpGet();
			Log.d("myapp", "4b, request set");
			request.setURI(new URI(url));
			Log.d("myapp", "4c, uri set");
			r = client.execute(request);
			Log.d("myapp", "checkpoint 4d, http stuff executed");
		} catch (URISyntaxException e){
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = r.getEntity().getContent();
		} catch (IllegalStateException e3) {
			e3.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		String shell = convertStreamToString(is);
		JSONObject timeline = null;
		try {
			timeline = new JSONObject(shell);
		} catch (org.json.JSONException e) {
			e.printStackTrace();
		}
		return timeline;
	}
	
	public static String convertStreamToString(InputStream input) {
		if(input != null){
			Writer writer = new StringWriter();
			
			char[] buffer = new char[1024];
			try{
				Reader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 1024);
				int n;
				while((n = reader.read(buffer)) != -1){
					writer.write(buffer, 0, n);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return writer.toString();
		} else {
			return "";
		}
		
		
	}

	public static String ReadInputStream(InputStream in) throws IOException {
		StringBuffer stream = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			stream.append(new String(b, 0, n));
		}
		return stream.toString();
	}
	
	public String deal_with_content(HttpsURLConnection con){
		if(con != null){
			try{
				BufferedReader br;
				InputStreamReader is;
				InputStream in = null;
				Log.d("myapp", "con: " + con.toString());
				try{
					in = con.getInputStream();
				}catch(UnknownServiceException e){
					Log.d("myapp", "UnknownServiceException");
					e.printStackTrace();
				}
				is = new InputStreamReader(in);
				br = new BufferedReader(is);
				
				String line;
				StringBuilder body = null;
				while((line = br.readLine()) != null){
					body.append(line);
				}
				br.close();
				return body.toString();
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return null;
	}
}

class RetreiveGame extends AsyncTask<String, Void, Game>{
	private Exception exception;
	
	final static String key = "AIzaSyDWv6d2u7_0N9MV_RE8s5j34b6VPk-gv_s";
	final static String url1 = "https://www.googleapis.com/shopping/search/v1/public/products?key=";

	@Override
	protected Game doInBackground(String... urls) {
		HttpResponse r = null;
		
		String url = url1 + key + "&country=US&q=" + urls[0] + "&alt=json";
		URL url2 = null;
		Log.d("myapp", "checkpoint 4, url is set");

		Game game = new Game();
		JSONObject timeline = null;
		JSONArray items = null;
		JSONArray images = null;
		JSONObject link = null;
		JSONObject prod = null;
		JSONObject prod2 = null;
		String brand = null;
		String title = null;
		String pic = null;
		Uri image = null;
		
		try{
			HttpClient client = new DefaultHttpClient();
			Log.d("myapp", "4a, client initiated");
			HttpGet request = new HttpGet();
			Log.d("myapp", "4b, request set");
			request.setURI(new URI(url));
			Log.d("myapp", "4c, uri set");
			r = client.execute(request);
			Log.d("myapp", "checkpoint 4d, http stuff executed");
		} catch (URISyntaxException e){
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = r.getEntity().getContent();
			Log.d("myapp", "response is set");
		} catch (IllegalStateException e3) {
			e3.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		String shell = convertStreamToString(is);
		Log.d("myapp", "shell is set");
		
		try {
			timeline = new JSONObject(shell);
		} catch (org.json.JSONException e) {
			e.printStackTrace();
		}
		
		try {
			items = (JSONArray) timeline.get("items");
		} catch (org.json.JSONException e) {
			e.printStackTrace();
		}
		if(items != null){
			Log.d("myapp", "title works");
		}else{
			Log.d("myapp", "item is null");
		}
		int i = 0;
		while(i <= items.length()){
			
			try {
				prod = items.getJSONObject(i);
			} catch (org.json.JSONException e) {
				e.printStackTrace();
			}
			if(prod != null){
				//Log.d("myapp", "title: " + prod);
			}else{
				Log.d("myapp", "prod is null");
			}
			
			try {
				prod2 = prod.getJSONObject("product");
			} catch (org.json.JSONException e) {
				e.printStackTrace();
			}
			try {
				images = (JSONArray)prod2.get("images");
			} catch(org.json.JSONException e) {
				e.printStackTrace();
			}
			try {
				link = (JSONObject)images.getJSONObject(0);
				pic = link.optString("link");
			} catch (org.json.JSONException e1) {
				e1.printStackTrace();
			}
			//Log.d("myapp", "prod2 = " + prod2);
			if(title == null || title == ""){
				title = prod2.optString("title");
			}
			if(brand == null || brand == ""){
				brand = prod2.optString("brand");
			}
			if(image == null || image == Uri.parse("")){
				try {
					String temp = prod2.getJSONObject("images").optString("link");
					Log.d("myapp", "link: " + temp);
					image = Uri.parse(prod2.getJSONObject("images").optString("link"));
				} catch (org.json.JSONException e) {
					e.printStackTrace();
				}
			}
			//Log.d("myapp", "title is " + (title == "" ? "" : "not ") + "null: " + title);
			//Log.d("myapp", "brand is " + (brand == "" ? "" : "not ") + "null: " + brand);
			//Log.d("myapp", "i is " + i);
			i++;
		}
		
		//Log.d("myapp", "name: " + title);
		game.setTitle(title);
		//Log.d("myapp", "brand: " + brand);
		game.setProducer(brand);
		game.setImage(Uri.parse(pic));
		/*if(image != null){
			Log.d("myapp", "image got something");
			game.setImage(image);
		} else {
			Log.d("myapp", "image got nothing");
		}*/
		Log.d("myapp", "pic is " + (pic == "" ? "" : "not ") + "null: " + pic);
		return game;
	}
	
	public static String convertStreamToString(InputStream input) {
		if(input != null){
			Writer writer = new StringWriter();
			
			char[] buffer = new char[1024];
			try{
				Reader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 1024);
				int n;
				while((n = reader.read(buffer)) != -1){
					writer.write(buffer, 0, n);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return writer.toString();
		} else {
			return "";
		}
	}
	protected void onPostExecute(Game g){
		try {
			g = this.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
