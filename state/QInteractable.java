package tryan.inq.state;

import tryan.inq.event.QGameEvent;

public interface QInteractable {
	public void onUse();
	public void onHover();
	public void addGameEvent(QGameEvent gameEvent);
	public QGameEvent getGameEvent();
}
