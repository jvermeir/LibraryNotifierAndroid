package com.library.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ListActivity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class JSONBookListJActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new HttpGetTask().execute();
	}

	private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

		private static final String TAG = "HttpGetTask";

		private static final String URL = "http://192.168.10.127:9181/recommendations";
//		private static final String URL = "http://localhost:9181/books";

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<String> doInBackground(Void... params) {
			Log.d(TAG, "Sending request to: " + URL);
			HttpGet request = new HttpGet(URL);
			JSONResponseHandler responseHandler = new JSONResponseHandler();
			List<String> result = new ArrayList<String>();
			try {
				result.addAll(mClient.execute(request, responseHandler));
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			if (null != mClient)
				mClient.close();
			setListAdapter(new ArrayAdapter<String>(JSONBookListJActivity.this, R.layout.list_item, result));
		}
	}

	private class JSONResponseHandler implements ResponseHandler<List<String>> {
		private static final String BOOKS_TAG = "books";

		@Override
		public List<String> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			List<String> result = new ArrayList<String>();
			String JSONResponse = new BasicResponseHandler().handleResponse(response);
			try {
				JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
				JSONArray books = responseObject.getJSONArray(BOOKS_TAG);
				for (int idx = 0; idx < books.length(); idx++) {
					Book book = Book.parseFromJSONObject((JSONObject) books.get(idx));
					result.add(book.toString());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}
	}
}