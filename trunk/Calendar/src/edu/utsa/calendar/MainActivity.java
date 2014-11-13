package edu.utsa.calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

/**
 * Main activity class of our project. Initializes manager classes and database
 * class and render last viewed activity (monthly, daily or weekly)
 */
public class MainActivity extends Activity {

	public static final int DAILY_VIEW_MODE = 0;
	public static final int WEEKLY_VIEW_MODE = 1;
	public static final int MONTHLY_VIEW_MODE = 2;
	public static final int AGENDA_VIEW_MODE = 3;

	private SharedPreferences mPrefs;
	private int mCurViewMode;
	private CategoryManager categoryManager;
	private EventManager eventManager;
	private DatabaseHelper databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// First create the database
		databaseHelper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME);

		// Next create CategoryManager and EventManager
		categoryManager = new CategoryManager(databaseHelper);
		eventManager = new EventManager(databaseHelper, categoryManager);

		// Set into the Manager singleton class
		Manager.getInstance().setEventManager(eventManager);
		Manager.getInstance().setCategoryManager(categoryManager);

		// go to the last view user stayed
		mPrefs = getSharedPreferences("view", 0);
		mCurViewMode = mPrefs.getInt("view_mode", MONTHLY_VIEW_MODE);

		// start last saved view
		Intent firstView;
		switch (mCurViewMode) {
		case DAILY_VIEW_MODE:
			firstView = new Intent(this, DailyViewActivity.class);
			break;
		case WEEKLY_VIEW_MODE:
			firstView = new Intent(this, WeeklyViewActivity.class);
			break;
		case MONTHLY_VIEW_MODE:
			firstView = new Intent(this, MonthlyViewActivity.class);
			break;
		default:
			return;
		}

		startActivity(firstView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
