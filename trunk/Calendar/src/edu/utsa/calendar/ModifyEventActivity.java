package edu.utsa.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * ModifyEventActivity provides the user the capability to update and remove the
 * existing events
 */
public class ModifyEventActivity extends EventActivity {

	// if the event is a weekly periodical event, we created it as multiple
	// events for consecutive weeks
	private Calendar[] oldfrom; 
	private Calendar[] oldto; 
	private int index; 
	private int total; 

	// set attributes with event information
	private void setData(Event event) {
		fromYear = event.getStartDate().get(Calendar.YEAR);
		fromMonth = event.getStartDate().get(Calendar.MONTH) + 1;
		fromDay = event.getStartDate().get(Calendar.DAY_OF_MONTH);
		fromHour = event.getStartDate().get(Calendar.HOUR_OF_DAY);
		fromMinute = event.getStartDate().get(Calendar.MINUTE);

		toYear = event.getEndDate().get(Calendar.YEAR);
		toMonth = event.getEndDate().get(Calendar.MONTH) + 1;
		toDay = event.getEndDate().get(Calendar.DAY_OF_MONTH);
		toHour = event.getEndDate().get(Calendar.HOUR_OF_DAY);
		toMinute = event.getEndDate().get(Calendar.MINUTE);

		description = event.getDescription();
		occurance = event.getTotalOccurance();
		total = occurance;
		if (occurance > 1) {
			checked = true;
		}
		index = event.getOccuranceIndex();
		categoryName = event.getCategoryID();

		oldfrom = new Calendar[total];
		oldto = new Calendar[total];

		oldfrom[index - 1] = event.getStartDate();
		oldto[index - 1] = event.getEndDate();

		for (int i = index - 2; i >= 0; i--) {
			oldfrom[i] = Calendar.getInstance();
			oldfrom[i].set(fromYear, fromMonth - 1, fromDay, fromHour,
					fromMinute, 0);
			oldfrom[i].add(Calendar.DAY_OF_MONTH, -7 * (index - 1 - i));
			oldfrom[i].clear(Calendar.MILLISECOND);
			
			oldto[i] = Calendar.getInstance();
			oldto[i].set(toYear, toMonth - 1, toDay, toHour, toMinute, 0);
			oldto[i].add(Calendar.DAY_OF_MONTH, -7 * (index - 1 - i));
			oldto[i].clear(Calendar.MILLISECOND);
		}

		for (int i = index; i <= total - 1; i++) {
			oldfrom[i] = Calendar.getInstance();
			oldfrom[i].set(fromYear, fromMonth - 1, fromDay, fromHour,
					fromMinute, 0);
			oldfrom[i].add(Calendar.DAY_OF_MONTH, 7 * (i - (index - 1)));
			oldfrom[i].clear(Calendar.MILLISECOND);
			
			oldto[i] = Calendar.getInstance();
			oldto[i].set(toYear, toMonth - 1, toDay, toHour, toMinute, 0);
			oldto[i].add(Calendar.DAY_OF_MONTH, 7 * (i - (index - 1)));
			oldto[i].clear(Calendar.MILLISECOND);
		}
	}

	// construct user friendly time display, like 4:04 AM
	private String constructTime(int hourOfDay, int minute) {
		String suffix;
		if (hourOfDay < 12) {
			suffix = " AM";
			if (minute < 10)
				return String.valueOf(hourOfDay) + ":" + 0
						+ String.valueOf(minute) + suffix;
			else
				return String.valueOf(hourOfDay) + ":" + String.valueOf(minute)
						+ suffix;
		} else {
			suffix = " PM";
			if (minute < 10)
				return String.valueOf(hourOfDay - 12) + ":" + 0
						+ String.valueOf(minute) + suffix;
			else
				return String.valueOf(hourOfDay - 12) + ":"
						+ String.valueOf(minute) + suffix;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		manager = Manager.getInstance().getEventManager();
		final String EVENT_TIME_CONFLICT = getResources().getString(
				R.string.time_conflict);
		final String INVALID_USER_INPUT = getResources().getString(
				R.string.invalid_input);
		final String INCOMPLETE_USER_INPUT = getResources().getString(
				R.string.incomplte_input);

		Intent intent = getIntent();
		int event_id = intent.getIntExtra("event_id", -100);
		Event e = manager.getEventById(event_id);

		setData(e);
		// dynamically set the views
		String s;
		TextView from_date = (TextView) findViewById(R.id.from_date);
		from_date.setText(fromMonth + "/" + fromDay + "/" + fromYear);

		TextView from_time = (TextView) findViewById(R.id.from_time);
		s = constructTime(fromHour, fromMinute);
		from_time.setText(s);

		TextView to_date = (TextView) findViewById(R.id.to_date);
		to_date.setText(toMonth + "/" + toDay + "/" + toYear);

		TextView to_time = (TextView) findViewById(R.id.to_time);
		s = constructTime(toHour, toMinute);
		to_time.setText(s);

		EditText what = (EditText) findViewById(R.id.description);
		what.setText(description);

		// construct spinner item array by getting all categories from database
		Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
		CategoryManager category_manager = Manager.getInstance()
				.getCategoryManager();
		ArrayList<Category> list = category_manager.readAllCategory();
		Iterator<Category> itr = list.iterator();
		ArrayList<String> options = new ArrayList<String>();
		options.add(EventActivity.DEFAULT_CATEGORY);
		Category c;
		int pos = -1;
		int i = 0;
		while (itr.hasNext()) {
			c = itr.next();
			s = c.getName();
			// exclude from showing the default category
			if (!(s.equalsIgnoreCase(DEFAULT_CATEGORY))) {
				options.add(s);
				i++;
				if (s.equals(categoryName)) {
					pos = i;
				}
			}
		}
		// add spinner item array to spinner dynamically
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, options);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		if (i > 0 && pos != -1) {
			spinner.setSelection(pos);
			adapter.notifyDataSetChanged();
		}
		spinner.setOnItemSelectedListener(this);

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.event_layout);
		RelativeLayout.LayoutParams update_params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams remove_params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams return_params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// add a update button to the relative layout dynamically
		Button update_button = new Button(this);
		update_button.setText(R.string.update_event);
		update_button.setOnClickListener(new View.OnClickListener() {

			@Override
			// update event
			public void onClick(View view) {
				if (getData()) {
					if (verifyData()) {
						Calendar[] from = new Calendar[occurance];
						Calendar[] to = new Calendar[occurance];

						List<Event> list = null;
						boolean flag = true;
						Iterator<Event> iterator;
						Event e;

						// check event time conflict
						for (int i = index-1; i < occurance; i++) {
							from[i] = Calendar.getInstance();
							from[i].set(fromYear, fromMonth, fromDay, fromHour,
									fromMinute, 0);
							from[i].add(Calendar.DAY_OF_MONTH, 7 * (i-(index-1)));
							from[i].clear(Calendar.MILLISECOND);
							
							to[i] = Calendar.getInstance();
							to[i].set(toYear, toMonth, toDay, toHour, toMinute,
									0);
							to[i].add(Calendar.DAY_OF_MONTH, 7 * (i-(index-1)));
							to[i].clear(Calendar.MILLISECOND);

							list = manager.readEvents(from[i], to[i]);
							iterator = list.iterator();
							if (list.size() != 0) {
								while (iterator.hasNext()) {
									e = iterator.next();
									boolean b = contains(e.getStartDate(),
											e.getEndDate());
									if (!b) {
										flag = false;
										break;
									}
								}
							}
						}
						
						for (int i = index-2; i >= 0; i--) {
							from[i] = Calendar.getInstance();
							from[i].set(fromYear, fromMonth, fromDay, fromHour,
									fromMinute, 0);
							from[i].add(Calendar.DAY_OF_MONTH, 7 * (i-(index-1)));
							from[i].clear(Calendar.MILLISECOND);
							
							to[i] = Calendar.getInstance();
							to[i].set(toYear, toMonth, toDay, toHour, toMinute,
									0);
							to[i].add(Calendar.DAY_OF_MONTH, 7 * (i-(index-1)));
							to[i].clear(Calendar.MILLISECOND);

							list = manager.readEvents(from[i], to[i]);
							iterator = list.iterator();
							if (list.size() != 0) {
								while (iterator.hasNext()) {
									e = iterator.next();
									boolean b = contains(e.getStartDate(),
											e.getEndDate());
									if (!b) {
										flag = false;
										break;
									}
								}
							}
						}

						// resolve event time conflict
						if (flag) {
							removeEvent();
							for (int i = 0; i < occurance; i++) {
								e = new Event(from[i], to[i], categoryName,
										description, occurance, i + 1);
								manager.createEvent(e);
							}

							finish();
						} else {
							popup(EVENT_TIME_CONFLICT);
						}
					} else {
						popup(INVALID_USER_INPUT);
					}
				} else {
					popup(INCOMPLETE_USER_INPUT);
				}
			}
		});
		update_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		update_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layout.addView(update_button, update_params);

		// add a remove button to the relative layout dynamically
		Button remove_button = new Button(this);
		remove_button.setText(R.string.remove_event);
		remove_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				removeEvent();
				finish();

			}
		});
		remove_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		remove_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layout.addView(remove_button, remove_params);

		// add a return button to the relative layout dynamically
		Button return_button = new Button(this);
		return_button.setText(R.string.cancel);
		return_button.setOnClickListener(new View.OnClickListener() {

			@Override
			// return to last activity
			public void onClick(View v) {
				finish();
			}
		});
		return_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		return_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layout.addView(return_button, return_params);

	}

	// check whether instances are in the corresponding arrays or not
	private boolean contains(Calendar from, Calendar to) {
		boolean flag = false;
		for (int i = 0; i <= total - 1; i++) {
			if (from.getTime().toString().equals(oldfrom[i].getTime().toString()) && to.getTime().toString().equals(oldto[i].getTime().toString())) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	// remove event according to its start time and end time
	private void removeEvent() {
		for (int i = 0; i <= total - 1; i++) {
			manager.deleteEvent(oldfrom[i], oldto[i]);
		}
	}

}
