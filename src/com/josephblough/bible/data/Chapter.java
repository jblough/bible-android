package com.josephblough.bible.data;

import java.util.ArrayList;

import com.josephblough.bible.providers.BibleProvider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Chapter implements BaseColumns {
    public static final String DEFAULT_SORT_ORDER = "Chapter ASC, Verse ASC";

    public static final String BOOK_ID = "BookID";
    public static final String ID = "Chapter";

    public Integer id = null;
    public Integer bookId = null;
    public ArrayList<Verse> verses = null;
    

    public Chapter(Integer id, Integer bookId) {
	super();
	this.id = id;
	this.bookId = bookId;
    }

    public static Uri getContentUri(final Book book) {
	return getContentUri(book.id);
    }
    
    public static Uri getContentUri(final int bookId) {
	return Uri.parse(BibleProvider.CONTENT_URI + "/books/" + bookId + "/chapters");
    }

    public static Uri getContentUri(final Book book, final int chapter) {
	return getContentUri(book.id);
    }
    
    public static Uri getContentUri(final int bookId, final int chapter) {
	return Uri.parse(BibleProvider.CONTENT_URI + "/books/" + bookId + "/chapters/" + chapter);
    }
    
    public static Uri getCountUri(final Book book) {
	return getCountUri(book.id);
    }
    
    public static Uri getCountUri(final int bookId) {
	return Uri.parse(BibleProvider.CONTENT_URI + "/books/" + bookId + "/chapters/count");
    }
    
    public static String getWhereClause(final Book book) {
	return getWhereClause(book.id);
    }

    public static String getWhereClause(final int bookId) {
	return "BookID = " + bookId;
    }
}
