package com.theragequit.game.catalog;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class GameOpenHelper{
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	// Important database information
	private static final String DATABASE_NAME = "Collections";
	private static final int DATABASE_VERSION = 1;
	// Field names
	private static final String FIELD_0 = "ID";
	private static final String FIELD_1 = "Title";
	private static final String FIELD_2 = "Producer";
	private static final String FIELD_3 = "Image";
	private static final String FIELD_4 = "Barcode";
	//private static final String FIELD_3 = "Image";
	// Total table creation

	private static final String GAME_TABLE_CREATE_1 = 
			"CREATE TABLE IF NOT EXISTS Gamecube (" + FIELD_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			   FIELD_1 + " TEXT, " +
			   FIELD_2 + " TEXT, " +
			   FIELD_3 + " TEXT, " +
			   FIELD_4 + " INTEGER);";
	private static final String GAME_TABLE_CREATE_2 = 
			"CREATE TABLE IF NOT EXISTS Playstation (" + FIELD_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			   FIELD_1 + " TEXT, " +
			   FIELD_2 + " TEXT, " +
			   FIELD_3 + " TEXT, " +
			   FIELD_4 + " INTEGER);";
	private static final String GAME_TABLE_CREATE_3 = 
			"CREATE TABLE IF NOT EXISTS Playstation_2 (" + FIELD_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			   FIELD_1 + " TEXT, " +
			   FIELD_2 + " TEXT, " +
			   FIELD_3 + " TEXT, " +
			   FIELD_4 + " INTEGER);";
	private static final String GAME_TABLE_CREATE_4 = 
			"CREATE TABLE IF NOT EXISTS Playstation_3 (" + FIELD_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			   FIELD_1 + " TEXT, " +
			   FIELD_2 + " TEXT, " +
			   FIELD_3 + " TEXT, " +
			   FIELD_4 + " INTEGER);";
	private static final String GAME_TABLE_CREATE_5 = 
			"CREATE TABLE IF NOT EXISTS Wii (" + FIELD_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			   FIELD_1 + " TEXT, " +
			   FIELD_2 + " TEXT, " +
			   FIELD_3 + " TEXT, " +
			   FIELD_4 + " INTEGER);";
	private static final String GAME_TABLE_CREATE_6 = 
			"CREATE TABLE IF NOT EXISTS Wii_U (" + FIELD_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			   FIELD_1 + " TEXT, " +
			   FIELD_2 + " TEXT, " +
			   FIELD_3 + " TEXT, " +
			   FIELD_4 + " INTEGER);";
	private static final String GAME_TABLE_CREATE_7 = 
			"CREATE TABLE IF NOT EXISTS Xbox (" + FIELD_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			   FIELD_1 + " TEXT, " +
			   FIELD_2 + " TEXT, " +
			   FIELD_3 + " TEXT, " +
			   FIELD_4 + " INTEGER);";
	private static final String GAME_TABLE_CREATE_8 = 
			"CREATE TABLE IF NOT EXISTS Xbox_360 (" + FIELD_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			   FIELD_1 + " TEXT, " +
			   FIELD_2 + " TEXT, " +
			   FIELD_3 + " TEXT, " +
			   FIELD_4 + " INTEGER);";
	
	private final Context ctx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context ctx){
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db){
			db.execSQL(GAME_TABLE_CREATE_1);
			db.execSQL(GAME_TABLE_CREATE_2);
			db.execSQL(GAME_TABLE_CREATE_3);
			db.execSQL(GAME_TABLE_CREATE_4);
			db.execSQL(GAME_TABLE_CREATE_5);
			db.execSQL(GAME_TABLE_CREATE_6);
			db.execSQL(GAME_TABLE_CREATE_7);
			db.execSQL(GAME_TABLE_CREATE_8);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			db.execSQL("DROP TABLE IF EXISTS");
			onCreate(db);
		}
	}
	
	public GameOpenHelper(Context context){
		this.ctx = context;
		//super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public GameOpenHelper open() throws SQLException{
		dbHelper = new DatabaseHelper(ctx);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbHelper.close();
	}
	
	// Add a game
	public void addGame(Game game){
		//SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FIELD_1, game.getTitle());
		values.put(FIELD_2, game.getProducer());
		values.put(FIELD_3, game.getImage().toString());
		values.put(FIELD_4, game.getBarcode());
		
		if(checkExists(game)){
			Log.d("db", "game already exists");
		} else {
			db.insert(game.system, null, values);
		}
	}
	// Check if a game exists
	public boolean checkExists(Game game){
		String query = "SELECT * FROM " + game.system + " WHERE " + FIELD_4 + " = '" + game.barcode + "'";
		Cursor c = null;
		if(db != null){
			Log.d("db", "consle = " + game.system);
			Log.d("db", "title = " + game.title);
			Log.d("db", "barcode = " + game.barcode);
			c = db.query(game.system, null, game.barcode + "=" + FIELD_4, null, null, null, null, null);
		}
		if(c.moveToFirst()){
			return true;
		} else {
			return false;
		}
	}
	// Check if a table is populated
	public boolean isEmpty(String table){
		String query = "SELECT 8 FROM " + table;
		Cursor c = null;
		if(db != null){
			c = db.query(table, null, null, null, null, null, null, null);
		}
		if(c.moveToFirst()){
			return false;
		} else {
			return true;
		}
	}
	// Return all games
	public Cursor getGames(String table){
		return db.query(table, new String[] {FIELD_1, FIELD_2, FIELD_3, FIELD_4}, null, null, null, null, "Title ASC", null);
	}
	// Return one game
	public Game getOneGame(String code, String table){
		Log.d("db", table);
		Cursor c = db.query(table, null, "Barcode = '" + code + "'", null, null, null, null, null);
		Game game = new Game();
		if(c.moveToFirst()){
			String tit = c.getString(c.getColumnIndex("Title"));
			game.title	= tit;
			game.producer = c.getString(c.getColumnIndex("Producer"));	
			game.image = Uri.parse(c.getString(c.getColumnIndex("Image")));	
			game.barcode = c.getString(c.getColumnIndex("Barcode"));
		}
		
		return game;
	}
	// Return the number of games in the db
	public int getGamesCount(){
		return 0;
	}
	// Update a single game
	public boolean updateGame(Game game){
		ContentValues args = new ContentValues();
		args.put("Title", game.title);
		args.put("Producer", game.producer);
		if(db.update(game.system, args, "Barcode = '" + game.barcode + "'", null) == 1){
			return true;
		} else {
			return false;
		}
	}
	// Delete a single game from the db
	public void deleteGame(String code){
		
	}
}
