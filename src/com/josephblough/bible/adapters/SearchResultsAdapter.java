package com.josephblough.bible.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.josephblough.bible.R;
import com.josephblough.bible.activities.SearchActivity;
import com.josephblough.bible.activities.SearchResultsActivity;
import com.josephblough.bible.data.Book;
import com.josephblough.bible.data.Verse;
import com.josephblough.bible.providers.BibleLibrary;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class SearchResultsAdapter extends ArrayAdapter<Verse> {

    private static final String TAG = "SearchResultsAdapter";
    private static LayoutInflater inflater = null;
    private SearchResultsActivity activity;
    private HashMap<Integer, String> bookNameLookupMap;
    private List<String> searchTokens;
    
    public SearchResultsAdapter(SearchResultsActivity context, List<Verse> verses) {
	super(context, R.layout.search_results_row, verses);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        this.activity = context;
        populateSearchTokens();
        populateBookNameMap();
    }

    public static class ViewHolder {
	public TextView bookName;
	public TextView chapterNumber;
        public TextView verseNumber;
        public TextView verseText;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
	View row = convertView;
        ViewHolder holder;

	if (row == null) {
	    row = inflater.inflate(R.layout.search_results_row, null);
            holder = new ViewHolder();
            holder.bookName = (TextView)row.findViewById(R.id.search_result_row_book);
            holder.chapterNumber = (TextView)row.findViewById(R.id.search_result_row_chapter);
            holder.verseNumber = (TextView)row.findViewById(R.id.search_result_row_verse_number);
            holder.verseText = (TextView)row.findViewById(R.id.search_result_row_verse_text);
            row.setTag(holder);
	}
        else
            holder = (ViewHolder)row.getTag();
	
	final Verse entry = (Verse)super.getItem(position);
	
	//Log.d(TAG, "Verse: " + entry.number + " - " + entry.text);
	holder.bookName.setText(bookNameLookupMap.get(entry.bookId));
	holder.chapterNumber.setText("Chapter " + entry.chapter);
	holder.verseNumber.setText("[" + entry.number + "]");
	holder.verseText.setText(highlightSearchString(entry.text), BufferType.SPANNABLE);
	
	return row;
    }
    
    private SpannableString highlightSearchString(final String verseText) {
	SpannableString formattedString = new SpannableString(verseText);

	for (String token : searchTokens) {
	    int index = verseText.toUpperCase().indexOf(token);
	    while (index != -1) {
		formattedString.setSpan(new ForegroundColorSpan(Color.RED), index, index + token.length(), 0);

		// Move to the next occurrence of this token in the verse
		index = verseText.toUpperCase().indexOf(token, index+1);
	    }
	}

	return formattedString;
    }
    
    private void populateSearchTokens() {
	searchTokens = new ArrayList<String>();
	
	// Based on activity.method, highlight the words in activity.searchTerm
	if (activity.method == SearchActivity.SEARCH_METHOD_EXACT_PHRASE) {
	    searchTokens.add(activity.searchTerm.toUpperCase());
	}
	else {
	    StringTokenizer tokenizer = new StringTokenizer(activity.searchTerm, " ,.");
	    while (tokenizer.hasMoreTokens()) {
		searchTokens.add(tokenizer.nextToken().toUpperCase());
	    }
	}
    }
    
    private void populateBookNameMap() {
	Log.d(TAG, "populateBookNameMap");
	
	bookNameLookupMap = new HashMap<Integer, String>();
	
	List<Book> books = BibleLibrary.getBooks(activity.getContentResolver());
	for (Book book : books) {
	    bookNameLookupMap.put(book.id, book.name);
	}
    }
}
