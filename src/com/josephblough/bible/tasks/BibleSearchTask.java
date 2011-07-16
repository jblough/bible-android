package com.josephblough.bible.tasks;

import com.josephblough.bible.activities.SearchActivity;
import com.josephblough.bible.activities.SearchResultsActivity;
import com.josephblough.bible.data.Verse;
import com.josephblough.bible.providers.BibleLibrary;

import java.util.List;
import java.util.StringTokenizer;

import android.os.AsyncTask;
import android.util.Log;

public class BibleSearchTask extends AsyncTask<Void, Void, List<Verse>> {

    private static final String TAG = "BibleSearchTask";
    
    private SearchResultsActivity activity;
    
    public BibleSearchTask(final SearchResultsActivity activity) {
	this.activity = activity;
    }
    
    @Override
    protected List<Verse> doInBackground(Void... arg0) {

	StringBuilder builder = new StringBuilder();
	// Create a WHERE clause based on the search method
	switch (activity.method) {
	case SearchActivity.SEARCH_METHOD_EXACT_PHRASE:
	    // WHERE VerseText LIKE %?%
	    builder.append("VerseText LIKE '%" + activity.searchTerm + "%'");
	    break;
	case SearchActivity.SEARCH_METHOD_ALL_WORDS:
	    // Break up the search term into words and add each as
	    // WHERE VerseText LIKE %?% AND VerseText LIKE %?% AND ...
	{
	    StringTokenizer tokenizer = new StringTokenizer(activity.searchTerm, " ,.");
	    while (tokenizer.hasMoreTokens()) {
		builder.append("VerseText LIKE '%" + tokenizer.nextToken() + "%'");
		if (tokenizer.hasMoreTokens()) {
		    builder.append(" AND ");
		}
	    }
	}
	    break;
	case SearchActivity.SEARCH_METHOD_ANY_WORDS:
	    // Break up the search term into words and add each as
	    // WHERE VerseText LIKE %?% OR VerseText LIKE %?% OR ...
	{
	    StringTokenizer tokenizer = new StringTokenizer(activity.searchTerm, " ,.");
	    while (tokenizer.hasMoreTokens()) {
		builder.append("VerseText LIKE '%" + tokenizer.nextToken() + "%'");
		if (tokenizer.hasMoreTokens()) {
		    builder.append(" OR ");
		}
	    }
	}
	    break;
	}
	
	// Limit the scope of the search if needed
	switch (activity.scope) {
	case SearchActivity.SEARCH_SCOPE_NT_BOOKS:
	    // AND BookID IN (SELECT id FROM Books WHERE TestamentID = 2)
	    builder.append(" AND BookID IN (SELECT id FROM Books WHERE TestamentID = 2)");
	    break;
	case SearchActivity.SEARCH_SCOPE_OT_BOOKS:
	    // AND BookID IN (SELECT id FROM Books WHERE TestamentID = 1)
	    builder.append(" AND BookID IN (SELECT id FROM Books WHERE TestamentID = 1)");
	    break;
	case SearchActivity.SEARCH_SCOPE_SELECTED_BOOKS:
	    // AND BookID IN (?, ?, ?, ?, ...)
	    builder.append(" AND BookID IN (");
	    for (int i=0; i<activity.selectedBooks.length; i++) {
		builder.append(activity.selectedBooks[i]);
		if (i < (activity.selectedBooks.length-1)) {
		    builder.append(", ");
		}
	    }
	    builder.append(")");
	    break;
	}
	
	// Run the query via BibleLibrary
	Log.d(TAG, "Searching with : " + builder.toString());
	List<Verse> verses = BibleLibrary.getVerses(activity.getContentResolver(), builder.toString());
	Log.d(TAG, "There were " + verses.size() + " verses found");

	return verses;
    }

    @Override
    protected void onPostExecute(List<Verse> verses) {
	activity.searchCompleted(verses);
	
        super.onPostExecute(verses);
    }
}
