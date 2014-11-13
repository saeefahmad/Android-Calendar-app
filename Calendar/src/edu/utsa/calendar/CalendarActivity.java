package edu.utsa.calendar;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.widget.ArrayAdapter;

/**
 * This class relate to the Menu Action Bar and it also start the calendar activity from last saved view
 */
public class CalendarActivity extends Activity {
	private int mIsUserInitiatedNavItemSelection = 0;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		String[] actions = new String[] { "Select Action", "Daily View", "Weekly View ", "Monthly View", 
				"Create Event", "Create Category", "Delete Category" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), 
				android.R.layout.simple_spinner_dropdown_item, actions);

		/** Enabling dropdown list navigation for the action bar */
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setTitle("");

		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {

				// save last stay view (Daily, Weekly, Monthly, Agenda) to preference
				if (mIsUserInitiatedNavItemSelection != itemPosition) {
					mIsUserInitiatedNavItemSelection = itemPosition;
					SharedPreferences mPrefs = getSharedPreferences("view", 0);
					Editor edit = mPrefs.edit();

					if (itemPosition == 0) {
						// do nothing
					} else if (itemPosition == 1) {

						edit.clear();
						edit.putInt("view_mode", MainActivity.DAILY_VIEW_MODE);
						edit.commit();

						Intent intent = new Intent(CalendarActivity.this, DailyViewActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						Calendar instance = Calendar.getInstance();
						intent.putExtra("selectedDay", instance.getTimeInMillis());
						startActivity(intent);
					} else if (itemPosition == 2) {
						edit.clear();
						edit.putInt("view_mode", MainActivity.WEEKLY_VIEW_MODE);
						edit.commit();

						Intent intent = new Intent(CalendarActivity.this, WeeklyViewActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);

					} else if (itemPosition == 3) {

						edit.clear();
						edit.putInt("view_mode", MainActivity.MONTHLY_VIEW_MODE);
						edit.commit();

						Intent intent = new Intent(CalendarActivity.this, MonthlyViewActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
					} else if (itemPosition == 4) {
						Intent intent = new Intent(CalendarActivity.this, NewEventActivity.class);
						startActivity(intent);

					} else if (itemPosition == 5) {
						Intent intent = new Intent(CalendarActivity.this, NewCategoryActivity.class);
						startActivity(intent);
					} else if (itemPosition == 6) {
						Intent intent = new Intent(CalendarActivity.this, ModifyCategoryActivity.class);
						startActivity(intent);
					}

				}
				return true;
			}
		};
		// Setting dropdown items and item navigation listener for the actionbar
		getActionBar().setListNavigationCallbacks(adapter, navigationListener);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar6));
		getActionBar().setIcon(getResources().getDrawable(R.drawable.calendar_icon));
		return true;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey("STATE_SELECTED_NAVIGATION_ITEM")) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("STATE_SELECTED_NAVIGATION_ITEM"));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		if(getActionBar()!=null){
		outState.putInt("STATE_SELECTED_NAVIGATION_ITEM", getActionBar().getSelectedNavigationIndex());
		}
	}

}
