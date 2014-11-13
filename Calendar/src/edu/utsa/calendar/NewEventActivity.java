package edu.utsa.calendar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.RelativeLayout.LayoutParams;

/**
 * NewEventActivity is an Android activity in charge of rendering view,
 * receiving user inputs and creating events on demand.
 */
public class NewEventActivity extends EventActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		categoryName = DEFAULT_CATEGORY;
		manager = Manager.getInstance().getEventManager();
		final String EVENT_TIME_CONFLICT = getResources().getString(
				R.string.time_conflict);
		final String INVALID_USER_INPUT = getResources().getString(
				R.string.invalid_input);
		final String INCOMPLETE_USER_INPUT = getResources().getString(
				R.string.incomplte_input);

		Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
		// construct spinner item array by getting all categories from database
		CategoryManager manager = Manager.getInstance().getCategoryManager();
		ArrayList<Category> list = manager.readAllCategory();
		Iterator<Category> itr = list.iterator();
		ArrayList<String> options = new ArrayList<String>();
		options.add(EventActivity.DEFAULT_CATEGORY);
		Category c;
		String s;
		while (itr.hasNext()) {
			c = itr.next();
			s = c.getName();
			// exclude from showing the default category
			if (!(s.equalsIgnoreCase(DEFAULT_CATEGORY))) {
				options.add(s);
			}
		}

		// add spinner item array to spinner dynamically
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, options);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.event_layout);
		RelativeLayout.LayoutParams create_params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams return_params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		// add a create button to the relative layout dynamically
		Button create_button = new Button(this);
		create_button.setText(R.string.create_event);
		create_button.setOnClickListener(new View.OnClickListener() {

			@Override
			// create event
			public void onClick(View v) {
				if (getData()) {
					if (verifyData()) {
						Calendar[] from = new Calendar[occurance];
						Calendar[] to = new Calendar[occurance];

						EventManager manager = Manager.getInstance()
								.getEventManager();
						List<Event> list = null;
						boolean flag = true;

						// check event time conflict
						for (int i = 0; i < occurance; i++) {
							from[i] = Calendar.getInstance();
							from[i].set(fromYear, fromMonth, fromDay, fromHour,
									fromMinute, 0);
							from[i].add(Calendar.DAY_OF_MONTH, 7 * i);
							from[i].clear(Calendar.MILLISECOND);

							to[i] = Calendar.getInstance();
							to[i].set(toYear, toMonth, toDay, toHour, toMinute,
									0);
							to[i].add(Calendar.DAY_OF_MONTH, 7 * i);
							to[i].clear(Calendar.MILLISECOND);

							list = manager.getConflictedEvents(from[i], to[i]);
							if (list.size() > 0) {
								flag = false;
								break;
							}
						}

						// resolve event time conflict
						if (flag) {
							Event e;
							for (int i = 0; i < occurance; i++) {
								e = new Event(from[i], to[i], categoryName,
										description, occurance, i + 1);
								// save newly created event to database
								categoryName = EventActivity.DEFAULT_CATEGORY;
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
		create_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		create_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layout.addView(create_button, create_params);

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
}