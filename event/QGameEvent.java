package tryan.inq.event;

public abstract class QGameEvent {
	private int priority;
	
	public QGameEvent(int priority) {
		this.priority = priority;
	}
	
	public abstract void playEvent();
	
	public int getPriority() {
		return priority;
	}
}
