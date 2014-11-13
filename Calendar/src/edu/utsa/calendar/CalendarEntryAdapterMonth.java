package edu.utsa.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * This class extends base adapter class and will be responsible for drawing textbox in the grid monthly view
 */
public class CalendarEntryAdapterMonth extends BaseAdapter {

	private Context context;
	private String[] text = {};
	private String[] workIndicator = {};
	private int totalEntry;

	/**
	 * Constructor
	 * 
	 */
	public CalendarEntryAdapterMonth(Context c, String[] text,
			String[] workIndicator, int totalEntry) {

		context = c;
		this.text = text;
		this.workIndicator = workIndicator;
		this.totalEntry = totalEntry;

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

			gridView = inflater.inflate(R.layout.grid_entry_month, null);

		} else {

			gridView = (View) convertView;
		}

		// set value into textview
		textView = (TextView) gridView
				.findViewById(R.id.textViewGridEntryMonth);
		textView.setText(text[position]);

		// set colors for weekends and also time header and week days header
		if(position>=totalEntry){
			gridView.setBackgroundColor(Color.rgb(252, 244, 252)); 
		}
		else if (position < 7) {
			gridView.setBackgroundColor(Color.rgb(150, 192, 224)); 
		} else if ((position % 7 == 0) || (position % 7 == 6)) {
			gridView.setBackgroundColor(Color.rgb(198, 223, 232));

		}

		// set circle of the work days

		if (workIndicator[position].equals("1")) {
			textView.setBackground(context.getResources().getDrawable(R.drawable.circle1));
		}

		return gridView;
	}

}
