package com.josephblough.bible.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Bookmarks {

    public List<Integer> bookmarks = new ArrayList<Integer>();
    private Context context;
    
    public Bookmarks(Context context) {
	this.context = context;
    }
    
    public void loadBookmarks() {
	// Remove old bookmarks previously loaded
	bookmarks.clear();
	
	SharedPreferences prefs = context.getSharedPreferences("Bookmarks", 0);
	int ctr = 0;
	String key = "bookmark" + ctr;
	while (prefs.contains(key)) {
	    int verseId = prefs.getInt(key, 0);
	    if (verseId != 0) {
		bookmarks.add(verseId);
	    }
	    
	    ctr++;
	    key = "bookmark" + ctr;
	}
	
	Collections.sort(bookmarks);
    }
    
    public void saveBookmarks() {
	SharedPreferences prefs = context.getSharedPreferences("Bookmarks", 0);
	Editor editor = prefs.edit();
	editor.clear();
	
	for (int i=0; i<bookmarks.size(); i++) {
	    editor.putInt("bookmark" + i, bookmarks.get(i));
	}
	editor.commit();
    }
    
    public void addBookmark(final Integer verseId) {
	loadBookmarks();
	
	if (!bookmarks.contains(verseId)) {
	    bookmarks.add(verseId);
	    Collections.sort(bookmarks);
	    saveBookmarks();
	}
    }
    
    public void removeBookmark(final Integer verseId) {
	loadBookmarks();
	
	if (bookmarks.contains(verseId)) {
	    bookmarks.remove(verseId);
	    Collections.sort(bookmarks);
	    saveBookmarks();
	}
    }
}
