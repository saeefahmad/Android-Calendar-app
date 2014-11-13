package edu.utsa.calendar;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * DatePickerFragment is an Android Fragment to enable user to select a date
 */
public class DatePickerFragment extends DialogFragment implements
		OnDateSetListener {
	
	/**
	 * constructor with no parameter
	 */
	public DatePickerFragment() {
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    // Use the current date as the default date in the picker
	    final Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH);
	    int day = c.get(Calendar.DAY_OF_MONTH);

	    // Create a new instance of DatePickerDialog and return it
	    return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		// set date view text
		int id = getArguments().getInt("date_view");
		TextView textView = (TextView)getActivity().findViewById(id);
		textView.setText(String.valueOf(month + 1 ) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
	}


}
