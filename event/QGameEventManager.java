package tryan.inq.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import tryan.inq.state.QDynamicActorState;

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
	
	public void addAreaTrigger(QAreaTrigger areaTrigger) {
		areaTriggers.add(areaTrigger);
	}
	
	public void addTimedEvent(QTimedEvent timedEvent) {
		timedEvents.add(timedEvent);
	}
	
	public void checkAreaTriggers(QDynamicActorState causalActor) {
		// See if the given actor is inside any area trigger, add event to queue if so
		Iterator<QAreaTrigger> iter = areaTriggers.iterator();
		QAreaTrigger trigger = null;
		
		while(iter.hasNext()) {
			trigger = iter.next();
			
			// Note: This needs to check if bounds is clipped, not just coords
			if(trigger.containsCoords(causalActor.getX(), causalActor.getY())) {
				addGameEvent(trigger.getGameEvent());
				trigger.setCausalActor(causalActor);
				
				// If trigger flagged as one shot, remove from list
				if(trigger.isOneShot()) {
					iter.remove();
				}
			}
		}
	}
	
	public void checkTimedEvents(long tickTime) {
		Iterator<QTimedEvent> iter = timedEvents.iterator();
		QTimedEvent timer = null;
		
		while(iter.hasNext()) {
			timer = iter.next();
			
			// Note: This needs to check if bounds is clipped, not just coords
			if(timer.isTimeExpired(tickTime)) {
				addGameEvent(timer.getGameEvent());
				
				// If trigger flagged as one shot, remove from list
				if(timer.isOneShot()) {
					iter.remove();
				}
			}
		}
	}

	// Enqueue game event
	public void addGameEvent(QGameEvent gameEvent) {
		if(gameEvent != null){
			eventQueue.add(gameEvent);
		}
	}
	
	public boolean hasNextEvent() {
		return !eventQueue.isEmpty();
	}
	
	public QGameEvent getNextEvent() {
		// Note: May need to remove event trigger from queue after polling
		return eventQueue.poll();
	}
	
}
