package com.josephblough.bible.providers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.josephblough.bible.data.Book;
import com.josephblough.bible.data.Chapter;
import com.josephblough.bible.data.Testament;
import com.josephblough.bible.data.Verse;

public class BibleLibrary {
    private static final String TAG = "BibleLibrary";

    // Get a list of the testaments
    public static List<Testament> getTestaments(final ContentResolver resolver) {
	List<Testament> testaments = new ArrayList<Testament>();
	Cursor cursor = null;
	try {
	    try {
		cursor = getTestamentsCursor(resolver);

		while (cursor.moveToNext()) {
		    testaments.add(new Testament(cursor.getInt(cursor.getColumnIndex(Testament.ID)), 
			    cursor.getString(cursor.getColumnIndex(Testament.NAME))));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return testaments;
    }
    
    public static Cursor getTestamentsCursor(final ContentResolver resolver) {
	return resolver.query(Testament.CONTENT_URI, new String[] { Testament.ID, Testament.NAME }, 
		null, null, Testament.DEFAULT_SORT_ORDER);
    }
    
    // Get all books in the Bible
    public static List<Book> getBooks(final ContentResolver resolver) {
	List<Book> books = new ArrayList<Book>();
	
	Cursor cursor = null;
	try {
	    try {
		cursor = getBooksCursor(resolver);

		while (cursor.moveToNext()) {
		    books.add(new Book(cursor.getInt(cursor.getColumnIndex(Book.ID)), cursor.getString(cursor.getColumnIndex(Book.NAME))));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return books;
    }
    
    public static Cursor getBooksCursor(final ContentResolver resolver) {
	return resolver.query(Book.CONTENT_URI, new String[] { Book.ID, Book.NAME }, 
		null, null, Book.DEFAULT_SORT_ORDER);
    }
    
    // Get the books for a testament
    public static List<Book> getBooks(final ContentResolver resolver, final Testament testament) {
	return getBooks(resolver, testament.id);
    }
    
    public static List<Book> getBooks(final ContentResolver resolver, final int testamentId) {
	List<Book> books = new ArrayList<Book>();
	
	Cursor cursor = null;
	try {
	    try {
		cursor = getBooksCursor(resolver, testamentId);

		while (cursor.moveToNext()) {
		    books.add(new Book(cursor.getInt(cursor.getColumnIndex(Book.ID)), cursor.getString(cursor.getColumnIndex(Book.NAME))));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return books;
    }

    public static Cursor getBooksCursor(final ContentResolver resolver, final Testament testament) {
	return getBooksCursor(resolver, testament.id);
    }
    
    public static Cursor getBooksCursor(final ContentResolver resolver, final int testamentId) {
	return resolver.query(Book.CONTENT_URI, new String[] { Book.ID, Book.NAME }, 
		"TestamentID = " + testamentId, null, Book.DEFAULT_SORT_ORDER);
    }

    public static Book getBook(final ContentResolver resolver, final int bookId) {
	Book book = null;

	Cursor cursor = null;
	try {
	    try {
		cursor = resolver.query(Book.getContentUri(bookId), 
			new String[] { Book.ID, Book.NAME }, 
			Book.getWhereClause(bookId), null, 
			Book.DEFAULT_SORT_ORDER);

		while (cursor.moveToNext()) {
		    book = new Book(cursor.getInt(cursor.getColumnIndex(Book.ID)), 
			    cursor.getString(cursor.getColumnIndex(Book.NAME)));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return book;
    }
    
    // Get the number of chapters in a book (this may be the only useful Chapter-based method)
    public static int getChapterCount(final ContentResolver resolver, final Book book) {
	return getChapterCount(resolver, book.id);
    }
    
    public static int getChapterCount(final ContentResolver resolver, final int bookId) {
	int count = 0;
	
	Cursor cursor = null;
	try {
	    try {
		cursor = resolver.query(Chapter.getCountUri(bookId), new String[] { "MAX(Chapter) AS count" }, 
			Chapter.getWhereClause(bookId), null, "count");

		while (cursor.moveToNext()) {
		    count = cursor.getInt(cursor.getColumnIndex("count"));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return count;
    }
    
    // Get all the chapters in a book
    public static List<Chapter> getChapters(final ContentResolver resolver, final Book book) {
	return getChapters(resolver, book.id);
    }
    
    public static List<Chapter> getChapters(final ContentResolver resolver, final int bookId) {
	List<Chapter> chapters = new ArrayList<Chapter>();
	
	Cursor cursor = null;
	try {
	    try {
		cursor = getChaptersCursor(resolver, bookId);

		while (cursor.moveToNext()) {
		    chapters.add(new Chapter(cursor.getInt(cursor.getColumnIndex(Chapter.ID)), cursor.getInt(cursor.getColumnIndex(Chapter.BOOK_ID))));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return chapters;
    }
    
    public static Cursor getChaptersCursor(final ContentResolver resolver, final Book book) {
	return getChaptersCursor(resolver, book.id);
    }
    
    public static Cursor getChaptersCursor(final ContentResolver resolver, final int bookId) {
	return resolver.query(Chapter.getContentUri(bookId), new String[] { Chapter.ID, Chapter.BOOK_ID }, 
		Chapter.getWhereClause(bookId), null, Chapter.DEFAULT_SORT_ORDER);
    }
    
    // Get one chapter in a book
    public static Chapter getChapter(final ContentResolver resolver, final Book book, final int chapter) {
	return getChapter(resolver, book.id, chapter);
    }
    
    public static Chapter getChapter(final ContentResolver resolver, final int bookId, final int chapter) {
	return null;
    }

    // Get the number of verses in a chapter
    public static int getVerseCount(final ContentResolver resolver, final Book book, final int chapter) {
	return getVerseCount(resolver, book.id, chapter);
    }

    public static int getVerseCount(final ContentResolver resolver, final int bookId, final int chapter) {
	int count = 0;
	
	Cursor cursor = null;
	try {
	    try {
		cursor = resolver.query(Verse.getCountUri(bookId, chapter), new String[] { "MAX(Verse) AS count" }, 
			Verse.getWhereClause(bookId, chapter), null, "count");

		while (cursor.moveToNext()) {
		    count = cursor.getInt(cursor.getColumnIndex("count"));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return count;
    }
    
    // Get all the verses for a chapter
    public static List<Verse> getVerses(final ContentResolver resolver, final Book book, final int chapter) {
	return getVerses(resolver, book.id, chapter);
    }

    public static List<Verse> getVerses(final ContentResolver resolver, final int bookId, final int chapter) {
	List<Verse> verses = new ArrayList<Verse>();
	
	Cursor cursor = null;
	try {
	    try {
		cursor = getVersesCursor(resolver, bookId, chapter);

		while (cursor.moveToNext()) {
		    verses.add(new Verse(cursor.getInt(cursor.getColumnIndex(Verse.ID)), 
			    cursor.getInt(cursor.getColumnIndex(Verse.NUMBER)),
			    cursor.getString(cursor.getColumnIndex(Verse.TEXT)),
			    cursor.getInt(cursor.getColumnIndex(Verse.BOOK_ID)),
			    cursor.getInt(cursor.getColumnIndex(Verse.CHAPTER))));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return verses;
    }
    
    public static Cursor getVersesCursor(final ContentResolver resolver, final Book book, final int chapter) {
	return getVersesCursor(resolver, book.id, chapter);
    }

    public static Cursor getVersesCursor(final ContentResolver resolver, final int bookId, final int chapter) {
	return resolver.query(Verse.getContentUri(bookId, chapter), new String[] { Verse.ID, Verse.NUMBER, Verse.TEXT, Verse.BOOK_ID, Verse.CHAPTER }, 
		Verse.getWhereClause(bookId, chapter), null, Verse.DEFAULT_SORT_ORDER);
    }
    
    // Get one verse for a chapter
    public static Verse getVerse(final ContentResolver resolver, final Book book, final int chapter, final int verse) {
	return getVerse(resolver, book.id, chapter, verse);
    }

    public static Verse getVerse(final ContentResolver resolver, final int bookId, final int chapter, final int verse) {
	Verse v = null;

	Cursor cursor = null;
	try {
	    try {
		cursor = resolver.query(Verse.getContentUri(bookId, chapter, verse), 
			new String[] { Verse.ID, Verse.NUMBER, Verse.TEXT, Verse.BOOK_ID, Verse.CHAPTER }, 
			Verse.getWhereClause(bookId, chapter, verse), null, 
			Verse.DEFAULT_SORT_ORDER);

		while (cursor.moveToNext()) {
		    v = new Verse(cursor.getInt(cursor.getColumnIndex(Verse.ID)), 
			    cursor.getInt(cursor.getColumnIndex(Verse.NUMBER)),
			    cursor.getString(cursor.getColumnIndex(Verse.TEXT)),
			    cursor.getInt(cursor.getColumnIndex(Verse.BOOK_ID)),
			    cursor.getInt(cursor.getColumnIndex(Verse.CHAPTER)));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return v;
    }

    public static Verse getVerse(final ContentResolver resolver, final int verseId) {
	Verse v = null;

	Cursor cursor = null;
	try {
	    try {
		cursor = resolver.query(Verse.getContentUri(verseId), 
			new String[] { Verse.ID, Verse.NUMBER, Verse.TEXT, Verse.BOOK_ID, Verse.CHAPTER }, 
			Verse.getWhereClause(verseId), null, Verse.DEFAULT_SORT_ORDER);

		while (cursor.moveToNext()) {
		    v = new Verse(cursor.getInt(cursor.getColumnIndex(Verse.ID)), 
			    cursor.getInt(cursor.getColumnIndex(Verse.NUMBER)),
			    cursor.getString(cursor.getColumnIndex(Verse.TEXT)),
			    cursor.getInt(cursor.getColumnIndex(Verse.BOOK_ID)),
			    cursor.getInt(cursor.getColumnIndex(Verse.CHAPTER)));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return v;
    }

    public static List<Verse> getVerses(final ContentResolver resolver, final String where) {
	List<Verse> verses = new ArrayList<Verse>();
	
	Cursor cursor = null;
	try {
	    try {
		cursor = getVersesCursor(resolver, where);

		while (cursor.moveToNext()) {
		    verses.add(new Verse(cursor.getInt(cursor.getColumnIndex(Verse.ID)), 
			    cursor.getInt(cursor.getColumnIndex(Verse.NUMBER)),
			    cursor.getString(cursor.getColumnIndex(Verse.TEXT)),
			    cursor.getInt(cursor.getColumnIndex(Verse.BOOK_ID)),
			    cursor.getInt(cursor.getColumnIndex(Verse.CHAPTER))));
		}
	    } finally {
		if (cursor != null) {
		    cursor.close();
		}
	    }
	} catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return verses;
    }
    
    public static Cursor getVersesCursor(final ContentResolver resolver, final String where) {
	return resolver.query(Verse.getContentUri(), new String[] { Verse.ID, Verse.NUMBER, Verse.TEXT, Verse.BOOK_ID, Verse.CHAPTER }, 
		where, null, Verse.DEFAULT_SORT_ORDER);
    }
}
