package edu.utsa.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class manages events. Main purpose of this class is to save, read,
 * delete, update events from the database.
 * 
 */
public class EventManager {

	private DatabaseHelper storageHandler;
	private CategoryManager categoryManager;

	/**
	 * Constructor
	 */
	public EventManager(DatabaseHelper storageHandler,
			CategoryManager categoryManager) {
		super();
		this.storageHandler = storageHandler;
		this.categoryManager = categoryManager;
	}

	/**
	 * Save an event to database
	 * @param event
	 */
	public void createEvent(Event event) {
		// Query for create event

		SQLiteDatabase db = storageHandler.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(storageHandler.getEventsStartTime(), event.getStartDate()
				.getTimeInMillis());
		values.put(storageHandler.getEventsEndTime(), event.getEndDate()
				.getTimeInMillis());
		values.put(storageHandler.getEventsDescription(),
				event.getDescription());
		values.put(storageHandler.getEVENTS_CATEGORY(), event.getCategoryID());
		values.put(storageHandler.getEVENTS_TOTAL_OCCURANCE(),
				event.getTotalOccurance());
		values.put(storageHandler.getEVENTS_OCCURANCE_INDEX(),
				event.getOccuranceIndex());

		// Inserting Row
		db.insert(storageHandler.getEventTableName(), null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Read events from the database from start date to end date
	 */
	public List<Event> readEvents(java.util.Calendar sDate,
			java.util.Calendar eDate) {
		List<Event> eventList = new ArrayList<Event>();

		// Read all entry for category table
		List<Category> categoryList = categoryManager.readAllCategory();

		String selectQueryStartTime = "SELECT  * FROM "
				+ storageHandler.getEventTableName() + " WHERE "
				+ storageHandler.getEventsStartTime() + " >= "
				+ sDate.getTimeInMillis() + " AND "
				+ storageHandler.getEventsEndTime() + " <= "
				+ eDate.getTimeInMillis();

		SQLiteDatabase db = storageHandler.getWritableDatabase();
		Cursor cursorStartTime = db.rawQuery(selectQueryStartTime, null);

		// looping through all rows and adding to list
		if (cursorStartTime.moveToFirst()) {
			do {
				Calendar startDate = Calendar.getInstance();
				startDate.setTimeInMillis(Long.valueOf(
						cursorStartTime.getString(2)).longValue());
				Calendar endDate = Calendar.getInstance();
				endDate.setTimeInMillis(Long.valueOf(
						cursorStartTime.getString(3)).longValue());

				Event event = new Event(Integer.parseInt(cursorStartTime
						.getString(0)), startDate, endDate,
						cursorStartTime.getString(4),
						cursorStartTime.getString(1),
						Integer.parseInt(cursorStartTime.getString(5)),
						Integer.parseInt(cursorStartTime.getString(6)));

				// Set the category color
				for (Category ct : categoryList) {

					if (ct.getName().equals(event.getCategoryID())) {
						event.setColor(ct.getColor());
					}

				}

				// Adding contact to list
				eventList.add(event);

			} while (cursorStartTime.moveToNext());
		}

		// Select all events with end time between sDate and eDate
		String selectQueryEndTime = "SELECT  * FROM "
				+ storageHandler.getEventTableName() + " WHERE "
				+ storageHandler.getEventsStartTime() + " < "
				+ sDate.getTimeInMillis() + " AND "
				+ storageHandler.getEventsEndTime() + " <= "
				+ eDate.getTimeInMillis() + " AND "
				+ storageHandler.getEventsEndTime() + " >= "
				+ sDate.getTimeInMillis();

		Cursor cursorEndTime = db.rawQuery(selectQueryEndTime, null);

		// looping through all rows and adding to list
		if (cursorEndTime.moveToFirst()) {
			do {
				Calendar startDate = Calendar.getInstance();
				startDate.setTimeInMillis(Long.valueOf(
						cursorEndTime.getString(2)).longValue());
				Calendar endDate = Calendar.getInstance();
				endDate.setTimeInMillis(Long
						.valueOf(cursorEndTime.getString(3)).longValue());

				Event event = new Event(Integer.parseInt(cursorEndTime
						.getString(0)), startDate, endDate,
						cursorEndTime.getString(4), cursorEndTime.getString(1),
						Integer.parseInt(cursorEndTime.getString(5)),
						Integer.parseInt(cursorEndTime.getString(6)));

				// Set the category color
				for (Category ct : categoryList) {

					if (ct.getName().equals(event.getCategoryID())) {
						event.setColor(ct.getColor());
					}

				}

				// Adding contact to list
				eventList.add(event);

			} while (cursorEndTime.moveToNext());
		}

		// Select all events with start time between sDate and eDate
		selectQueryEndTime = "SELECT  * FROM "
				+ storageHandler.getEventTableName() + " WHERE "
				+ storageHandler.getEventsStartTime() + " > "
				+ sDate.getTimeInMillis() + " AND "
				+ storageHandler.getEventsStartTime() + " <= "
				+ eDate.getTimeInMillis() + " AND "
				+ storageHandler.getEventsEndTime() + " > "
				+ eDate.getTimeInMillis();

		cursorEndTime = db.rawQuery(selectQueryEndTime, null);

		// looping through all rows and adding to list
		if (cursorEndTime.moveToFirst()) {
			do {
				Calendar startDate = Calendar.getInstance();
				startDate.setTimeInMillis(Long.valueOf(
						cursorEndTime.getString(2)).longValue());
				Calendar endDate = Calendar.getInstance();
				endDate.setTimeInMillis(Long
						.valueOf(cursorEndTime.getString(3)).longValue());

				Event event = new Event(Integer.parseInt(cursorEndTime
						.getString(0)), startDate, endDate,
						cursorEndTime.getString(4), cursorEndTime.getString(1),
						Integer.parseInt(cursorEndTime.getString(5)),
						Integer.parseInt(cursorEndTime.getString(6)));

				// Set the category color
				for (Category ct : categoryList) {

					if (ct.getName().equals(event.getCategoryID())) {
						event.setColor(ct.getColor());
					}

				}

				// Adding contact to list
				eventList.add(event);

			} while (cursorEndTime.moveToNext());
		}

		// Select all events that start before sDate and end after eDate
		selectQueryEndTime = "SELECT  * FROM "
				+ storageHandler.getEventTableName() + " WHERE "
				+ storageHandler.getEventsStartTime() + " < "
				+ sDate.getTimeInMillis() + " AND "
				+ storageHandler.getEventsEndTime() + " > "
				+ eDate.getTimeInMillis();

		cursorEndTime = db.rawQuery(selectQueryEndTime, null);

		// looping through all rows and adding to list
		if (cursorEndTime.moveToFirst()) {
			do {
				Calendar startDate = Calendar.getInstance();
				startDate.setTimeInMillis(Long.valueOf(
						cursorEndTime.getString(2)).longValue());
				Calendar endDate = Calendar.getInstance();
				endDate.setTimeInMillis(Long
						.valueOf(cursorEndTime.getString(3)).longValue());

				Event event = new Event(Integer.parseInt(cursorEndTime
						.getString(0)), startDate, endDate,
						cursorEndTime.getString(4), cursorEndTime.getString(1),
						Integer.parseInt(cursorEndTime.getString(5)),
						Integer.parseInt(cursorEndTime.getString(6)));

				// Set the category color
				for (Category ct : categoryList) {

					if (ct.getName().equals(event.getCategoryID())) {
						event.setColor(ct.getColor());
					}

				}

				// Adding contact to list
				eventList.add(event);

			} while (cursorEndTime.moveToNext());
		}

		// return contact list
		return eventList;
	}

	
	/**
	 * Read events from the database that belong to a particular category
	 * @param categoryId category id
	 * @return list of events that are in the same category
	 */
	public List<Event> readEventsByCategory(String categoryID) {
		List<Event> eventList = new ArrayList<Event>();
	
		// Read all entry for category table
				List<Category> categoryList = categoryManager.readAllCategory();
		// Read all events belong to a particular category
		
		String selectQueryCategory = "SELECT * FROM "
				+ storageHandler.getEventTableName()+ " WHERE "
				+ storageHandler.getEVENTS_CATEGORY() + "='" + categoryID + "'";
		SQLiteDatabase db = storageHandler.getWritableDatabase();
		Cursor cursorCategory = db.rawQuery(selectQueryCategory, null);

		// looping through all rows and adding to list
		if (cursorCategory.moveToFirst()) {
			do {
				Calendar startDate = Calendar.getInstance();
				startDate.setTimeInMillis(Long.valueOf(
						cursorCategory.getString(2)).longValue());
				Calendar endDate = Calendar.getInstance();
				endDate.setTimeInMillis(Long.valueOf(
						cursorCategory.getString(3)).longValue());

				Event event = new Event(Integer.parseInt(cursorCategory
						.getString(0)), startDate, endDate,
						cursorCategory.getString(4),
						cursorCategory.getString(1),
						Integer.parseInt(cursorCategory.getString(5)),
						Integer.parseInt(cursorCategory.getString(6)));

				// Set the category color
				for (Category ct : categoryList) {

					if (ct.getName().equals(event.getCategoryID())) {
						event.setColor(ct.getColor());
					}

				}

				// Adding contact to list
				eventList.add(event);

			} while (cursorCategory.moveToNext());
		}

	
		// return contact list
		return eventList;
	}
	
	
	
	/**
	 * Return the list of all conflicted events or null if no conflict occurs
	 */
	public List<Event> getConflictedEvents(java.util.Calendar sDate,
			java.util.Calendar eDate) {

		// return contact list
		return readEvents(sDate, eDate);
	}

	/**
	 * Delete an events with startDate and endDate
	 */
	public void deleteEvent(Calendar startDate, Calendar endDate) {
		SQLiteDatabase db = storageHandler.getWritableDatabase();

		// Deleting events
		db.delete(storageHandler.getEventTableName(),
				storageHandler.getEventsStartTime() + "= ? AND "
						+ storageHandler.getEventsEndTime() + " = ?",
				new String[] { String.valueOf(startDate.getTimeInMillis()),
						String.valueOf(endDate.getTimeInMillis()) });
		db.close();

	}

	/**
	 * Update an event by event id
	 */
	public void updateEvent(int eventId, Event event) {
		SQLiteDatabase db = storageHandler.getWritableDatabase();

		String strFilter = "_id=" + eventId;
		ContentValues args = new ContentValues();
		args.put(storageHandler.getEventsDescription(), event.getDescription());
		db.update(storageHandler.getEventTableName(), args, strFilter, null);

		db.close();

	}

	/**
	 * Get an event by event id
	 */

	public Event getEventById(int eventId) {

		String selectQuery = "SELECT  * FROM "
				+ storageHandler.getEventTableName() + " WHERE "
				+ storageHandler.getEventsKeyId() + " = " + eventId;

		SQLiteDatabase db = storageHandler.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// Read all entry for category table
		List<Category> categoryList = categoryManager.readAllCategory();
		Event event = null;
		if (cursor.moveToFirst()) {
			do {
				Calendar startDate = Calendar.getInstance();
				startDate.setTimeInMillis(Long.valueOf(cursor.getString(2))
						.longValue());
				Calendar endDate = Calendar.getInstance();
				endDate.setTimeInMillis(Long.valueOf(cursor.getString(3))
						.longValue());

				event = new Event(Integer.parseInt(cursor.getString(0)),
						startDate, endDate, cursor.getString(4),
						cursor.getString(1), Integer.parseInt(cursor
								.getString(5)), Integer.parseInt(cursor
								.getString(6)));

				// Set the category color
				for (Category ct : categoryList) {

					if (ct.getName().equals(event.getCategoryID())) {
						event.setColor(ct.getColor());
					}

				}

			} while (cursor.moveToNext());
		}

		return event;

	}

}