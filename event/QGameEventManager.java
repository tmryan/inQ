package tryan.inq.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import tryan.inq.state.QActorState;

public class QGameEventManager {
	private PriorityQueue<QGameEvent> eventQueue;
	private ArrayList<QAreaTrigger> areaTriggers;
	private ArrayList<QTimedEvent> timedEvents;
	
	public QGameEventManager() {
		areaTriggers = new ArrayList<QAreaTrigger>();
		timedEvents = new ArrayList<QTimedEvent>();
		// Note: Need to pass Comparator to eventQueue
		eventQueue = new PriorityQueue<QGameEvent>(new Comparator<QGameEvent>() {

			public int compare(QGameEvent o1, QGameEvent o2) {
				int result = 0;
				
				if(o1.getPriority() > o2.getPriority()) {
					result = 1;
				} else if(o1.getPriority() < o2.getPriority()) {
					result = -1;
				}
				
				return result;
			}
		});
	}
	
	// Enqueue game event
	public void addGameEvent(QGameEvent gameEvent) {
		eventQueue.add(gameEvent);
	}
	
	public void addAreaTrigger(QAreaTrigger areaTrigger) {
		areaTriggers.add(areaTrigger);
	}
	
	public void addTimedEvent(QTimedEvent timedEvent) {
		timedEvents.add(timedEvent);
	}
	
	public void checkAreaTriggers(QActorState actorState) {
		// See if the given actor is inside any area trigger, add event to queue if so
		for(int i = 0; i < areaTriggers.size(); i++) {
			// Note: This needs to check if bounds is clipped, not just coords
			if(areaTriggers.get(i).containsCoords(actorState.getX(), actorState.getY())) {
				addGameEvent(areaTriggers.get(i).getGameEvent());
				
				// If trigger flagged as one shot, remove from list
				if(areaTriggers.get(i).isOneShot()) {
					areaTriggers.remove(i);
				}
			}
		}
	}
	
	public void checkTimedEvents(long tickTime) {
		for(int i = 0; i < timedEvents.size(); i++) {
			// Note: This needs to check if bounds is clipped, not just coords
			if(timedEvents.get(i).isTimeExpired(tickTime)) {
				addGameEvent(timedEvents.get(i).getGameEvent());
				
				// If trigger flagged as one shot, remove from list
				if(timedEvents.get(i).isOneShot()) {
					timedEvents.remove(i);
				}
			}
		}
	}
	
	///////////////
	// eventQueue
	///////////
	
	public boolean hasNextEvent() {
		return !eventQueue.isEmpty();
	}
	
	public QGameEvent getNextEvent() {
		return eventQueue.poll();
	}
	
}
