package com.josephblough.bible.adapters;

import java.util.List;

import com.josephblough.bible.R;
import com.josephblough.bible.data.Verse;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VerseAdapter extends ArrayAdapter<Verse> {

    private static final String TAG = "VerseAdapter";
    private static LayoutInflater inflater = null;
    
    public VerseAdapter(Context context, List<Verse> verses) {
	super(context, R.layout.verse_row, verses);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public TextView verseNumber;
        public TextView verseText;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
	View row = convertView;
        ViewHolder holder;

	if (row == null) {
	    row = inflater.inflate(R.layout.verse_row, null);
            holder = new ViewHolder();
            holder.verseNumber = (TextView)row.findViewById(R.id.verse_number);
            holder.verseText = (TextView)row.findViewById(R.id.verse_text);
            row.setTag(holder);
	}
        else
            holder = (ViewHolder)row.getTag();
	
	final Verse entry = (Verse)super.getItem(position);
	
	Log.d(TAG, "Verse: " + entry.number + " - " + entry.text);
	holder.verseNumber.setText("[" + entry.number + "]");
	holder.verseText.setText(entry.text);
	
	return row;
    }    
}
