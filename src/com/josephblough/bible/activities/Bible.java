package com.josephblough.bible.activities;

import java.util.HashSet;
import java.util.List;

import com.josephblough.bible.R;
import com.josephblough.bible.adapters.BookAdapter;
import com.josephblough.bible.data.Book;
import com.josephblough.bible.data.Bookmarks;
import com.josephblough.bible.data.Verse;
import com.josephblough.bible.providers.BibleLibrary;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class Bible extends ListActivity implements OnItemClickListener {
    private static final String TAG = "Bible";

    List<Book> books = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(TAG, "onCreate");

	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getListView().setFastScrollEnabled(true);
        getListView().setTextFilterEnabled(true);

        setTitle("Books of the Bible");
        
        books = BibleLibrary.getBooks(getContentResolver());
        setListAdapter(new BookAdapter(this, books));
        getListView().setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	Log.d(TAG, "onItemClick");
	//final Book book = books.get(position);
	final Book book = ((BookAdapter)getListAdapter()).getItem(position);
	//int count = BibleLibrary.getChapterCount(getContentResolver(), book);
	
	//if (count == 1) {
	    gotoChapter(book, 1);
	/*}
	else {
	    final String[] chapterNames = new String[count];
	    for (int i=0; i<count; i++) {
		chapterNames[i] = "Chapter " + (i+1);
	    }

	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(book.name);
	    builder.setSingleChoiceItems(chapterNames, -1, new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
		    gotoChapter(book, which+1);

		    dialog.cancel();
		}
	    });

	    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		    dialog.cancel();
		}
	    });

	    builder.show();
	}*/
    }
    
    public void selectChapter(final Book book) {
	int count = BibleLibrary.getChapterCount(getContentResolver(), book);
	
	if (count == 1) {
	    gotoChapter(book, 1);
	}
	else {
	    final String[] chapterNames = new String[count];
	    for (int i=0; i<count; i++) {
		chapterNames[i] = "Chapter " + (i+1);
	    }

	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(book.name);
	    builder.setSingleChoiceItems(chapterNames, -1, new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
		    gotoChapter(book, which+1);

		    dialog.cancel();
		}
	    });

	    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		    dialog.cancel();
		}
	    });

	    builder.show();
	}
    }
    
    private void gotoChapter(final Book book, final int chapter) {
	Intent intent = new Intent(Bible.this, ChapterActivity.class);
	intent.putExtra(ChapterActivity.TITLE, book.name);
	intent.putExtra(ChapterActivity.BOOK_ID, book.id);
	intent.putExtra(ChapterActivity.CHAPTER, chapter);
	startActivity(intent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_SEARCH) {
	    //showSearchOptions();
	    Intent intent = new Intent(this, SearchActivity.class);
	    startActivity(intent);
	    
	    return true;
	}
	return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.books_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.load_bookmarks_menu_item:
	    loadBookmark();
	    break;
	case R.id.delete_bookmarks_menu_item:
	    removeBookmarks();
	    break;
	case R.id.search_menu_item:
	    Intent intent = new Intent(this, SearchActivity.class);
	    startActivity(intent);
	    break;
	}
	return super.onOptionsItemSelected(item);
    }
    
    private void loadBookmark() {
	Bookmarks bookmarks = new Bookmarks(this);
	bookmarks.loadBookmarks();
	final List<Integer> bookmarkListing = bookmarks.bookmarks;

	final String[] bookmarkStrings = new String[bookmarkListing.size()];
	for (int i=0; i<bookmarkListing.size(); i++) {
	    Verse bookmarkedVerse = BibleLibrary.getVerse(getContentResolver(), bookmarkListing.get(i));
	    Book book = findBook(bookmarkedVerse.bookId);
	    bookmarkStrings[i] = book.name + " Chapter " + bookmarkedVerse.chapter + " Verse " + bookmarkedVerse.number;
	}
	
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Load Bookmark");
	builder.setSingleChoiceItems(bookmarkStrings, -1, new DialogInterface.OnClickListener() {
	    
	    public void onClick(DialogInterface dialog, int which) {
		Verse selectedBookmark = BibleLibrary.getVerse(getContentResolver(), bookmarkListing.get(which));
		Book book = findBook(selectedBookmark.bookId);
		
		Intent intent = new Intent(Bible.this, ChapterActivity.class);
		intent.putExtra(ChapterActivity.TITLE, book.name);
		intent.putExtra(ChapterActivity.BOOK_ID, book.id);
		intent.putExtra(ChapterActivity.CHAPTER, selectedBookmark.chapter);
		intent.putExtra(ChapterActivity.VERSE, selectedBookmark.number);
		startActivity(intent);

		dialog.cancel();
	    }
	});
	
	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
		// Canceled.
		dialog.cancel();
	    }
	});
	
	builder.show();
    }
    
    private void removeBookmarks() {
	final Bookmarks bookmarks = new Bookmarks(this);
	bookmarks.loadBookmarks();
	final List<Integer> bookmarkListing = bookmarks.bookmarks;

	final String[] bookmarkStrings = new String[bookmarkListing.size()];
	for (int i=0; i<bookmarkListing.size(); i++) {
	    Verse bookmarkedVerse = BibleLibrary.getVerse(getContentResolver(), bookmarkListing.get(i));
	    Book book = findBook(bookmarkedVerse.bookId);
	    bookmarkStrings[i] = book.name + " Chapter " + bookmarkedVerse.chapter + " Verse " + bookmarkedVerse.number;
	}
	
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Remove Bookmarks");
	final HashSet<Integer> bookmarksToDelete = new HashSet<Integer>();
	builder.setMultiChoiceItems(bookmarkStrings, null, new DialogInterface.OnMultiChoiceClickListener() {
		
	    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		if (isChecked)
		    bookmarksToDelete.add(bookmarkListing.get(which));
		else
		    bookmarksToDelete.remove(bookmarkListing.get(which));
	    }
	});
	
	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    
	    public void onClick(DialogInterface dialog, int which) {
		// Delete the bookmarks
		for (final Integer bookmark : bookmarksToDelete) {
		    bookmarks.removeBookmark(bookmark);
		}
		
		// Dismiss the dialog
		dialog.dismiss();
	    }
	});
	
	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
		// Canceled.
		dialog.cancel();
	    }
	});
	
	builder.show();
    }
    
    private Book findBook(final int bookId) {
	for (int i=0; i<books.size(); i++) {
	    Book book = books.get(i);
	    if (book.id.equals(bookId))
		return book;
	}
	return null;
    }
}
