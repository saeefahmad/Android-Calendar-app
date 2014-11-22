package edu.utsa.calendar;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * EventActivity is a superclass of Activities (NewEventActivity and
 * ModifyEventActivity), which are related to event operations, such as creating
 * event, updating event and removing event. 
 */

public class EventActivity extends Activity implements OnItemSelectedListener {

	protected int fromYear;
	protected int fromMonth;
	protected int fromDay;
	protected int fromHour;
	protected int fromMinute;
	protected int toYear;
	protected int toMonth;
	protected int toDay;
	protected int toHour;
	protected int toMinute;
	protected String description;
	protected String times;
	protected boolean checked = false;
	protected int occurance = 1; //This value indicate how many events will be created
	protected String categoryName;

	protected final static String DEFAULT_CATEGORY = "default";
	protected EventManager manager;
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

	/**
	 * Display the date picker dialog to enable user to select event start date 
	 * @param view
	 */
	public void showFromDatePickerDialog(View view) {
		DatePickerFragment date = new DatePickerFragment();
		Bundle args = new Bundle();
		args.putInt("date_view", R.id.from_date);
		date.setArguments(args);
		date.show(getFragmentManager(), "fromDatePicker");
	}

	/**
	 * Display time picker dialog to enable user to select event start time of day	 
	 * @param view
	 */
	public void showFromTimePickerDialog(View view) {
		TimePickerFragment time = new TimePickerFragment();
		Bundle args = new Bundle();
		args.putInt("time_view", R.id.from_time);
		time.setArguments(args);
		time.show(getFragmentManager(), "fromTimePicker");
	}

	/**
	 * Display the date picker dialog to enable user to select event end date
	 * @param view
	 */
	public void showToDatePickerDialog(View view) {
		DatePickerFragment date = new DatePickerFragment();
		Bundle args = new Bundle();
		args.putInt("date_view", R.id.to_date);
		date.setArguments(args);
		date.show(getFragmentManager(), "toDatePicker");
	}

	/**
	 * Display the time picker dialog to enable user to select event end time of day
	 * @param view
	 */
	public void showToTimePickerDialog(View view) {
		TimePickerFragment time = new TimePickerFragment();
		Bundle args = new Bundle();
		args.putInt("time_view", R.id.to_time);
		time.setArguments(args);
		time.show(getFragmentManager(), "toTimePicker");
	}

	/**
	 * Handle the event when the checkbox is checked
	 * @param view
	 */
	public void onCheckboxClicked(View view) {
		checked = ((CheckBox) view).isChecked();
	}

	/**
	 * Collect user input from all view components
	 * @return boolean
	 */
	protected boolean getData() {
		TextView from = (TextView) findViewById(R.id.from_date);
		String tmp = from.getText().toString();
		Boolean bool;
		if (tmp == null || tmp.isEmpty())
			return false;
		String[] s = tmp.split("/");
		String[] time;
		fromYear = Integer.parseInt(s[2]);
		fromMonth = Integer.parseInt(s[0]) - 1;
		fromDay = Integer.parseInt(s[1]);

		TextView f_time = (TextView) findViewById(R.id.from_time);
		tmp = f_time.getText().toString();
		if (tmp == null || tmp.isEmpty())
			return false;
		s = tmp.split(" ");
		if (s[1].equals("AM")) {
			time = s[0].split(":");
			fromHour = Integer.parseInt(time[0]);
			fromMinute = Integer.parseInt(time[1]);
		} else {
			time = s[0].split(":");
			fromHour = Integer.parseInt(time[0]) + 12;
			fromMinute = Integer.parseInt(time[1]);
		}

		TextView to = (TextView) findViewById(R.id.to_date);
		tmp = to.getText().toString();
		if (tmp == null || tmp.isEmpty())
			return false;
		s = tmp.split("/");
		toYear = Integer.parseInt(s[2]);
		toMonth = Integer.parseInt(s[0]) - 1;
		toDay = Integer.parseInt(s[1]);

		TextView t_time = (TextView) findViewById(R.id.to_time);
		tmp = t_time.getText().toString();
		if (tmp == null || tmp.isEmpty())
			return false;
		s = tmp.split(" ");
		if (s[1].equals("AM")) {
			time = s[0].split(":");
			toHour = Integer.parseInt(time[0]);
			toMinute = Integer.parseInt(time[1]);
		} else {
			time = s[0].split(":");
			toHour = Integer.parseInt(time[0]) + 12;
			toMinute = Integer.parseInt(time[1]);
		}

		EditText editText = (EditText) findViewById(R.id.description);
		tmp = editText.getText().toString();
		if (tmp == null || tmp.isEmpty())
			return false;
		description = tmp;
		
		CheckBox check = (CheckBox) findViewById(R.id.periodical);
		bool = check.isChecked();
				
		if (bool) {
			occurance = 5;
//			editText = (EditText) findViewById(R.id.times);
//			tmp = editText.getText().toString();
//			occurance = Integer.parseInt(tmp);
		}

		return true;
	}

	/**
	 * verify the user input data to make sure that event start time is earlier
	 * than end time and if it is weekly repeating event, the repeating times is
	 * positive value
	 * 
	 * @return boolean
	 */
	protected boolean verifyData() {
		Calendar fromDate, toDate;

		fromDate = Calendar.getInstance();
		fromDate.set(fromYear, fromMonth, fromDay, fromHour, fromMinute, 0);

		toDate = Calendar.getInstance();
		toDate.set(toYear, toMonth, toDay, toHour, toMinute, 0);

		if (fromDate.after(toDate) || fromDate.equals(toDate))
			return false;

		if (occurance <= 0)
			return false;

		return true;
	}

	/**
	 * Give user feedback when something goes wrong
	 * @param text
	 */
	protected void popup(CharSequence text) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		categoryName = (String) parent.getItemAtPosition(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
