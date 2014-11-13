package edu.utsa.calendar;

import java.util.ArrayList;
import java.util.Iterator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * ModifyCategoryActivity provides user the capability of removing category
 */
public class ModifyCategoryActivity extends Activity {

	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private String categoryName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_category);

		final CategoryManager manager = Manager.getInstance()
				.getCategoryManager();
		ArrayList<Category> list = manager.readAllCategory();
		final String PROMPT = getResources().getString(
				R.string.select_category_to_delete);
		final String INFO = getResources().getString(
				R.string.info);
		final String DELETE = getResources().getString(
				R.string.delete);
		final String RETURN = getResources().getString(
				R.string.cancel);
		final String PROMPT2 = getResources().getString(
				R.string.prompt);;
		final String DEFAULT_CATEGORY = "default";

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.delete_category_view);
		ArrayList<String> options = new ArrayList<String>();
		Category c;
		String s;

		// check if is there any category except the default category
		if (list.size() > 1) {
			Iterator<Category> itr = list.iterator();
			while (itr.hasNext()) {
				c = itr.next();
				s = c.getName();
				if (!(s.equalsIgnoreCase(DEFAULT_CATEGORY))) {
					options.add(s);
				}
			}

			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// add a textview to the relative layout dynamically
			TextView textview = new TextView(this);
			textview.setId(ONE);
			textview.setText(PROMPT);
			params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
			layout.addView(textview, params1);

			// add a spinner to the relative layout dynamically
			Spinner spinner = new Spinner(this);
			spinner.setId(TWO);
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
					android.R.layout.simple_spinner_item, options);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
					categoryName = (String) parent.getItemAtPosition(pos);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
			params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params2.addRule(RelativeLayout.BELOW, textview.getId());
			layout.addView(spinner, params2);

			// add a create button to the relative layout dynamically
			Button create_button = new Button(this);
			create_button.setId(THREE);
			create_button.setText(DELETE);
			create_button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (categoryName == null || categoryName.isEmpty()) {
						popup(PROMPT2);
					} else {
						manager.deleteCategory(categoryName);
						finish();
					}
				}
			});
			params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			layout.addView(create_button, params3);

			// add a return button to the relative layout dynamically
			Button return_button = new Button(this);
			return_button.setId(FOUR);
			return_button.setText(RETURN);
			return_button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();

				}
			});
			params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			layout.addView(return_button, params4);

		} else {
			// there is no other category except default category
			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			// add a textview to the relative layout dynamically
			TextView textview = new TextView(this);
			textview.setId(ONE);
			textview.setText(INFO);
			params1.addRule(RelativeLayout.CENTER_HORIZONTAL);

			// add a return button to the relative layout dynamically
			Button return_button = new Button(this);
			return_button.setId(TWO);
			return_button.setText(RETURN);
			params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			return_button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});

			layout.addView(textview, params1);
			layout.addView(return_button, params2);
		}
	}

	// give user feedback when something goes wrong
	private void popup(CharSequence text) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.modify_category, menu);
		return true;
	}

}
