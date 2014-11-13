package edu.utsa.calendar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * This is the Presentation Model Class for Daily Activity View of Calendar.
 * Daily Activity adapter class uses this model.
 */


public class DailyViewModel {
	private List<Event> mEvents = new ArrayList<Event>();
	private String mTimeLebel;
	
	private String sCreateLabel;

	/**
	 * add event to event list
	 * 
	 */
	public void addEvent(Event pEvent) {
		mEvents.add(pEvent);
	}

	/**
	 * @return event list
	 * 
	 */
	public List<Event> getEvents() {
		return mEvents;
	}
	public void setTimeLebel(String pTimeLebel) {
		mTimeLebel = pTimeLebel;
	}

	/**
	 * @return time label text
	 * 
	 */
	public String getTimeLebel() {
		return mTimeLebel;
	}
	/**
	 * @return create label text
	 * 
	 */
	public String getCreateLabel() {
		return sCreateLabel;
	}
	
	/**
	 * Create label for creating new Event
	 */
	public void setCreateLabel(String pCreateLabel) {
		sCreateLabel = pCreateLabel;
	}
	/**
	 * Remove all event from the event List
	 */
	public void removeEvents(){
		mEvents.clear();
	}
}
