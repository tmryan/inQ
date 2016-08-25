package tryan.inq.state;

import tryan.inq.event.QGameEvent;
import tryan.inq.mobility.QDirection;

/*
 * Note: Need to abstract actor states soon
 * 		 For example: this doesn't need movers, just animation states
 */
public class QInteractableState extends QActorState implements QInteractable {	
	private boolean isHightlighted;
	private QGameEvent gameEvent;
	private int tDelta;
	
	public QInteractableState(int x, int y, int width, int height, int id) {
		super(x, y, width, height, 0, id, null);
		
		isHightlighted = false;
		gameEvent = null;
		tDelta = 0;
	}
	
	@Override
	public void onTick(long tickTime, QDirection direction) {
		// Do something every game tick
	}
	
	@Override
	public void onUse() {
		System.out.println("Clicked actor " + getActorId() + "!");
	}

	@Override
	public void onHover() {
		isHightlighted = true;
	}

	// Note: Need to allow for multiple events at some point
	@Override
	public void addGameEvent(QGameEvent gameEvent) {
		this.gameEvent = gameEvent;
	}
	
	@Override
	public QGameEvent getGameEvent() {
		return gameEvent;
	}
	
	public boolean isHighlighted() {
		return isHightlighted;
	}
	
	public void removeHighlight() {
		isHightlighted = false;
	}
}
