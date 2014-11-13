package edu.utsa.calendar;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * NewCategoryActivity in responsible of creating category on user's demand
 */
public class NewCategoryActivity extends Activity {

	private String name;
	private String description;
	private int color;

	private String NAME_IS_TAKEN;
	private String INCOMPLETE_USER_INPUT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		NAME_IS_TAKEN = getResources().getString(
				R.string.name_is_taken);
		INCOMPLETE_USER_INPUT = getResources().getString(
				R.string.incomplte_input);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_category);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_category, menu);
		return true;
	}

	// collect user inputs
	private void getData() {
		EditText editText = (EditText) findViewById(R.id.name_text);
		name = editText.getText().toString();

		editText = (EditText) findViewById(R.id.description_text);
		description = editText.getText().toString();
	}

	// Verify user inputs
	private boolean verifyData() {
		if (name == null || name.isEmpty() || description == null
				|| description.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Create category by going through getting user inputs, verifying user inputs
	 * and resolving category name conflict
	 */
	public void createCategory(View view) {
		getData();
		if (verifyData()) {
			CategoryManager manager = Manager.getInstance()
					.getCategoryManager();
			ArrayList<Category> list = manager.readCategory(name);
			if (list.size() > 0) {
				popup(NAME_IS_TAKEN);
			} else {
				Category c = new Category(color, name, description);
				manager.addCategory(c);
				finish();
			}
		} else {
			popup(INCOMPLETE_USER_INPUT);
		}
	}

	// give user feedback when something goes wrong
	private void popup(CharSequence text) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	/**
	 * Return to the last activity
	 */
	public void cancel(View view) {
		finish();
	}

}
