package com.josephblough.bible.activities;

import java.util.List;

import com.josephblough.bible.R;
import com.josephblough.bible.adapters.SearchResultsAdapter;
import com.josephblough.bible.data.Book;
import com.josephblough.bible.data.Verse;
import com.josephblough.bible.providers.BibleLibrary;
import com.josephblough.bible.tasks.BibleSearchTask;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultsActivity extends ListActivity implements OnItemClickListener {

    private static final String TAG = "SearchResultsActivity";

    public static final String SEARCH_METHOD = "search.method";
    public static final String SEARCH_SCOPE = "search.scope";
    public static final String SEARCH_SCOPE_SELECTED_BOOKS = "search.scope.selected.books";
    public static final String SEARCH_TERM = "search.term";

    public String searchTerm;
    public int method;
    public int scope;
    public int[] selectedBooks;
    
    private ProgressDialog progress;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(TAG, "onCreate");
	
	super.onCreate(savedInstanceState);

	setContentView(R.layout.search_results);

	getListView().setOnItemClickListener(this);
	
	this.searchTerm = getIntent().getStringExtra(SEARCH_TERM);
	this.method = getIntent().getIntExtra(SEARCH_METHOD, SearchActivity.SEARCH_METHOD_EXACT_PHRASE);
	this.scope = getIntent().getIntExtra(SEARCH_SCOPE, SearchActivity.SEARCH_SCOPE_ALL_BOOKS);
	this.selectedBooks = getIntent().getIntArrayExtra(SEARCH_SCOPE_SELECTED_BOOKS);

	((TextView) findViewById(R.id.search_results_header)).setText("Search results for \"" + this.searchTerm + "\"");
	
	search();
    }
    
    private void search() {
	progress = ProgressDialog.show(this, null, "Searching", true);
	
	BibleSearchTask searcher = new BibleSearchTask(this);
	searcher.execute();
    }
    
    public void searchCompleted(final List<Verse> verses) {
	
	// Populate an adapter from the results (search with the word "the" to test scalability)
	Log.d(TAG, "There were " + verses.size() + " verses found");
	SearchResultsAdapter adapter = new SearchResultsAdapter(this, verses);
	setListAdapter(adapter);

	// Dismiss the progress indicator
	if (progress != null)
	    progress.dismiss();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	Verse verse = ((SearchResultsAdapter)getListAdapter()).getItem(position);
	
	Book book = BibleLibrary.getBook(getContentResolver(), verse.bookId);
	
	Intent intent = new Intent(this, ChapterActivity.class);
	intent.putExtra(ChapterActivity.TITLE, book.name);
	intent.putExtra(ChapterActivity.BOOK_ID, book.id);
	intent.putExtra(ChapterActivity.CHAPTER, verse.chapter);
	intent.putExtra(ChapterActivity.VERSE, verse.number);
	startActivity(intent);
    }
}
