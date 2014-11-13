package edu.utsa.calendar.test;

import java.util.Calendar;
import java.util.List;

import android.test.AndroidTestCase;
import edu.utsa.calendar.CategoryManager;
import edu.utsa.calendar.DatabaseHelper;
import edu.utsa.calendar.Event;
import edu.utsa.calendar.EventManager;

public class EventManagerTest extends AndroidTestCase {
	private DatabaseHelper mDatabaseHelper = null;
	private CategoryManager mCategoryManager = null;
	private EventManager mEventManager = null;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		mDatabaseHelper = new DatabaseHelper(getContext(),"CalendarDBTest");
		mCategoryManager = new CategoryManager(mDatabaseHelper);
		mEventManager = new EventManager(mDatabaseHelper, mCategoryManager);
		
	}

	@Override
	protected void tearDown() throws Exception {
		mDatabaseHelper.close();

		super.tearDown();
	}

	public void testCreateEvent() throws Exception {
		
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, 3);
		
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 5);
		
		Event event = new Event(startDate,endDate,"Meeting","Meeting with prof", 1,1);
		mEventManager.createEvent(event);
		
		
		startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, -3);
		
		endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, -1);
		
		event = new Event(startDate,endDate,"Dinner","Dinner with prof", 1,1);
		mEventManager.createEvent(event);
		
		
		startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, 6);
		
		endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 8);
		
		event = new Event(startDate,endDate,"Dinner","Dinner with colleague", 1,1);
		mEventManager.createEvent(event);
		
		startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, 10);
		
		endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 15);
		
		event = new Event(startDate,endDate,"Meeting","Meeting with colleague", 1,1);
		mEventManager.createEvent(event);
		
		
		startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, -30);
		
		endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 30);
		
		List<Event> readEvents = mEventManager.readEvents(startDate, endDate);
		
		assertEquals(4,readEvents.size());
		assertEquals(1,readEvents.get(0).getOccuranceIndex());
		assertEquals(1,readEvents.get(0).getTotalOccurance());
	
	}
	
	
	
	public void testReadEvent() throws Exception {
		
		
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, -2);
		
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 7);
		
		
		List<Event> events = mEventManager.readEvents(startDate, endDate);
		
		assertEquals(3, events.size());
		
	}
	
	
	public void testReadEventByCategory() throws Exception{
		
		List<Event> events = mEventManager.readEventsByCategory("Meeting");
		System.out.println(events.size());
		assertEquals(2, events.size());
		
		
	}
	
	public void testGetEventById() throws Exception {
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, 3);
		
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 5);
		
		List<Event> events = mEventManager.readEvents(startDate, endDate);
		
		int id = events.get(0).getID();
		
		Event event = mEventManager.getEventById(id);
		
		assertEquals(id, event.getID());
		assertEquals(events.get(0).getStartDate(),event.getStartDate() );
		assertEquals(events.get(0).getEndDate(),event.getEndDate() );
		assertEquals(events.get(0).getDescription(),event.getDescription() );
		assertEquals(events.get(0).getCategoryID(),event.getCategoryID() );
		assertEquals(events.get(0).getColor(),event.getColor() );
		assertEquals(events.get(0).getOccuranceIndex(),event.getOccuranceIndex() );
		assertEquals(events.get(0).getTotalOccurance(),event.getTotalOccurance() );
	
	}
	
	
	public void testXDeleteEvent() throws Exception {
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, -50);
		
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 50);
		
		List<Event> events = mEventManager.readEvents(startDate, endDate);
		
		for(Event e :events){
			mEventManager.deleteEvent(e.getStartDate(), e.getEndDate());
		}
		
		events = mEventManager.readEvents(startDate, endDate);
		assertEquals(0,events.size());
		
		
	}
	


	
}
