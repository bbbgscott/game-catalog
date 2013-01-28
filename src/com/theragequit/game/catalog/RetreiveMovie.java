package com.theragequit.game.catalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

class RetreiveMovie extends AsyncTask<String, Void, Movie>{
	private Exception exception;
	
	final static String key = "AIzaSyDWv6d2u7_0N9MV_RE8s5j34b6VPk-gv_s";
	final static String url1 = "https://www.googleapis.com/shopping/search/v1/public/products?key=";

	@Override
	protected Movie doInBackground(String... urls) {
		HttpResponse r = null;
		
		String url = url1 + key + "&country=US&q=" + urls[0] + "&alt=json";
		URL url2 = null;
		Log.d("myapp", "checkpoint 4, url is set");

		Movie mov = new Movie();
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
		mov.setTitle(title);
		//Log.d("myapp", "brand: " + brand);
		mov.setProducer(brand);
		mov.setImage(Uri.parse(pic));
		/*if(image != null){
			Log.d("myapp", "image got something");
			game.setImage(image);
		} else {
			Log.d("myapp", "image got nothing");
		}*/
		Log.d("myapp", "pic is " + (pic == "" ? "" : "not ") + "null: " + pic);
		return mov;
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
	protected void onPostExecute(Movie g){
		try {
			g = this.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
