package com.josephblough.bible.adapters;

import java.util.List;

import com.josephblough.bible.R;
import com.josephblough.bible.activities.Bible;
import com.josephblough.bible.data.Book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookAdapter extends ArrayAdapter<Book> {

    private static LayoutInflater inflater = null;
    private Bible context;
    
    public BookAdapter(Bible context, List<Book> books) {
	super(context, R.layout.book_row, books);

	this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public TextView bookName;
        public ImageView chapterSelectButton;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
	View row = convertView;
        ViewHolder holder;

	if (row == null) {
	    row = inflater.inflate(R.layout.book_row, null);
            holder = new ViewHolder();
            holder.bookName = (TextView)row.findViewById(R.id.book_name);
            holder.chapterSelectButton = (ImageView)row.findViewById(R.id.book_chapter_selection);
            row.setTag(holder);
	}
        else
            holder = (ViewHolder)row.getTag();
	
	final Book entry = (Book)super.getItem(position);
	
	holder.bookName.setText(entry.name);
	
	holder.chapterSelectButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		context.selectChapter(entry);
	    }
	});
	
	return row;
    }    
}
