package com.josephblough.bible.activities;

import java.util.ArrayList;
import java.util.List;

import com.josephblough.bible.R;
import com.josephblough.bible.data.Book;
import com.josephblough.bible.providers.BibleLibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener {

    private static final String TAG = "SearchActivity";

    public static final int SEARCH_METHOD_EXACT_PHRASE = 0;
    public static final int SEARCH_METHOD_ALL_WORDS = 1;
    public static final int SEARCH_METHOD_ANY_WORDS = 2;
    
    public static final int SEARCH_SCOPE_ALL_BOOKS = 0;
    public static final int SEARCH_SCOPE_OT_BOOKS = 1;
    public static final int SEARCH_SCOPE_NT_BOOKS = 2;
    public static final int SEARCH_SCOPE_SELECTED_BOOKS = 3;
    
    List<Book> books = null;
    EditText searchField;
    ListView selectedBooksListView;
    Spinner searchMethodSpinner;
    Spinner searchScopeSpinner;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(TAG, "onCreate");
	
	super.onCreate(savedInstanceState);

	setContentView(R.layout.search);

	searchField = (EditText) findViewById(R.id.search_term);
	
        books = BibleLibrary.getBooks(getContentResolver());
        selectedBooksListView = (ListView) findViewById(R.id.search_selected_books_list);
        
        ArrayAdapter<Book> adapter = new ArrayAdapter<Book>(this, 
        	android.R.layout.simple_list_item_multiple_choice, books);
        selectedBooksListView.setAdapter(adapter);
        selectedBooksListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        selectedBooksListView.setVisibility(View.INVISIBLE);
        
        searchMethodSpinner = (Spinner) findViewById(R.id.search_method);
        searchScopeSpinner = (Spinner) findViewById(R.id.search_scope);
        
        ArrayAdapter<CharSequence> methodsAdapter = ArrayAdapter.createFromResource(this, R.array.search_methods,
                android.R.layout.simple_spinner_item);
        methodsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchMethodSpinner.setAdapter(methodsAdapter);
        
        ArrayAdapter<CharSequence> scopeAdapter = ArrayAdapter.createFromResource(this, R.array.search_scope,
                android.R.layout.simple_spinner_item);
        scopeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchScopeSpinner.setAdapter(scopeAdapter);
        
        searchScopeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    public void onItemSelected(AdapterView<?> parent, View view,
		    int position, long id) {
	        selectedBooksListView.setVisibility((SEARCH_SCOPE_SELECTED_BOOKS == position) ? 
	        	View.VISIBLE :
	        	View.INVISIBLE);
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	        selectedBooksListView.setVisibility(View.INVISIBLE);
	    }
	});
        
        selectedBooksListView.setFastScrollEnabled(true);
        
        ((Button) findViewById(R.id.submit_search)).setOnClickListener(this);
        
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	    
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
		    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		    in.hideSoftInputFromWindow(searchField.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		    return true;
		}
		return false;
	    }
	});
    }

    public void onClick(View v) {
	
	if ("".equals(searchField.getText().toString())) {
	    Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
	    // Set focus to search term field
	    //searchField.???
	}
	else if (SEARCH_SCOPE_SELECTED_BOOKS == searchScopeSpinner.getSelectedItemPosition() && 
		getSelectedBooks().length < 1) {
	    Toast.makeText(this, "No books selected", Toast.LENGTH_SHORT).show();
	}
	else {
	    // Submit the search
	    Intent intent = new Intent(this, SearchResultsActivity.class);
	    intent.putExtra(SearchResultsActivity.SEARCH_TERM, searchField.getText().toString());
	    intent.putExtra(SearchResultsActivity.SEARCH_METHOD, searchMethodSpinner.getSelectedItemPosition());
	    intent.putExtra(SearchResultsActivity.SEARCH_SCOPE, searchScopeSpinner.getSelectedItemPosition());

	    if (SEARCH_SCOPE_SELECTED_BOOKS == searchScopeSpinner.getSelectedItemPosition()) {
		intent.putExtra(SearchResultsActivity.SEARCH_SCOPE_SELECTED_BOOKS, getSelectedBooks());
	    }
	    startActivity(intent);
	}
    }
    
    @SuppressWarnings("unchecked")
    public int[] getSelectedBooks() {
	ArrayList<Integer> selectedBooks = new ArrayList<Integer>();
	SparseBooleanArray items = selectedBooksListView.getCheckedItemPositions();
	
	ArrayAdapter<Book> adapter = (ArrayAdapter<Book>)selectedBooksListView.getAdapter();
	for (int i=0; i<items.size(); i++) {
	    if (items.valueAt(i)) {
		selectedBooks.add(adapter.getItem(items.keyAt(i)).id);
	    }
	}

	int[] asIntArray = new int[selectedBooks.size()];
	
	for (int i=0; i<selectedBooks.size(); i++) {
	    asIntArray[i] = selectedBooks.get(i);
	}

	return asIntArray;
    }
}
