
/**
 * @author bbbgscott
 *
 * I totally didn't shamelessly steal the base code from zxing.
 * Hopefully it's changed enough so that nobody can tell that I didn't take it from them.
 *//*
package com.theragequit.game.catalog;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.theragequit.Game_Catalog.HttpHelper;
import com.theragequit.Game_Catalog.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

final class NetworkWorker implements Runnable {

  private static final String TAG = NetworkWorker.class.getSimpleName();

  //Marked for removal
  //private final String isbn;
  private final String query;
  private final Handler handler;

  // Marked for removal
  /*NetworkWorker(String isbn, String query, Handler handler) {
    this.isbn = isbn;
    this.query = query;
    this.handler = handler;
  }*//*

  NetworkWorker(String query, Handler handler){
	  this.query = query;
	  this.handler = handler;
  }
  
  public void run() {
    try {
      // These return a JSON result which describes if and where the query was found. This API may
      // break or disappear at any time in the future. Since this is an API call rather than a
      // website, we don't use LocaleManager to change the TLD.
      String uri;
      /*if (LocaleManager.isBookSearchUrl(isbn)) {
        int equals = isbn.indexOf('=');
        String volumeId = isbn.substring(equals + 1);
        uri = "http://www.google.com/books?id=" + volumeId + "&jscmd=SearchWithinVolume2&q=" + query;
      } else {
        uri = "http://www.google.com/books?vid=isbn" + isbn + "&jscmd=SearchWithinVolume2&q=" + query;
      }*//*
      uri = "https://www.googleapis.com/shopping/search/v1/pulic/products?key=AIzaSyDWv6d2u7_0N9MV_RE8s5j34b6VPk-gv_s&country=US&q=" + query + "&alt=json";

      try {
        String content = HttpHelper.downloadViaHttp(uri, HttpHelper.ContentType.JSON);
        JSONObject json = new JSONObject(content);
        Message message = Message.obtain(handler, R.id.search_book_contents_succeeded);
        message.obj = json;
        message.sendToTarget();
      } catch (IOException ioe) {
        Message message = Message.obtain(handler, R.id.search_book_contents_failed);
        message.sendToTarget();
      }
    } catch (JSONException je) {
      Log.w(TAG, "Error accessing book search", je);
      Message message = Message.obtain(handler, R.id.search_book_contents_failed);
      message.sendToTarget();
    }
  }

}*/