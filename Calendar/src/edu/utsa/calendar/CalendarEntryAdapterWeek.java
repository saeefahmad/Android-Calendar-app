package edu.utsa.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * This class extends base adapter class and will be responsible for drawing textbox in the grid weekly view
 * 
 */

public class CalendarEntryAdapterWeek extends BaseAdapter {

	private Context context;
	private String[] text = {};
	private int[] color;

	/**
	 * Constructor
	 */
	public CalendarEntryAdapterWeek(Context c, String[] text, int[] color) {
		context = c;
		this.text = text;
		this.color = color;

	}

	public int getCount() {
		return text.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		TextView textView;

		if (convertView == null) {
			gridView = new View(context);
			gridView = inflater.inflate(R.layout.grid_entry_week, null);
		} else {
			gridView = (View) convertView;
		}
		// set value into textview
		textView = (TextView) gridView.findViewById(R.id.textViewGridEntryWeek);
		// set the text
		textView.setText(text[position]);
		// set the background color only if it is not the default color
		if (color[position] != Color.WHITE) {
			textView.setBackgroundColor(color[position]);
		}
		// set colors for weekends and also time header and week days header
		if (position % 8 == 0) {
			gridView.setBackgroundColor(Color.rgb(136, 217, 183));
		} else if (position < 8) {
			gridView.setBackgroundColor(Color.rgb(150, 192, 224));
		} else if ((position % 8 == 1) || (position % 8 == 7)) {
			gridView.setBackgroundColor(Color.rgb(198, 223, 232));
		}

		return gridView;
	}

}
