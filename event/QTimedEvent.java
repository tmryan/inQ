package tryan.inq.event;

public class QTimedEvent {
	private int interval;
	private int tDelta;
	private QGameEvent gameEvent;
	private boolean isOneShot;

	public QTimedEvent(int interval, boolean isOneShot) {
		this.interval = interval;
		tDelta = 0;
		gameEvent = null;
		this.isOneShot = isOneShot;
	}

	public boolean isTimeExpired(long tickTime) {
		boolean timerExpired = false;
		tDelta += tickTime;

		if(tDelta > interval) {
			timerExpired = true;
			tDelta = 0;
		}
		
		return timerExpired;
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
