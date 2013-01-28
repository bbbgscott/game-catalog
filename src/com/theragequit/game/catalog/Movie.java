package com.theragequit.game.catalog;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
	
	String barcode;
	String title;
	String format;
	Uri image;

	public Movie(){
		this.barcode = "";
		this.title = "";
		this.format = "";
		this.image = null;
	}
	
	public Movie(String barcode, String title, String format, Uri image){
		this.barcode = barcode;
		this.title = title;
		this.format = format;
		this.image = image;
	}
	
	public Movie(Parcel in){
		readFromParcel(in);
	}
	
	public String getBarcode(){
		return this.barcode;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getFormat(){
		return this.format;
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
	
	public void setFormat(String format){
		this.format = format;
	}
	
	public void setImage(Uri image){
		this.image = image;
	}
	
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(barcode);
		out.writeString(title);
		out.writeString(format);
		out.writeString(image.toString());
		
	}
	
	public void readFromParcel(Parcel in){
		barcode = in.readString();
		title = in.readString();
		format = in.readString();
		image = Uri.parse(in.readString());
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
		public Movie createFromParcel(Parcel in){
			return new Movie(in);
		}
		
		public Movie[] newArray(int size){
			return new Movie[size];
		}
	};
	
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
