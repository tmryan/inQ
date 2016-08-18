package tryan.inq.event;

import tryan.inq.mobility.QBounds;

//Note: This class has a QBounds, checks to see if a given actor is inside, and creates a QActionEvent object if so
public class QAreaTrigger {
	private QBounds bounds;
	private QGameEvent gameEvent;
	private boolean isOneShot;
	// Note: Need to figure out how to store and execute scripted events
	
	// Note: Figure out best way to pass triggering actor in to event at runtime
	
	// Note: Triggers need lifetime or flag for removal upon completion
	
	public QAreaTrigger(int x, int y, int width, int height, boolean isOneShot) {
		bounds = new QBounds(x, y, width, height);
		gameEvent = null;
		this.isOneShot = isOneShot;
	}
	
	public boolean containsCoords(int x, int y) {
		return bounds.containsCoords(x, y);
	}
	
	// Note: Need to allow for multiple events at some point
	public void addGameEvent(QGameEvent gameEvent) {
		this.gameEvent = gameEvent;
	}
	
	public QGameEvent getGameEvent() {
		return gameEvent;
	}
	
	public boolean isOneShot() {
		return isOneShot;
	}
	
}
