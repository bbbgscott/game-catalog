package com.theragequit.game.catalog;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
	// Variables
	String barcode;
	String title;
	String producer;
	Uri image;
	String system;
	
	public Game(){
		this.title = "";
		this.producer = "";
		this.image = null;
		this.system = "";
	}
	
	public Game(String barcode, String title, String producer, Uri image){
		this.barcode = barcode;
		this.title = title;
		this.producer = producer;
		this.image = image;
	}
	
	public Game(Parcel in){
		readFromParcel(in);
	}
	
	public String getBarcode(){
		return this.barcode;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getProducer(){
		return this.producer;
	}
	
	public Uri getImage(){
		return this.image;
	}
	
	public void setBarcode(String barcode){
		this.barcode = barcode;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setProducer(String producer){
		this.producer = producer;
	}
	
	public void setImage(Uri image){
		this.image = image;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(barcode);
		out.writeString(title);
		out.writeString(producer);
		out.writeString(image.toString());
		out.writeString(system);
		
	}
	
	public void readFromParcel(Parcel in){
		barcode = in.readString();
		title = in.readString();
		producer = in.readString();
		image = Uri.parse(in.readString());
		system = in.readString();
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
		public Game createFromParcel(Parcel in){
			return new Game(in);
		}
		
		public Game[] newArray(int size){
			return new Game[size];
		}
	};
}
