package edu.utsa.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Purpose of this class is to draw weekly activity properly. It will on demand
 * retrieve event information from database based on start date and end date
 */

public class WeeklyViewActivity extends CalendarActivity {

	private GridView gridView;
	private TextView weekViewHeader;
	private Calendar startDate;
	private Calendar endDate;

	private static int DAY_IN_NEXT_WEEK = 8;
	private static int DAY_IN_WEEK = 7;

	private String[] weekWorks;
	private String[] dayInWeek = { "SUN", "MON", "TUE", "WED", "THU", "FRI",
			"SAT" };
	private static int[] weekWorkColor = new int[200];
	private static int[] eventID = new int[200];

	/**
	 * Set the startDate and endDate variable to the start date and end date of
	 * this week
	 */
	private void setDate() {

		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance();
		int dayOfWeek = startDate.get(Calendar.DAY_OF_WEEK);

		if (Calendar.SUNDAY == dayOfWeek) {
			startDate.add(Calendar.DATE, 0);
			endDate.add(Calendar.DATE, 6);

		} else if (Calendar.MONDAY == dayOfWeek) {
			startDate.add(Calendar.DATE, -1);
			endDate.add(Calendar.DATE, 5);

		} else if (Calendar.TUESDAY == dayOfWeek) {
			startDate.add(Calendar.DATE, -2);
			endDate.add(Calendar.DATE, 4);

		} else if (Calendar.WEDNESDAY == dayOfWeek) {
			startDate.add(Calendar.DATE, -3);
			endDate.add(Calendar.DATE, 3);

		} else if (Calendar.THURSDAY == dayOfWeek) {
			startDate.add(Calendar.DATE, -4);
			endDate.add(Calendar.DATE, 2);

		} else if (Calendar.FRIDAY == dayOfWeek) {
			startDate.add(Calendar.DATE, -5);
			endDate.add(Calendar.DATE, 1);

		} else if (Calendar.SATURDAY == dayOfWeek) {
			startDate.add(Calendar.DATE, -6);
			endDate.add(Calendar.DATE, 0);

		} else {
			//System.out.println("Day format not found");
		}

		// set time to initial values
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);

		endDate.set(Calendar.HOUR_OF_DAY, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		endDate.set(Calendar.MILLISECOND, 999);

	}

	/**
	 * Set the variable text to populate new event information
	 */

	private void setWeekWorks() {
		weekWorks = new String[] {

		"Time", "", "", "", "", "", "", "", "12am", "", "", "", "", "", "", "",
				" 1am", "", "", "", "", "", "", "", " 2am", "", "", "", "", "",
				"", "", " 3am", "", "", "", "", "", "", "", " 4am", "", "", "",
				"", "", "", "", " 5am", "", "", "", "", "", "", "", " 6am", "",
				"", "", "", "", "", "", " 7am", "", "", "", "", "", "", "",
				" 8am", "", "", "", "", "", "", "", " 9am", "", "", "", "", "",
				"", "", "10am", "", "", "", "", "", "", "", "11am", "", "", "",
				"", "", "", "", "12pm", "", "", "", "", "", "", "", " 1pm", "",
				"", "", "", "", "", "", " 2pm", "", "", "", "", "", "", "",
				" 3pm", "", "", "", "", "", "", "", " 4pm", "", "", "", "", "",
				"", "", " 5pm", "", "", "", "", "", "", "", " 6pm", "", "", "",
				"", "", "", "", " 7pm", "", "", "", "", "", "", "", " 8pm", "",
				"", "", "", "", "", "", " 9pm", "", "", "", "", "", "", "",
				"10pm", "", "", "", "", "", "", "", "11pm", "", "", "", "", "",
				"", "   "

		};

		// set date to the week days
		for (int i = 0; i < 7; i++) {
			int day = startDate.get(Calendar.DAY_OF_MONTH) + i;
			if (day > startDate.getActualMaximum(Calendar.DAY_OF_MONTH)) {
				day = (startDate.get(Calendar.DAY_OF_MONTH) + i)
						% startDate.getActualMaximum(Calendar.DAY_OF_MONTH);
			}

			weekWorks[i + 1] = dayInWeek[i] + " " + day;

		}

		weekWorkColor = new int[200]; // storage for hold color information
		eventID = new int[200]; // storage for holding event information
	}

	/**
	 * This method reads event information from the database and populate them
	 */
	private void populateFields() {

		setWeekWorks();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy",
				java.util.Locale.getDefault());
		weekViewHeader.setText(sdf.format(startDate.getTime()) + " to "
				+ sdf.format(endDate.getTime()));

		for (int i = 0; i < DAY_IN_WEEK; i++) {

			// get the start time of this day to populate
			Calendar dayStartDate = (Calendar) startDate.clone();
			dayStartDate.add(Calendar.DATE, i);

			// get the end time of this day to populate
			Calendar dayEndDate = (Calendar) dayStartDate.clone();
			dayEndDate.set(Calendar.HOUR_OF_DAY, 23);
			dayEndDate.set(Calendar.MINUTE, 59);
			dayEndDate.set(Calendar.SECOND, 59);
			dayEndDate.set(Calendar.MILLISECOND, 999);

			List<Event> events = Manager.getInstance().getEventManager()
					.readEvents(dayStartDate, dayEndDate);

			// populate based on the event retrieved
			for (Event ev : events) {

				
				int startHourMarker = 0;
				int endHourMarker = 0;
				// if the event start before the day and end in this day
				if ((ev.getStartDate().getTimeInMillis() <= dayStartDate
						.getTimeInMillis())
						&& (ev.getEndDate().getTimeInMillis() <= dayEndDate
								.getTimeInMillis())) {
					startHourMarker = dayStartDate
							.getActualMinimum(Calendar.HOUR_OF_DAY);
					endHourMarker = ev.getEndDate().get(Calendar.HOUR_OF_DAY);
				}
				// if the event start in this month and end after this month
				else if ((ev.getStartDate().getTimeInMillis() >= dayStartDate
						.getTimeInMillis())
						&& (ev.getEndDate().getTimeInMillis() >= dayEndDate
								.getTimeInMillis())) {
					startHourMarker = ev.getStartDate().get(
							Calendar.HOUR_OF_DAY);
					endHourMarker = dayEndDate
							.getActualMaximum(Calendar.HOUR_OF_DAY);

				}
				// if the event start and end within this month
				else if ((ev.getStartDate().getTimeInMillis() >= dayStartDate
						.getTimeInMillis())
						&& (ev.getEndDate().getTimeInMillis() <= dayEndDate
								.getTimeInMillis())) {
					startHourMarker = ev.getStartDate().get(
							Calendar.HOUR_OF_DAY);
					endHourMarker = ev.getEndDate().get(Calendar.HOUR_OF_DAY);

				}
				// if the event start before this month and end after this month
				else if ((ev.getStartDate().getTimeInMillis() < dayStartDate
						.getTimeInMillis())
						&& (ev.getEndDate().getTimeInMillis() > dayEndDate
								.getTimeInMillis())) {
					startHourMarker = dayStartDate
							.getActualMinimum(Calendar.HOUR_OF_DAY);
					endHourMarker = dayEndDate
							.getActualMaximum(Calendar.HOUR_OF_DAY);

				}

				for (int j = startHourMarker; j <= endHourMarker; j++) {

					if (weekWorks[9 + 8 * j + i].equals("")){
						if(ev.getDescription().length()<=11){
							weekWorks[9 + 8 * j + i] = ev.getDescription();
						}
						else{
							weekWorks[9 + 8 * j + i] = ev.getDescription().substring(0, 11)+"..";
						}
						eventID[9 + 8 * j + i] = ev.getID();
						// set the category color
						weekWorkColor[9 + 8 * j + i] = ev.getColor();
					
					}
					else{
						
						weekWorks[9 + 8 * j + i] = weekWorks[9 + 8 * j + i].concat("*");
					}
					
				}

			}

		}


		gridView.setAdapter(new CalendarEntryAdapterWeek(this, weekWorks,
				weekWorkColor));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weekly_view);

		gridView = (GridView) findViewById(R.id.gridViewWeekly);
		gridView.setBackgroundColor(Color.GRAY);
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
		weekViewHeader = (TextView) findViewById(R.id.weekViewHeader);
		this.setDate();
		addListenerOnButton(gridView);

	}

	protected void onResume() {
		super.onResume();
		this.populateFields();
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setSelectedNavigationItem(2);
		
	}

	/**
	 * Action listen for the user actions
	 * 
	 * @param gridView
	 *            gird view to listen
	 */
	public void addListenerOnButton(GridView gridView) {

		ImageButton imageButtonNext = (ImageButton) findViewById(R.id.nextImageButton);

		imageButtonNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(WeeklyViewActivity.this, "Loading ...",
						Toast.LENGTH_SHORT).show();
				startDate.add(Calendar.DATE, 7);
				endDate.add(Calendar.DATE, 7);
				WeeklyViewActivity.this.onResume();
			}

		});

		ImageButton imageButtonPrev = (ImageButton) findViewById(R.id.prevImageButton);

		imageButtonPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(WeeklyViewActivity.this, "Loading ...",
						Toast.LENGTH_SHORT).show();
				startDate.add(Calendar.DATE, -7);
				endDate.add(Calendar.DATE, -7);
				WeeklyViewActivity.this.onResume();

			}

		});

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				if (eventID[position] != 0) {
					
					Intent intent = new Intent(WeeklyViewActivity.this,
							ModifyEventActivity.class);
					intent.putExtra("event_id", eventID[position]);
					startActivity(intent);
				} else if (weekWorks[position].equals("")) {
					
					Intent intent = new Intent(WeeklyViewActivity.this,
							NewEventActivity.class);
					startActivity(intent);
				}
				else if((position<=7)&&(position>0)){
					
					
					Intent intent = new Intent(
							WeeklyViewActivity.this,
							DailyViewActivity.class);
					Calendar newDate = (Calendar)startDate.clone();
					newDate.add(Calendar.DAY_OF_MONTH, position-1 );
					
					intent.putExtra("selectedDay",
							newDate.getTimeInMillis());
					startActivity(intent);
					
					
				}

			}
		});

	}

}
