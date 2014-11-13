package edu.utsa.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Create and manage sqlite database tables access 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	
	private final static int DATABASE_VERSION = 8;
	public final static String DATABASE_NAME = "CalenderDB";
    private final String EVENT_TABLE_NAME = "Event";
   
    // Event Table Columns names
    private final String EVENTS_KEY_ID = "event_id";
    private final String EVENTS_START_TIME = "start_time"; 
    private final String EVENTS_END_TIME = "end_time"; 
    private final String EVENTS_DESCRIPTION = "details";
    private final String EVENTS_CATEGORY = "category";
    private final String EVENTS_TOTAL_OCCURANCE = "total_occurance";
    private final String EVENTS_OCCURANCE_INDEX = "occurance_index";
 
    //Category Table Columns names
    private final String CATEGORY_TABLE_NAME = "Category";
    private final String CATEGORY_KEY_ID = "category_id";
    private final String CATEGORY_COLOR = "color"; 
    private final String CATEGORY_TYPE = "type";
    private final String CATEGORY_DESCRIPTION = "details";

    public DatabaseHelper(Context context, String pDataBaseName) {
        super(context, pDataBaseName, null, DATABASE_VERSION);
    }
    
    /**
     * Create Two Tables : Events and Categories 
     */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_EVENTS_TABLE = "CREATE TABLE " + EVENT_TABLE_NAME + "("
                + EVENTS_KEY_ID + " INTEGER PRIMARY KEY," 
				+ EVENTS_DESCRIPTION + " TEXT,"
                + EVENTS_START_TIME +" INTEGER,"
                + EVENTS_END_TIME +" INTEGER,"
                + EVENTS_CATEGORY +" TEXT,"
                + EVENTS_TOTAL_OCCURANCE+" INTEGER,"
                + EVENTS_OCCURANCE_INDEX+" INTEGER"
                +  ")";
        db.execSQL(CREATE_EVENTS_TABLE);
       
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + CATEGORY_TABLE_NAME + "("
                + CATEGORY_KEY_ID + " INTEGER PRIMARY KEY,"
        		+ CATEGORY_COLOR + " INTEGER,"
                + CATEGORY_TYPE + " TEXT," + CATEGORY_DESCRIPTION + " TEXT"+ ")";
        db.execSQL(CREATE_CATEGORIES_TABLE);
		
	}

	

	public String getEVENTS_CATEGORY() {
		return EVENTS_CATEGORY;
	}

	public String getEVENTS_TOTAL_OCCURANCE() {
		return EVENTS_TOTAL_OCCURANCE;
	}

	public String getEVENTS_OCCURANCE_INDEX() {
		return EVENTS_OCCURANCE_INDEX;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
 
        // Create tables again
        onCreate(db);
		
	}
	
	public int getDatabaseVersion() {
		return DATABASE_VERSION;
	}

	public String getEventsStartTime() {
		return EVENTS_START_TIME;
	}

	public String getEventsEndTime() {
		return EVENTS_END_TIME;
	}

	public SQLiteDatabase getDatabaseForRead() {
		return this.getReadableDatabase();
	}
	/**
	 * @return {@code SQLiteDatabase} write database instance
	 */
	public SQLiteDatabase getDatabaseForWrite() {
		return this.getWritableDatabase();
	}

	public int getDATABASE_VERSION() {
		return DATABASE_VERSION;
	}

	
	public String getEventTableName() {
		return EVENT_TABLE_NAME;
	}

	public String getCategoryTableName() {
		return CATEGORY_TABLE_NAME;
	}

	public  String getEventsKeyId() {
		return EVENTS_KEY_ID;
	}

	public String getEventsDescription() {
		return EVENTS_DESCRIPTION;
	}

	public String getCategoryKeyId() {
		return CATEGORY_KEY_ID;
	}

	public String getCategoryColor() {
		return CATEGORY_COLOR;
	}

	public String getCategoryType() {
		return CATEGORY_TYPE;
	}

	public String getCategoryDescription() {
		return CATEGORY_DESCRIPTION;
	}
	

}
