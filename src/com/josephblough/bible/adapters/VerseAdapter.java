package com.josephblough.bible.adapters;

import java.util.List;

import com.josephblough.bible.R;
import com.josephblough.bible.data.Verse;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VerseAdapter extends ArrayAdapter<Verse> {

    private static final String TAG = "VerseAdapter";
    public static final String FONT_PREFERENCE = "VerseAdapter.font";
    
    private static LayoutInflater inflater = null;
    private float verseFontSize = -1.0f;
    
    public VerseAdapter(Context context, List<Verse> verses) {
	super(context, R.layout.verse_row, verses);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
	SharedPreferences prefs = context.getSharedPreferences("VerseAdapter", 0);
	if (prefs.contains(FONT_PREFERENCE)) {
	    verseFontSize = prefs.getFloat(FONT_PREFERENCE, -1.0f);
	}

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

	if (verseFontSize > 0) {
	    holder.verseText.setTextSize(verseFontSize);
	}
	
	return row;
    }
    
    public float getFontSize() {
	if (verseFontSize > 0) {
	    return verseFontSize;
	}
	else {
	    View row = inflater.inflate(R.layout.verse_row, null);
	    return ((TextView)row.findViewById(R.id.verse_text)).getTextSize();
	}
    }
    
    public void setFontSize(final float fontSize) {
	this.verseFontSize = fontSize;
    }
}
