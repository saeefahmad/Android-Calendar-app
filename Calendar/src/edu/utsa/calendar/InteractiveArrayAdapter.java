package edu.utsa.calendar;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.utsa.calendar.R;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main adapter class for Daily Activity View of Calendar. 
 * For each model it create child adapter for multiple {@link Event} instances.
 */
public class InteractiveArrayAdapter extends ArrayAdapter<DailyViewModel> {

	private final List<DailyViewModel> mList;
	private final Activity mContext;
	private InteractiveArrayAdapter mInteractiveArrayAdapter;
	private List<ArrayAdapter<Event>> mAdapterList = new ArrayList<ArrayAdapter<Event>>();

	public InteractiveArrayAdapter(Activity context, List<DailyViewModel> list) {
		super(context, R.layout.list_item, list);
		this.mContext = context;
		this.mList = list;
		mInteractiveArrayAdapter = this;
	}
	// Static holder
	static class ViewHolder {
		protected TextView text1;
		protected View grid;
		protected CheckBox checkbox;
	}

	static class ViewHolderEvent {
		protected TextView text;

	}

	public List<ArrayAdapter<Event>> getAdapterList() {
		return mAdapterList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = mContext.getLayoutInflater();
			view = inflator.inflate(R.layout.list_item, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text1 = (TextView) view.findViewById(R.id.textView1);
			GridView gView = (GridView) view.findViewById(R.id.dailyEventlist);

			gView.setNumColumns(3);

			final DailyViewModel dailyViewModel = getItem(position);
			final ArrayAdapter<Event> adapter = getNewAdapter(dailyViewModel);
			mAdapterList.add(adapter);
			gView.setAdapter(adapter);

			gView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

					if (dailyViewModel.getEvents().size() >= position) {
						Event gridEvent = adapter.getItem(position);
						if (gridEvent.getID() == -100) {
							Intent intent = new Intent(mContext, NewEventActivity.class);

							mContext.startActivity(intent);
						} else {
							Intent intent = new Intent(mContext, ModifyEventActivity.class);
							intent.putExtra("event_id", gridEvent.getID());
							mContext.startActivity(intent);
						}
					} else {

					}

					mInteractiveArrayAdapter.upDateChange();

				}
			});
			viewHolder.grid = gView;
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}

		ViewHolder holder = (ViewHolder) view.getTag();
		if (mList.size() >= position) {
			DailyViewModel dailyViewModel = getItem(position);

			holder.text1.setText(dailyViewModel.getTimeLebel());
			List<Event> events = dailyViewModel.getEvents();
			final ArrayAdapter<Event> adapter = getNewAdapter(dailyViewModel);
			((GridView) holder.grid).setAdapter(adapter);

		
		}
		return view;
	}

	
	private ArrayAdapter<Event> getNewAdapter(final DailyViewModel dailyViewModel) {
		final ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(this.mContext, R.layout.event_list_entry,
				dailyViewModel.getEvents()) {

			@Override
			public Event getItem(int pPosition) {
				if (dailyViewModel.getEvents().size() >= pPosition) {
					return dailyViewModel.getEvents().get(pPosition);
				} else
					return null;
			}

			@Override
			public View getView(int pPosition, View pConvertView, ViewGroup pParent) {
				View localView = null;
				if (pConvertView == null) {
					LayoutInflater inflator = mContext.getLayoutInflater();
					localView = inflator.inflate(R.layout.event_list_entry, null);
					ViewHolderEvent viewHolder = new ViewHolderEvent();
					viewHolder.text = (TextView) localView.findViewById(R.id.textViewEvent);
					localView.setTag(viewHolder);
				} else {
					localView = pConvertView;
				}
				// viewholder is used to improve performances
				ViewHolderEvent localHolder = (ViewHolderEvent) localView.getTag();
				localHolder.text.setText("");
				Event item = getItem(pPosition);
				if (item != null) {

					localHolder.text.setText(item.getDescription());
					if (item.getID() == -100) {
						// dummy event, empty space on click provide facility to create new Event
					} else {
						int sMinute = item.getStartDate().get(Calendar.MINUTE);
						int eMinute = item.getEndDate().get(Calendar.MINUTE);
						localHolder.text.setText(item.getDescription() + "  [ " + sMinute + " - " + eMinute + " ]");
						localHolder.text.setBackgroundColor(item.getColor());
					}
				} else {
					localHolder.text.setText("");
				}
				return localView;
			}
		};
		return adapter;
	}

	@Override
	public DailyViewModel getItem(int pPosition) {
		return mList.get(pPosition);
	}

	public void upDateChange() {
		mInteractiveArrayAdapter.notifyDataSetChanged();
	}

}
