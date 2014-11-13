package edu.utsa.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.utsa.calendar.InteractiveArrayAdapter.ViewHolder;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ListActivity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the Class for Daily Activity View of Calendar. All the functionality
 * of Daily View are initiated by this class. 
 */

public class DailyViewActivity extends CalendarActivity {
	private InteractiveArrayAdapter mAdapter = null;
	private View mPrevious = null;
	private int mPreVposition = -1;
	private ListView mListView;
	private TextView mDayViewHeader;
	private Calendar mStartDate;
	private Calendar mEndDate;
	private Calendar mSelectedDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_view);
		mListView = (ListView) findViewById(R.id.listView1);
		mDayViewHeader = (TextView) findViewById(R.id.dayViewHeader);
		addListenerOnButton(mListView);
		mSelectedDate = Calendar.getInstance();
	}

	
	private void populateModel() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy", java.util.Locale.getDefault());
		if (mStartDate == null) {
			mStartDate = Calendar.getInstance();
			mEndDate = Calendar.getInstance();
		}
		mStartDate.setTime(mSelectedDate.getTime());
		mStartDate.set(Calendar.HOUR_OF_DAY, 0);
		mStartDate.set(Calendar.MINUTE, 0);
		mEndDate.setTime(mSelectedDate.getTime());
		mEndDate.set(Calendar.HOUR_OF_DAY, 23);
		mEndDate.set(Calendar.MINUTE, 59);

		String header = sdf.format(mSelectedDate.getTime());
		mDayViewHeader.setText(header);

		List<Event> events = Manager.getInstance().getEventManager().readEvents(mStartDate, mEndDate);
		List<DailyViewModel> modelList = new ArrayList<DailyViewModel>();
		for (String hour : CalendarData.s12Hours) {

			DailyViewModel dailyModel = new DailyViewModel();
			dailyModel.setTimeLebel(hour);
			modelList.add(dailyModel);

		}

		for (Event event : events) {
			Calendar startDateOfEvent = event.getStartDate();
			int startHourOfDay = startDateOfEvent.get(Calendar.HOUR_OF_DAY);
			int sday = startDateOfEvent.get(Calendar.DAY_OF_MONTH);
			DailyViewModel tempDailyModel = modelList.get(startHourOfDay);

			//tempDailyModel.addEvent(event);
			Calendar endDateOfEvent = event.getEndDate();
			int endHourOfDay = endDateOfEvent.get(Calendar.HOUR_OF_DAY);
			int eday = endDateOfEvent.get(Calendar.DAY_OF_MONTH);
			int currentday = mStartDate.get(Calendar.DAY_OF_MONTH);

			if (sday == eday && (endHourOfDay == startHourOfDay)) {
					tempDailyModel = modelList.get(startHourOfDay);
					tempDailyModel.addEvent(event);
				
			}
			else if (sday == eday && (endHourOfDay > startHourOfDay)) {
				while (startHourOfDay <= endHourOfDay) {
					
					tempDailyModel = modelList.get(startHourOfDay);
					tempDailyModel.addEvent(event);
					startHourOfDay += 1;
				}
			} else if ((currentday == sday) && (sday < eday)) {

				while (startHourOfDay < 24) {
					
					tempDailyModel = modelList.get(startHourOfDay);
					tempDailyModel.addEvent(event);
					startHourOfDay += 1;
				}
			} else if ((currentday == eday) && (sday < eday)) {
				startHourOfDay = 0;
				while (startHourOfDay <= endHourOfDay) {
					
					tempDailyModel = modelList.get(startHourOfDay);
					tempDailyModel.addEvent(event);
					startHourOfDay += 1;
				}
			}

		}
		
		/*
		 * Initialized the adapter with model list
		 */
		mAdapter = new InteractiveArrayAdapter(this, modelList);
		mListView.setAdapter(mAdapter);

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void addListenerOnButton(ListView pListView) {

		ImageButton imageButtonNext = (ImageButton) findViewById(R.id.nextButton);

		imageButtonNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View pView) {
				mSelectedDate.add(Calendar.DATE, 1);
				Intent intent = getIntent();
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				overridePendingTransition(R.anim.animation_slide_in_left, R.anim.animation_slide_out_right);

			}

		});

		ImageButton imageButtonPrev = (ImageButton) findViewById(R.id.prevButton);

		imageButtonPrev.setOnClickListener(new OnClickListener() {

			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onClick(View pView) {
				mSelectedDate.add(Calendar.DATE, -1);
				Intent intent = getIntent();
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				overridePendingTransition(R.anim.animation_slide_in_right, R.anim.animation_slide_out_left);
			}

		});

		pListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, final int position, long id) {
				DailyViewModel item = (DailyViewModel) mAdapter.getItem(position);
				
				// empty space for creating event
				
				if (item.getEvents().isEmpty()) {
					item.setCreateLabel(" + New Event");
					Event dummyEvent = new Event(-100, null, null, "", " + New Event", 0, 0);
					item.addEvent(dummyEvent);
				}

				// holder to improve performance
				final ViewHolder holder = (ViewHolder) v.getTag();
				holder.grid.setBackgroundResource(R.drawable.list_selected);
				if (mPreVposition == position) {

				} else if (mPrevious != null) {
					mPrevious.setBackgroundResource(getResources().getColor(android.R.color.transparent));

					DailyViewModel itemOld = (DailyViewModel) mAdapter.getItem(mPreVposition);
					itemOld.removeEvents();

					mPrevious = holder.grid;
				} else {
					mPrevious = holder.grid;
				}

				mPreVposition = position;

				mAdapter.notifyDataSetChanged();
				
				/* notify all the child adapter on change data of 
				 * parent adapter
				 */
				List<ArrayAdapter<Event>> adapterList = mAdapter.getAdapterList();

				for (ArrayAdapter<Event> arrayAdapter : adapterList) {

					arrayAdapter.notifyDataSetChanged();
				}

			}
		});

	}

	/*
	 * To under stand the android life activity life cycle
	 * @see Activity {@link #onResume() * onResume} method
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			Long time = bundle.getLong("selectedDay");
			if (time != null && time > 0) {
				mSelectedDate = Calendar.getInstance();
				mSelectedDate.setTimeInMillis(time);
				time = Long.valueOf(0);
				getIntent().putExtra("selectedDay", time);

			}
		}
		
		// on resume reset the previous position and view to null 
		populateModel();
		mPreVposition = -1;
		mPrevious = null;
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setSelectedNavigationItem(1);
	}

}
