package edu.utsa.calendar;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * TimePickerFragment is an Android Fragment enabling user to select time of day
 */
public class TimePickerFragment extends DialogFragment implements
		OnTimeSetListener {
	/**
	 * constructor with no parameter
	 */
	public TimePickerFragment() {
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// set time view text
		int id = getArguments().getInt("time_view");
		TextView textView = (TextView)getActivity().findViewById(id);
		formatTime(textView, hourOfDay, minute);

	}
	
	// generate user friendly time, such as 10:30 AM
	private void formatTime(TextView view, int hourOfDay, int minute) {
		String suffix;
		if (hourOfDay < 12) {
			suffix = " AM";
			if (minute < 10)
				view.setText(String.valueOf(hourOfDay) + ":" + 0 + String.valueOf(minute) + suffix);
			else
				view.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + suffix);
		}
		else {
			suffix = " PM";
			if (minute < 10)
				view.setText(String.valueOf(hourOfDay-12) + ":" + 0 + String.valueOf(minute) + suffix);
			else
				view.setText(String.valueOf(hourOfDay-12) + ":" + String.valueOf(minute) + suffix);
		}
	}

}
