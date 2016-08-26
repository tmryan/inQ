package tryan.inq.event;

import tryan.inq.state.QActorState;

public abstract class QGameEvent {
	private int priority;
	// Note: Only supporting one actorState for now, but eventually will need more
	private QActorState actorState;
	
	public QGameEvent(int priority) {
		this.priority = priority;
		actorState = null;
	}
	
	public QGameEvent(int priority, QActorState actorState) {
		this.priority = priority;
		this.actorState = actorState;
	} 
	
	public abstract void playEvent();
	
	public int getPriority() {
		return priority;
	}
	
	public QActorState getTriggerActor() {
		return actorState;
	}
	
	public void setTriggerActor(QActorState triggerActor) {
		actorState = triggerActor;
	}
	
}
