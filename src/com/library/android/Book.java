package com.library.android;

import org.json.JSONException;
import org.json.JSONObject;

public class Book {
	public final String authorFirstName;
	public final String authorLastName;
	public final String title;
	public final String status;
	private static final String AUTHOR_TAG = "author";
	private static final String FIRSTNAME_TAG = "firstName";
	private static final String LASTNAME_TAG = "lastName";
	private static final String TITLE_TAG = "title";

	public Book(String authorFirstName, String authorLastName, String title,
			String status) {
		this.authorFirstName = authorFirstName;
		this.authorLastName = authorLastName;
		this.title = title;
		this.status = status;
	}

	public Book(String authorFirstName, String authorLastName, String title) {
		this.authorFirstName = authorFirstName;
		this.authorLastName = authorLastName;
		this.title = title;
		this.status = "";
	}

	public static Book parseFromJSONObject(JSONObject bookAsJSONObject) throws JSONException {
		JSONObject author = (JSONObject) bookAsJSONObject.get(AUTHOR_TAG);
		String authorFirstName = author.getString(FIRSTNAME_TAG);
		String authorLastName = author.getString(LASTNAME_TAG);
		String title = bookAsJSONObject.getString(TITLE_TAG);
		Book book = new Book(authorFirstName, authorLastName, title);
		return book;
	}

	@Override
	public String toString() {
		return authorFirstName + "-" + authorLastName + "-" + title;
	}
}
