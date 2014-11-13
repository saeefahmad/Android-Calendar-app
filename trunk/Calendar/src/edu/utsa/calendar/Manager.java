package edu.utsa.calendar;

/**
 * Singleton class for solely managing category manager and event manager
 */

public class Manager {

	private EventManager eventManager;
	private CategoryManager categoryManager;
	private static Manager manager = null;

	private Manager() {

	}

	public static Manager getInstance() {
		if (manager == null) {

			manager = new Manager();
		}
		return manager;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	public CategoryManager getCategoryManager() {
		return categoryManager;
	}

	public void setCategoryManager(CategoryManager categoryManager) {
		this.categoryManager = categoryManager;
	}

}
